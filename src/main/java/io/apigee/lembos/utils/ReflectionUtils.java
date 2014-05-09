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

package io.apigee.lembos.utils;

import java.lang.reflect.Method;

/**
 * Various utilities for interacting with reflection.
 */
public final class ReflectionUtils {

    /**
     * Private constructor.
     */
    private ReflectionUtils() { }

    /**
     * Returns whether or not the actual class is the same or a subclass of expected.
     *
     * @param expected expected class
     * @param actual actual class
     *
     * @return true of actual is equal to expected or is a subclass of expected
     */
    public static boolean isClassOrSubclass(final Class<?> expected, final Class<?> actual) {
        return expected != null && actual != null && (actual.equals(expected) || expected.isAssignableFrom(actual));
    }

    /**
     * Invokes a static method on a class.
     *
     * @param clazz the class to invoke the method on
     * @param methodName the method name
     * @param argTypes the method argument types
     * @param args the method arguments
     *
     * @return the method result
     *
     * @throws Exception if anything goes wrong
     */
    public static Object invokeStatic(final Class<?> clazz, final String methodName, final Class<?>[] argTypes,
                                      final Object[] args) throws Exception {
        final Method method = clazz.getMethod(methodName, argTypes);

        return method.invoke(null, args);
    }

}
