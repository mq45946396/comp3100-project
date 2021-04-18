package comp3100;

public class Server implements Comparable<Server>{
    private static final String INACTIVE = "inactive";

    public String type = "";
    public int id = 0;
    public int core = 1, mem = 1, disk = 1;
    public int limit = 1;
    public int bootupTime = 0;
    public float hourlyRate = 0;
    
    public String state = INACTIVE;
    public int curStartTime = 0;

    public Server(String type, int id, int core, int mem, int disk, int limit, 
    int bootupTime, float hourlyRate, String state, int curStartTime) {
        this.type = type;
        this.id = id;
        this.core = core;
        this.mem = mem;
        this.disk = disk;
        this.limit = limit;
        this.bootupTime = bootupTime;
        this.hourlyRate = hourlyRate;
        this.state = state;
        this.curStartTime = curStartTime;
    }

    // LUCAS: added an override for compareTo() for finding the best server in scheduler.java
    @Override
    public int compareTo(Server s) {
        // sorting in descending order so that it doesn't matter how long the server array is
        return s.core-this.core;
    }

    // /* this method isn't needed */
    // @Deprecated
    // public static void main(String[] args) {
    //     try {
    //         ServerSocket serverSocket = new ServerSocket(Client.PORT);
    //         Socket clientSocket = serverSocket.accept();

    //         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    //         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

    //         boolean quit = false;
    //         while(!quit) {
    //             String input = in.readLine();
    //             String output = "";

    //             if (input.equals("HELO") || input.equals("AUTH " + Client.USER)) {
    //                 out.write("OK");
    //                 output = "OK";
    //             }
    //             if (input.equals("REDY")) {
    //                 quit = true;
    //             }
    //             System.out.println("I receive: " + input);
    //             System.out.println("You receive: " + output);
    //         }

    //         out.close();
    //         serverSocket.close();
    //         clientSocket.close();
    //     } catch (Exception e){System.out.println(e);}
    // }

}