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
        // load command line arguments and print help if requested
        Args arg = new Args(args);
        if(arg.help) {
            System.out.println("Usage: java [..vm] comp3100.Client [-n|-v|-h]");
            System.out.println();
            System.out.println("\t-n : use newline mode");
            System.out.println("\t-v : use verbose mode (print when job is scheduled)");
            System.out.println("\t-h : show this help message");
            return;
        }

        // open connection to server
        Connection conn = new Connection(HOST, PORT, arg.newline);
        
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
                    Scheduler.scheduleJob(conn, params, arg.verbose);
                    break;
                case "JCPL":
                    if(arg.verbose) {
                        int time  = Integer.parseInt(params[0]);
                        int jobID = Integer.parseInt(params[1]);
                        String sn = params[2] + " " + params[3];
                        System.out.printf("[Job %d] Completed running job (t = %d, s = %s)\n", jobID, time, sn); 
                    }
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

    static class Args {

        public boolean newline;
        public boolean verbose;
        public boolean help;

        private Args(String[] args) {
            for(String arg : args) {
                switch(arg.toLowerCase()) {
                    case "-n":
                        this.newline = true;
                        break;
                    case "-v":
                        this.verbose = true;
                        break;
                    case "-h":
                        this.help = true;
                        break;
                    default:
                        break;
                }
            }
        }

    }

}
