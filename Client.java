import java.io.*;
import java.net.*;

/**
 *    DS-CLIENT IMPLEMENTATION
 *    COMP3100, March 2021
 *
 *    by Jack Davenport (45946396)
 *       Lucas Turnbull (45947155)
 *       Elliot Holt    (45955964)
 */
public class Client {

    static final String HOST = "localhost"; // the host address of the server
    static final int    PORT = 50000;       // the TCP port of the server
    static final String USER = "dsclient";  // the user name used for authentication

    public static void main(String[] args) throws Exception {

        // open socket
	Socket socket = new Socket(HOST, PORT);
	InputStream in = socket.getInputStream();
	OutputStream out = socket.getOutputStream();

	// begin handshake
	send("HELO", out);

	// TODO

	System.out.println(read(in));
	socket.close();

    }

    /**
     *	Write the command string into the given OutputStream.
     *	@param str The command to send in the stream.
     *	@param out The target stream to write the command to.
     */
    static void send(String str, OutputStream out) throws IOException {
	byte[] buf = str.getBytes();
	out.write(buf);
	out.flush();
    }

    /**
     *	Reads the command string from the given InputStream.
     *	This method will block until the entire string is received from the server.
     *	@param in The stream to read the command string from.
     *	@return The command string from the stream.
     */
    static String read(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
	int nBytes = in.read(buf);
	return new String(buf, 0, nBytes);
    }

}
