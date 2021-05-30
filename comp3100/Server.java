package comp3100;

public class Server implements Comparable<Server>{
    private static final String INACTIVE = "inactive";
    private static final String BOOTING = "booting";

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

    public String getServerName() {
        return this.type + " " + this.id;
    }

    public int getBootingBias() {
        return this.state.equals(BOOTING) ? 2 : 1;
    }

}
