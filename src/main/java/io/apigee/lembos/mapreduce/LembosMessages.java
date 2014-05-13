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

/**
 * Class containing the shared messages.
 */
public final class LembosMessages {

    public static final String CLASS_NOT_FOUND = "Class not found";
    public static final String FIRST_ARG_MUST_BE_BOOL = "First argument must be a boolean";
    public static final String FIRST_ARG_MUST_BE_ARRAY = "First argument must be an array";
    public static final String FIRST_ARG_MUST_BE_BOOL_OR_CONF =
            "First argument must be a boolean or a Configuration object";
    public static final String FIRST_ARG_MUST_BE_CONF = "First argument must be a Configuration object";
    public static final String FIRST_ARG_MUST_BE_COUNTER_GROUP = "First argument must be a CounterGroup object";
    public static final String FIRST_ARG_MUST_BE_COUNTERS = "First argument must be a Counters object";
    public static final String FIRST_ARG_MUST_BE_JOB = "First argument must be a Job object";
    public static final String FIRST_ARG_MUST_BE_NUM = "First argument must be a number";
    public static final String FIRST_ARG_REQUIRED = "First argument is not optional";
    public static final String FIFTH_ARG_REQUIRED = "Fifth argument is not optional";
    public static final String FOURTH_ARG_REQUIRED = "Fourth argument is not optional";
    public static final String ONE_ARG_EXPECTED = "One argument expected";
    public static final String ONE_OR_TWO_ARGS_EXPECTED = "One or two arguments expected";
    public static final String FOUR_OR_SIX_ARGS_EXPECTED = "Four or six arguments expected";
    public static final String SECOND_ARG_ARG_MUST_BE_NUM = "Second argument must be a number";
    public static final String SECOND_ARG_MUST_BE_ARRAY = "Second argument must be an array";
    public static final String SECOND_ARG_MUST_BE_BOOL = "Second argument must be a boolean";
    public static final String SECOND_ARG_MUST_BE_CONF = "Second argument must be a Configuration object";
    public static final String SECOND_ARG_REQUIRED = "Second argument is not optional";
    public static final String SIXTH_ARG_REQUIRED = "Sixth argument is not optional";
    public static final String SIXTH_ARG_MUST_BE_ARRAY = "Sixth argument must be an array";
    public static final String THIRD_ARG_REQUIRED = "Third argument is not optional";
    public static final String THIRD_ARG_MUST_BE_ARR_OR_NUM = "Third argument must be an array or a number";
    public static final String THREE_ARGS_EXPECTED = "Three arguments expected";
    public static final String TWO_ARGS_EXPECTED = "Two arguments expected";
    public static final String ZERO_OR_ONE_ARG_EXPECTED = "Zero or one argument expected";
    public static final String ZERO_ONE_OR_TWO_ARGS_EXPECTED = "Zero, one or two arguments expected";

    /**
     * Private constructor.
     */
    private LembosMessages() { }

    /**
     * Helper to create a common error message for invalid class types.
     *
     * @param expected expected class type
     * @param actual actual class type
     *
     * @return common error message
     */
    public static String makeInvalidClassErrorMessage(final Class<?> expected, final Class<?> actual) {
        return actual.getCanonicalName() + " is not a valid " + expected.getCanonicalName();
    }

}
