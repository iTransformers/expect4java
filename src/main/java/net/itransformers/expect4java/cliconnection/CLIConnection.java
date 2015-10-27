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
