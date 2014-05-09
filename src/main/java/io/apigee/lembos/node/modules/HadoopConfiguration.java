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

import io.apigee.lembos.node.types.ConfigurationWrap;
import io.apigee.trireme.core.NodeModule;
import io.apigee.trireme.core.NodeRuntime;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of {@link NodeModule} that provides {@link ConfigurationWrap}.
 */
public final class HadoopConfiguration implements NodeModule {

    public static final String MODULE_NAME = "hadoop-configuration";
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

        ScriptableObject.defineClass(exports, ConfigurationWrap.class);

        return exports;
    }

    /**
     * @return module exports
     */
    public Scriptable getExports() {
        return exports;
    }

}
