package comp3100;

/**
 *    DS-CLIENT IMPLEMENTATION
 *    COMP3100, April 2021
 *
 *    by Jack Davenport (45946396)
 *       Lucas Turnbull (45947155)
 *       Elliot Holt    (45955964)
 */
public class Client {

    static final String HOST = "localhost"; // the host address of the server
    static final int    PORT = 50000;       // the TCP port of the server
//  static final String USER = "dsclient";  // the user name used for authentication

    public static void main(String[] args) throws Exception {

        // open connection to server
        Connection conn = new Connection(HOST, PORT, isNewlineMode(args));
        
        // perform handshake and authentication
        String user = System.getProperty("user.name");
        if(!conn.performHandshake(user)) {
            conn.end();
            throw new RuntimeException("Server handshake failed!");
        }

        // loop until server is finished
        boolean quit = false;
        while(!quit) {
            // parse command
            String cmd = conn.read();
            String[] params = {};
            if(cmd.contains(" ")) {
                params = cmd.substring(cmd.indexOf(" ")+1).split(" ");
                cmd = cmd.substring(0, cmd.indexOf(" "));
            }
            // choose behaviour based on command
            switch(cmd) { 
                case "NONE":
                    quit = true;
                    break;
                case "JOBN":
                    Scheduler.scheduleJob(conn, params);
                    break;
                case "JCPL":
                    conn.send("REDY");
                    break;
                default:
                    conn.sendf("ERR Unknown command: %s", cmd);
                    break;
            }
        }

        // close connection
        conn.end();
        
    }

    private static boolean isNewlineMode(String[] args) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-n")) {
                return true;
            }
        }
        return false;
    }

}
