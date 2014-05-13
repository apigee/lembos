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

package io.apigee.lembos.node.modules;

import io.apigee.lembos.node.types.CounterGroupWrap;
import io.apigee.lembos.node.types.CounterWrap;
import io.apigee.lembos.node.types.CountersWrap;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.InternalNodeModule;
import io.apigee.trireme.core.NodeModule;
import io.apigee.trireme.core.NodeRuntime;
import io.apigee.trireme.core.internal.ModuleRegistry;
import io.apigee.trireme.core.internal.ScriptRunner;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of {@link InternalNodeModule} that provides {@link CounterGroupWrap}, {@link CountersWrap}
 * and {@link CounterWrap}.  (For internal use only as these expose types not instantiable via JavaScript.)
 */
public final class HadoopInternal implements InternalNodeModule {

    public static final String MODULE_NAME = "hadoop-internal";
    private Scriptable exports;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable registerExports(final Context ctx, final Scriptable global, final NodeRuntime runtime)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        exports = ctx.newObject(global);

        exports.setPrototype(global);
        exports.setParentScope(null);

        ScriptableObject.defineClass(exports, CounterGroupWrap.class);
        ScriptableObject.defineClass(exports, CountersWrap.class);
        ScriptableObject.defineClass(exports, CounterWrap.class);

        return exports;
    }

    /**
     * @return module exports
     */
    public Scriptable getExports() {
        return exports;
    }

    /**
     * Loads this module.
     *
     * @param runtime the node runtime
     *
     * @return exports
     */
    public static Scriptable loadSelf(final NodeRuntime runtime) {
        final ModuleRegistry moduleRegistry = ((ScriptRunner)runtime).getRegistry();
        NodeModule rawModule = moduleRegistry.get(HadoopInternal.MODULE_NAME);
        Context ctx = Context.getCurrentContext();

        if (ctx == null) {
            ctx = Context.enter();
        }

        if (rawModule == null) {
            ((ScriptRunner)runtime).requireInternal(HadoopInternal.MODULE_NAME, ctx);

            rawModule = moduleRegistry.getInternal(HadoopInternal.MODULE_NAME);
        }

        if (rawModule == null) {
            throw new RuntimeException("Unable to load the " + HadoopInternal.MODULE_NAME + " for internal use");
        }

        final HadoopInternal module = (HadoopInternal)rawModule;
        Scriptable exports = module.getExports();

        if (!JavaScriptUtils.isDefined(exports)) {
            try {
                exports = (Scriptable)((ScriptRunner)runtime).initializeModule(HadoopInternal.MODULE_NAME,
                                                                               false, ctx,
                                                                               ((ScriptRunner)runtime)
                                                                                       .getScriptScope());
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Unable to load the " + HadoopInternal.MODULE_NAME
                                                   + " for internal use");
            }

        }

        return exports;
    }

}
