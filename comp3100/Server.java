package comp3100;

import java.io.*;
import java.net.*;

// LUCAS: Added the implements Comparable bit for scheduler.java
public class Server implements Comparable<Server>{
    private static final int INACTIVE = 0;
    private static final int BOOTING = 1;
    private static final int IDLE = 2;
    private static final int ACTIVE = 3;
    private static final int UNAVAILABLE = 4;

    public String type = "";
    public int id = 0;
    public int core = 1, mem = 1, disk = 1;
    public int limit = 1;
    public int bootupTime = 0;
    public float hourlyRate = 0;
    
    // TODO: add these fields: state, curStartTime
    public int state = INACTIVE;
    public int curStartTime = 0;

    // TODO: add constructor to set these fields
    public Server(String type, int id, int core, int mem, int disk, int limit, 
    int bootupTime, float hourlyRate, int state, int curStartTime) {
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
    // LUCAS: Doesn't look pretty, but she'll be right
    @Override
    public int compareTo(Server s) {
        // sorting in descending order so that it doesn't matter how long the server array is
        return this.core-s.core + this.mem-this.mem + this.disk-this.disk;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Client.PORT);
            Socket clientSocket = serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            boolean quit = false;
            while(!quit) {
                String input = in.readLine();
                String output = "";

                if (input.equals("HELO") || input.equals("AUTH " + Client.USER)) {
                    out.write("OK");
                    output = "OK";
                }
                if (input.equals("REDY")) {
                    quit = true;
                }
                System.out.println("I receive: " + input);
                System.out.println("You receive: " + output);
            }

            out.close();
            serverSocket.close();
            clientSocket.close();
        } catch (Exception e){System.out.println(e);}
    }

}