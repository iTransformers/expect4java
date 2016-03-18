/*
 * Expect4j.java
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

package net.itransformers.expect4java;


import net.itransformers.expect4java.matches.Match;
import net.itransformers.expect4java.matches.TimeoutMatch;

import java.io.*;

public interface Expect4j {
    void send(String str) throws IOException;
    int expect(Match match);
    int expect(Match[] matches);
    void setTimeout(TimeoutMatch timeoutMatch);
    void close() throws IOException;
    Reader getReader();
    Writer getWriter();
}
