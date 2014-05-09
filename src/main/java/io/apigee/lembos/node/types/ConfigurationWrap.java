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

package io.apigee.lembos.node.types;

import io.apigee.lembos.mapreduce.LembosMessages;
import io.apigee.lembos.node.modules.HadoopConfiguration;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.NodeModule;
import io.apigee.trireme.core.NodeRuntime;
import io.apigee.trireme.core.Utils;
import io.apigee.trireme.core.internal.ModuleRegistry;
import io.apigee.trireme.core.internal.ScriptRunner;
import org.apache.hadoop.conf.Configuration;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

/**
 * Java implementation of the {@link Configuration} JavaScript object.
 *
 * <b>Unsupported APIs:</b>
 * <ul>
 *   <li>{@link Configuration#addResource(java.io.InputStream)}</li>
 *   <li>{@link Configuration#dumpConfiguration(Configuration, java.io.Writer)}</li>
 *   <li>{@link Configuration#getClass(String, Class)}</li>
 *   <li>{@link Configuration#getClass(String, Class, Class)}</li>
 *   <li>{@link Configuration#getClassByName(String)}</li>
 *   <li>{@link Configuration#getClasses(String, Class[])}</li>
 *   <li>{@link Configuration#getClassLoader()}</li>
 *   <li>{@link Configuration#getConfResourceAsInputStream(String)}</li>
 *   <li>{@link Configuration#getConfResourceAsReader(String name)}</li>
 *   <li>{@link Configuration#getEnum(String, Enum)}</li>
 *   <li>{@link Configuration#getFile(String, String)}</li>
 *   <li>{@link Configuration#getInstances(String, Class)}</li>
 *   <li>{@link Configuration#getLocalPath(String, String)}</li>
 *   <li>{@link Configuration#getRange(String, String)}</li>
 *   <li>{@link Configuration#iterator()}</li>
 *   <li>{@link Configuration#readFields(java.io.DataInput)}</li>
 *   <li>{@link Configuration#setClassLoader(ClassLoader)}</li>
 *   <li>{@link Configuration#setEnum(String, Enum)}</li>
 *   <li>{@link Configuration#write(java.io.DataOutput)}</li>
 *   <li>{@link Configuration#writeXml(java.io.OutputStream)}</li>
 * </ul>
 */
public final class ConfigurationWrap extends ScriptableObject {

    private static final long serialVersionUID = -298565766333527676L;
    public static final String CLASS_NAME = "Configuration";

    // These transient fields are to please Findbugs.  I realize why the errors come up but I don't see us ever
    // serializing this object.  It will always be constructed during the MapReduce component setup phase.

    private transient Configuration conf;

    /**
     * Creates an instance of {@link ConfigurationWrap}.
     *
     * @param runtime the Node.js runtime
     * @param conf the Hadoop configuration to wrap
     *
     * @return the created configuration wrapper
     */
    public static ConfigurationWrap getInstance(final NodeRuntime runtime, final Configuration conf) {
        final ModuleRegistry moduleRegistry = ((ScriptRunner)runtime).getRegistry();
        NodeModule rawModule = moduleRegistry.get(HadoopConfiguration.MODULE_NAME);
        Context ctx = Context.getCurrentContext();

        if (ctx == null) {
            ctx = Context.enter();
        }

        if (rawModule == null) {
            runtime.require(HadoopConfiguration.MODULE_NAME, ctx);

            rawModule = moduleRegistry.get(HadoopConfiguration.MODULE_NAME);
        }

        if (rawModule == null) {
            throw new RuntimeException("Unable to load the " + HadoopConfiguration.MODULE_NAME + " for internal use");
        }

        final HadoopConfiguration module = (HadoopConfiguration)rawModule;
        Scriptable exports = module.getExports();

        if (!JavaScriptUtils.isDefined(exports)) {
            try {
                exports = (Scriptable)((ScriptRunner)runtime).initializeModule(HadoopConfiguration.MODULE_NAME,
                                                                               false, ctx,
                                                                               ((ScriptRunner)runtime)
                                                                                       .getScriptScope());
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Unable to load the " + HadoopConfiguration.MODULE_NAME
                                                   + " for internal use");
            }
        }

        final ConfigurationWrap wrapper = (ConfigurationWrap)ctx.newObject(exports, CLASS_NAME);

        wrapper.conf = conf;

        return wrapper;
    }

    /**
     * JavaScript constructor.
     *
     * @param ctx the JavaScript context
     * @param args the constructor arguments
     * @param ctorObj the constructor function
     * @param newExpr if a new expression caused the call
     *
     * @return the newly constructed configuration wrapper
     */
    @JSConstructor
    public static Object wrapConstructor(final Context ctx, final Object[] args, final Function ctorObj,
                                         final boolean newExpr) {
        Configuration conf;

        if (args.length == 0 || !JavaScriptUtils.isDefined(args[0])) {
            conf = new Configuration();
        } else if (args.length == 1) {
            final Object arg0 = args[0];

            if (arg0 instanceof Boolean) {
                conf = new Configuration((boolean)arg0);
            } else if (arg0 instanceof ConfigurationWrap) {
                conf = ((ConfigurationWrap)args[0]).conf;
            } else {
                throw Utils.makeError(ctx, ctorObj, LembosMessages.FIRST_ARG_MUST_BE_BOOL_OR_CONF);
            }
        } else {
            throw Utils.makeError(ctx, ctorObj, LembosMessages.ZERO_OR_ONE_ARG_EXPECTED);
        }

        ConfigurationWrap wrapper;

        if (newExpr) {
            wrapper = new ConfigurationWrap();
        } else {
            wrapper = (ConfigurationWrap)ctx.newObject(ctorObj, CLASS_NAME);
        }

        wrapper.conf = conf;

        return wrapper;
    }

    /**
     * Wraps {@link Configuration#addDefaultResource(String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     */
    @JSStaticFunction
    public static void addDefaultResource(final Context ctx, final Scriptable thisObj, final Object[] args,
                                          final Function func) {
        if (args.length == 0 || !JavaScriptUtils.isDefined(args[0])) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        Configuration.addDefaultResource(args[0].toString());
    }

    /**
     * Wraps {@link Configuration#addResource(org.apache.hadoop.fs.Path)}, {@link Configuration#addResource(String)}
     * and {@link Configuration#addResource(URL)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object addResource(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        if (args.length == 0 || !JavaScriptUtils.isDefined(args[0])) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        ((ConfigurationWrap)thisObj).conf.addResource(args[0].toString());

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#clear()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object clear(final Context ctx, final Scriptable thisObj, final Object[] args,
                               final Function func) {
        ((ConfigurationWrap)thisObj).conf.clear();

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#get(String)} and {@link Configuration#get(String, String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the string configuration value
     */
    @JSFunction
    public static Object get(final Context ctx, final Scriptable thisObj, final Object[] args, final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_OR_TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        }

        final ConfigurationWrap self = (ConfigurationWrap)thisObj;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;
        String confStr;

        if (JavaScriptUtils.isDefined(arg0) && JavaScriptUtils.isDefined(arg1)) {
            confStr = self.conf.get(arg0.toString(), arg1.toString());
        } else {
            confStr = self.conf.get(arg0.toString());
        }

        return confStr == null ? Context.getUndefinedValue() : confStr;
    }

    /**
     * Wraps {@link Configuration#getBoolean(String, boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the boolean configuration value
     */
    @JSFunction
    public static Object getBoolean(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Boolean)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_BOOL);
        }

        return ((ConfigurationWrap)thisObj).conf.getBoolean(arg0.toString(), (Boolean)arg1);
    }

    /**
     * Wraps {@link Configuration#getFloat(String, float)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the float configuration value
     */
    @JSFunction
    public static Object getFloat(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        // Converted to double because Float is not something supported by JavaScript
        return Double.valueOf(Float.toString(((ConfigurationWrap)thisObj).conf
                                                     .getFloat(arg0.toString(),
                                                               JavaScriptUtils.fromNumber(arg1).floatValue())));
    }

    /**
     * Wraps {@link Configuration#getInt(String, int)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the int configuration value
     */
    @JSFunction
    public static Object getInt(final Context ctx, final Scriptable thisObj, final Object[] args,
                                final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        return ((ConfigurationWrap)thisObj).conf.getInt(arg0.toString(),
                                                        JavaScriptUtils.fromNumber(arg1).intValue());
    }

    /**
     * Wraps {@link Configuration#getLong(String, long)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the int configuration value
     */
    @JSFunction
    public static Object getLong(final Context ctx, final Scriptable thisObj, final Object[] args,
                                 final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        return ((ConfigurationWrap)thisObj).conf.getLong(arg0.toString(),
                                                         JavaScriptUtils.fromNumber(arg1).longValue());
    }

    /**
     * Wraps {@link Configuration#getRaw(String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the string configuration value without variable expansion
     */
    @JSFunction
    public static Object getRaw(final Context ctx, final Scriptable thisObj, final Object[] args,
                                final Function func) {
        if (args.length == 0 || !JavaScriptUtils.isDefined(args[0])) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final ConfigurationWrap self = (ConfigurationWrap)thisObj;
        final String confStr = self.conf.getRaw(args[0].toString());

        return confStr == null ? Context.getUndefinedValue() : confStr;
    }

    /**
     * Wraps {@link Configuration#getResource(String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the url of the named resource
     */
    @JSFunction
    public static Object getResource(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        if (args.length == 0 || !JavaScriptUtils.isDefined(args[0])) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final URL resource = ((ConfigurationWrap)thisObj).conf.getResource(args[0].toString());

        return resource == null ? Context.getUndefinedValue() : resource.toString();
    }

    /**
     * Wraps {@link Configuration#getStringCollection(String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return property value as an array of strings or null
     */
    @JSFunction
    public static Object getStringCollection(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        if (args.length == 0 || !JavaScriptUtils.isDefined(args[0])) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final ConfigurationWrap self = (ConfigurationWrap)thisObj;
        final Collection<String> strings = self.conf.getStringCollection(args[0].toString());

        // Call returns either the matching strings or an empty collection
        return JavaScriptUtils.asArray(thisObj, strings);
    }

    /**
     * Wraps {@link Configuration#getStrings(String)} and {@link Configuration#getStrings(String, String...)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return property value as an array of strings or null
     */
    @JSFunction
    public static Object getStrings(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_OR_TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (JavaScriptUtils.isDefined(arg1) && !(arg1 instanceof NativeArray)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_ARRAY);
        }

        final ConfigurationWrap self = (ConfigurationWrap)thisObj;
        String[] strings;

        if (JavaScriptUtils.isDefined(arg1)) {
            final NativeArray jsDefaults = (NativeArray)arg1;
            final String[] defaults = new String[Integer.valueOf(Long.toString(jsDefaults.getLength()))];

            for (int i = 0; i < defaults.length; i++) {
                defaults[i] = jsDefaults.get(i).toString();
            }

            strings = self.conf.getStrings(arg0.toString(), defaults);
        } else {
            strings = self.conf.getStrings(arg0.toString());
        }

        // Returns null for missing property and no default
        return strings == null ? null : JavaScriptUtils.asArray(thisObj, strings);
    }

    /**
     * Wraps {@link Configuration#getValByRegex(String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the map of matching configuration values
     */
    @JSFunction
    public static Scriptable getValByRegex(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        if (args.length == 0 || !JavaScriptUtils.isDefined(args[0])) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final ConfigurationWrap self = (ConfigurationWrap)thisObj;
        Map<String, String> confMap;

        String regex = args[0].toString();

        // Strip leading slash
        if (regex.charAt(0) == '/') {
            regex = regex.substring(1);
        }

        // Strip trailing slash
        if (regex.endsWith("/")) {
            regex = regex.substring(0, regex.length() - 1);
        }

        // Strip trailing slash with modifier
        if (regex.endsWith("/g") || regex.endsWith("/i") || regex.endsWith("/m")) {
            regex = regex.substring(0, regex.length() - 2);
        }

        confMap = self.conf.getValByRegex(regex);

        return JavaScriptUtils.asObject(thisObj, confMap);
    }

    /**
     * Wraps {@link Configuration#reloadConfiguration()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object reloadConfiguration(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        ((ConfigurationWrap)thisObj).conf.reloadConfiguration();

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#set(String, String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object set(final Context ctx, final Scriptable thisObj, final Object[] args, final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        }

        ((ConfigurationWrap)thisObj).conf.set(arg0.toString(), arg1.toString());

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setBoolean(String, boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setBoolean(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Boolean)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_BOOL);
        }

        ((ConfigurationWrap)thisObj).conf.setBoolean(arg0.toString(), (Boolean)arg1);

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setBooleanIfUnset(String, boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setBooleanIfUnset(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Boolean)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_BOOL);
        }

        ((ConfigurationWrap)thisObj).conf.setBooleanIfUnset(arg0.toString(), (Boolean)arg1);

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setClass(String, Class, Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;
        final Object arg2 = args.length >= 3 ? args[2] : Undefined.instance;

        if (args.length < 3) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THREE_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg2)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THIRD_ARG_REQUIRED);
        }

        try {
            ((ConfigurationWrap)thisObj).conf.setClass(arg0.toString(),
                                                       Class.forName(arg1.toString()),
                                                       Class.forName(arg2.toString()));
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setFloat(String, float)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setFloat(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        ((ConfigurationWrap)thisObj).conf.setFloat(arg0.toString(), JavaScriptUtils.fromNumber(arg1).floatValue());

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setIfUnset(String, String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setIfUnset(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        }

        ((ConfigurationWrap)thisObj).conf.setIfUnset(arg0.toString(), arg1.toString());

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setInt(String, int)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setInt(final Context ctx, final Scriptable thisObj, final Object[] args,
                              final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        ((ConfigurationWrap)thisObj).conf.setInt(arg0.toString(),
                                                 JavaScriptUtils.fromNumber(arg1).intValue());

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setLong(String, long)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setLong(final Context ctx, final Scriptable thisObj, final Object[] args,
                                 final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        ((ConfigurationWrap)thisObj).conf.setLong(arg0.toString(),
                                                  JavaScriptUtils.fromNumber(arg1).longValue());

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setQuietMode(boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setQuietMode(final Context ctx, final Scriptable thisObj, final Object[] args,
                                      final Function func) {
        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!(args[0] instanceof Boolean)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_BOOL);
        }

        ((ConfigurationWrap)thisObj).conf.setQuietMode((Boolean)args[0]);

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#setStrings(String, String...)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setStrings(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof NativeArray)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_ARRAY);
        }

        final NativeArray jsArray = (NativeArray)args[1];
        final String[] strings = new String[jsArray.size()];

        for (int i = 0; i < jsArray.size(); i++) {
            strings[i] = jsArray.get(i).toString();
        }

        ((ConfigurationWrap)thisObj).conf.setStrings(args[0].toString(), strings);

        return thisObj;
    }

    /**
     * Wraps {@link Configuration#size()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the number of keys in the configuration
     */
    @JSFunction
    public static int size(final Context ctx, final Scriptable thisObj, final Object[] args,
                           final Function func) {
        return ((ConfigurationWrap)thisObj).conf.size();
    }

    /**
     * Wraps {@link Configuration#toString()}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the string representation
     */
    @JSFunction("toString")
    public static Object jsToString(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        return ((ConfigurationWrap)thisObj).conf.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    /**
     * @return the wrapped configuration
     */
    public Configuration getConf() {
        return conf;
    }

    /**
     * @param conf the Hadoop configuration to wrap
     */
    public void setConf(final Configuration conf) {
        this.conf = conf;
    }

}
