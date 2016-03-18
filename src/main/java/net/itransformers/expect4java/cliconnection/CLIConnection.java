/*
 * CLIConnection.java
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

package net.itransformers.expect4java.cliconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface CLIConnection {
    /**
     * Establishes a session connection to a network host and creates and stores
     * session parameters.
     * @param params Parameters used to connect connection to the host
     * @throws IOException if any IOException when
     * communication with the NE.
     */
    void connect(Map<String, Object> params) throws IOException;
    /**
     * Disconnects the session to the host.
     * @throws IOException if any IOException when
     * communication with the host
     */
    void disconnect() throws IOException;

    InputStream inputStream();

    OutputStream outputStream();

}
