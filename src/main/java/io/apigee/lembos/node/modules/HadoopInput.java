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

import io.apigee.lembos.node.types.CombineFileInputFormatWrap;
import io.apigee.lembos.node.types.DBInputFormatWrap;
import io.apigee.lembos.node.types.DataDrivenDBInputFormatWrap;
import io.apigee.lembos.node.types.FileInputFormatWrap;
import io.apigee.lembos.node.types.KeyValueTextInputFormatWrap;
import io.apigee.lembos.node.types.NLineInputFormatWrap;
import io.apigee.lembos.node.types.OracleDataDrivenDBInputFormatWrap;
import io.apigee.lembos.node.types.SequenceFileAsBinaryInputFormatWrap;
import io.apigee.lembos.node.types.SequenceFileAsTextInputFormatWrap;
import io.apigee.lembos.node.types.SequenceFileInputFilterWrap;
import io.apigee.lembos.node.types.SequenceFileInputFormatWrap;
import io.apigee.lembos.node.types.TextInputFormatWrap;
import io.apigee.trireme.core.NodeModule;
import io.apigee.trireme.core.NodeRuntime;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of {@link NodeModule} that provides static method access for
 * {@link org.apache.hadoop.mapreduce.InputFormat} extensions.
 */
public final class HadoopInput implements NodeModule {

    public static final String MODULE_NAME = "hadoop-input";

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
        final Scriptable exports = ctx.newObject(global);

        exports.setPrototype(global);
        exports.setParentScope(null);

        ScriptableObject.defineClass(exports, CombineFileInputFormatWrap.class);
        ScriptableObject.defineClass(exports, DataDrivenDBInputFormatWrap.class);
        ScriptableObject.defineClass(exports, DBInputFormatWrap.class);
        ScriptableObject.defineClass(exports, FileInputFormatWrap.class);
        ScriptableObject.defineClass(exports, KeyValueTextInputFormatWrap.class);
        ScriptableObject.defineClass(exports, NLineInputFormatWrap.class);
        ScriptableObject.defineClass(exports, OracleDataDrivenDBInputFormatWrap.class);
        ScriptableObject.defineClass(exports, SequenceFileInputFormatWrap.class);
        ScriptableObject.defineClass(exports, SequenceFileAsBinaryInputFormatWrap.class);
        ScriptableObject.defineClass(exports, SequenceFileAsTextInputFormatWrap.class);
        ScriptableObject.defineClass(exports, SequenceFileInputFilterWrap.class);
        ScriptableObject.defineClass(exports, TextInputFormatWrap.class);

        return exports;
    }

}
