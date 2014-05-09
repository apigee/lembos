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

import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.Utils;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.concurrent.CountDownLatch;

/**
 * Implementation of {@link BaseFunction} to be used as the "done" callback.
 */
public class LembosDoneCallback extends BaseFunction {

    private static final long serialVersionUID = -6464376989605227444L;
    private transient CountDownLatch doneLatch;
    private Object response;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context ctx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (JavaScriptUtils.isDefined(args) && args.length > 0) {
            final Object jsResponse = args[0];

            if (JavaScriptUtils.isDefined(jsResponse) && jsResponse instanceof ScriptableObject) {
                final ScriptableObject pError = (ScriptableObject)jsResponse;
                final Object pName = ScriptableObject.getProperty(pError, "name");

                if (pName != null && pName.toString().endsWith("Error")) {
                    response = Utils.makeError(ctx, scope, ScriptableObject.getProperty(pError, "message").toString());
                }
            } else {
                response = jsResponse;
            }
        }

        doneLatch.countDown();

        return super.call(ctx, scope, thisObj, args);
    }

    /**
     * Must be called prior to the function the {@link LembosDoneCallback} is being passed to.
     */
    public void prepare() {
        doneLatch = new CountDownLatch(1);
    }

    /**
     * Must be called after the function the {@link LembosDoneCallback} is being passed to.
     *
     * @return the object passed to the callback
     *
     * @throws InterruptedException if something goes wrong
     */
    public Object await() throws InterruptedException {
        // TODO: We should enable some sort of timeout mechanism
        doneLatch.await();

        return response;
    }

}
