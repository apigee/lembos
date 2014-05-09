/*
 * Copyright 2014 Apigee Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apigee.lembos.mapreduce;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeRuntime;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptFuture;
import io.apigee.trireme.core.internal.ModuleRegistry;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * This class will construct an environment allowing for Node.js modules to be driven via Java.
 */
public class LembosNodeEnvironment {

    private final String moduleName;
    private final File modulePath;
    private final String[] moduleArgs;

    private File moduleWrapperJS;
    private Function functionWrapper;
    private Scriptable module;
    private ModuleRegistry moduleRegistry;
    private ScriptFuture runningScript;
    private NodeRuntime runtime;

    /**
     * Constructor.
     *
     * @param moduleName the Node.js module name
     * @param modulePath the Node.js module location/path
     * @param moduleArgs the Node.js module arguments (CLI arguments)
     */
    public LembosNodeEnvironment(final String moduleName, final File modulePath, @Nullable final String[] moduleArgs) {
        this.moduleName = moduleName;
        this.modulePath = modulePath;
        this.moduleArgs = moduleArgs == null ? null : moduleArgs.clone();
    }

    /**
     * Initializes the Node.js environment.
     *
     * @throws ExecutionException if something goes wrong executing the Node.js module
     * @throws IOException if something goes wrong setting up the environment
     * @throws NodeException if there is an issue with the Node.js module
     * @throws InterruptedException if the execution of the Node.js module gets interrupted
     */
    public void initialize() throws IOException, NodeException, ExecutionException, InterruptedException {
        if (this.moduleName == null || this.moduleName.trim().isEmpty()) {
            throw new RuntimeException("Module name cannot be null");
        }

        if (!this.modulePath.exists()) {
            throw new RuntimeException("Module path does not exist: " + this.modulePath.getAbsolutePath());
        }

        // Create the "main.js" file used to load the MapReduce environment from the Node.js module
        this.moduleWrapperJS = File.createTempFile("Lembos-Node-Wrapper-", ".js");

        final Writer mainJsWriter = new OutputStreamWriter(new FileOutputStream(this.moduleWrapperJS), "UTF-8");

        mainJsWriter.write("var domain = require('domain').create();\n");
        mainJsWriter.write("var path = require('path');\n");
        mainJsWriter.write("var doneCallback;\n");
        mainJsWriter.write("domain.on('error', function (err) {\n");
        mainJsWriter.write("  if (err.stack) {\n");
        mainJsWriter.write("    console.error(err.stack);\n");
        mainJsWriter.write("  }\n");
        mainJsWriter.write("  doneCallback(err);\n");
        mainJsWriter.write("});\n");
        mainJsWriter.write("module.exports = require('" + moduleName + "');\n");
        mainJsWriter.write("module.exports.__wrapFunc = function (funcToCall, funcArgs) {\n");
        mainJsWriter.write("  doneCallback = funcArgs[funcArgs.length - 1];\n");
        mainJsWriter.write("  domain.run(function() {\n");
        mainJsWriter.write("    funcToCall.apply(this, funcArgs);\n");
        mainJsWriter.write("  });\n");
        mainJsWriter.write("};");

        mainJsWriter.close();

        // Create the script environment
        final NodeEnvironment nodeEnv = new NodeEnvironment();
        final NodeScript nodeScript = nodeEnv.createScript(this.moduleWrapperJS.getName(), this.moduleWrapperJS,
                                                           this.moduleArgs);
        final ConcurrentHashMap<String, String> nodeEnvironmentVariables = new ConcurrentHashMap<>();

        // For our Node.js applications, they are self-contained and only need to have the parent as NODE_PATH
        nodeEnvironmentVariables.put("NODE_PATH", this.modulePath.getAbsoluteFile().getParentFile().getAbsolutePath());

        nodeScript.setEnvironment(nodeEnvironmentVariables);

        this.runningScript = nodeScript.executeModule();
        this.module = this.runningScript.getModuleResult();
        this.functionWrapper = (Function)ScriptableObject.getProperty(this.getModule(), "__wrapFunc");
        this.runtime = runningScript.getRuntime();
        this.moduleRegistry = nodeEnv.getRegistry(null);
    }

    /**
     * @return the loaded module
     */
    public Scriptable getModule() {
        return this.module;
    }

    /**
     * @return the module args
     */
    public String[] getModuleArgs() {
        return this.moduleArgs == null ? null : this.moduleArgs.clone();
    }

    /**
     * @return the module name
     */
    public String getModuleName() {
        return this.moduleName;
    }

    /**
     * @return the module path
     */
    public File getModulePath() {
        return this.modulePath;
    }

    /**
     * @return the module registry
     */
    public ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    /**
     * @return the running script
     */
    public ScriptFuture getRunningScript() {
        return runningScript;
    }

    /**
     * @return the Node.js runtime
     */
    public NodeRuntime getRuntime() {
        return runtime;
    }

    /**
     * Calls the module function in a synchronous fashion, with error handling.
     *
     * @param funcToCall the function to call
     * @param args the arguments (Do not include the {@link LembosDoneCallback}, it's handled for you)
     *
     * @return the value passed to the callback if it's not an error
     */
    public Object callFunctionSync(final Function funcToCall, final Object[] args) {
        final LembosDoneCallback doneCallback = new LembosDoneCallback();

        // Reset the done callback latch
        doneCallback.prepare();

        // Prepare the arguments
        final Scriptable realArgs = getRunningScript().getRuntime().getEnvironment().getContextFactory()
                                                      .enterContext().newArray(this.module, args.length + 1);

        for (int i = 0; i < args.length; i++) {
            realArgs.put(i, realArgs, args[i]);
        }

        realArgs.put(args.length, realArgs, doneCallback);

        // Make the call, which is async
        getRunningScript().getRuntime().enqueueCallback(functionWrapper, module, module, new Object[] {
                funcToCall, realArgs
        });

        // Wait for the done latch to indicate the job is completed
        try {
            final Object response = doneCallback.await();

            if (response instanceof Throwable) {
                throw new RuntimeException(((Throwable)response).getMessage());
            }

            return response;
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

    /**
     * Cleans up all necessary pieces.
     */
    public void cleanup() {
        // Clean up the temporary file
        if (moduleWrapperJS != null && !moduleWrapperJS.delete()) {
            System.err.println("Unable to clean up temporary file: " + moduleWrapperJS.getAbsolutePath());
        }

        // Cancel the running script
        if (runningScript != null) {
            runningScript.cancel(true);
        }
    }

}
