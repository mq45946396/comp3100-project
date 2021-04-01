package comp3100;

import java.io.*;
import java.net.*;

public class Connection {

    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;

    public Connection(String host, int port) throws Exception {
        this.socket = new Socket(host, port);
        this.in = this.socket.getInputStream();
        this.out = this.socket.getOutputStream();
    }

    protected boolean performHandshake(String username) throws Exception {
        // send HELO message
        send("HELO");
        // make sure server sends back 'OK'
        if(!read().equals("OK")) {
            return false;
        }
        // send authentication command
        sendf("AUTH %s", username);
        // make sure server sends back 'OK'
        if(!read().equals("OK")) {
            return false;
        }
        // send REDY message
        send("REDY");
        return true;
    }

    protected void end() throws Exception {
        // send quit command
        send("QUIT");

        // close the socket
        in.close();
        out.close();
        socket.close();
    }

    /**
     *	Write the command string to the server connection.
     *	@param str The command to send in the stream.
     */
    protected void send(String str) throws IOException {
        byte[] buf = str.getBytes();
        this.out.write(buf);
        this.out.flush();
    }

    /**
     *  Writes a formatted string to the server connection.
     *  @param str The format string to send in the stream.
     *  @param format The values to use in the format string.
     */
    protected void sendf(String str, Object... format) throws IOException {
        str = String.format(str, format);
        send(str);
    }

    /**
     *	Reads the command string from the server connection.
     *	This method will block until the entire string is received from the server.
     *	@return The command string from the stream.
     */
    protected String read() throws IOException {
        byte[] buf = new byte[1024];
        int nBytes = this.in.read(buf);
        return new String(buf, 0, nBytes);
    }

}