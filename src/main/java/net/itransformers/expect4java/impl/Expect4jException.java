/*
 * Expect4jException.java
 *
 * Copyright 2016  iTransformers Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.itransformers.expect4java.impl;

/**
 * Created by vasko on 27.10.15.
 */
public class Expect4jException  extends Exception{
    public Expect4jException() {
    }

    public Expect4jException(String message) {
        super(message);
    }

    public Expect4jException(String message, Throwable cause) {
        super(message, cause);
    }

    public Expect4jException(Throwable cause) {
        super(cause);
    }

    public Expect4jException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
