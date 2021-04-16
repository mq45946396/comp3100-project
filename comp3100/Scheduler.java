package comp3100;

public class Scheduler {

    public static void scheduleJob(Connection conn, String[] args) throws Exception {
        // decode job parameters
        int submitTime = Integer.parseInt(args[0]);
        int jobID = Integer.parseInt(args[1]);
        // int estRuntime = Integer.parseInt(args[2]);
        int cores = Integer.parseInt(args[3]);
        int memory = Integer.parseInt(args[4]);
        int disk = Integer.parseInt(args[5]);

        // get list of servers and their current status
        Server[] servers = getAllServers(conn);

        // pick the best server based on availability and core count
        Server bestServer = pickBestServer(servers, cores, memory, disk);

        // if a server is picked, send back schedule command
        if(bestServer != null) {
            String fullServerName = bestServer.serverType + " " + bestServer.serverID;
            conn.sendf("SCHD %d %s", jobID, fullServerName);
            System.out.printf("[Job %d] Scheduled on %s (t = $d)\n", jobID, fullServerName, submitTime);
        } else {
            System.out.printf("[Job %d] Could not find server to schedule!", jobID);
        }

        // indicate that we are ready for more work
        conn.send("REDY");
    }

    /**
     * Gets all servers and their current status from the server connection.
     * Interally uses the 'GETS All' command to read information from the simulator.
     * @param conn The connection to read from.
     * @return Array of servers with their data.
     */
    private static Server[] getAllServers(Connection conn) {
        /**
         *  For Lucas to complete only
         *
         *  To ensure there's no possibility of merge conflicts, don't edit this method
         *  unless you're Lucas and on Lucas' branch.
         */
        return null;
    }

    /**
     * Chooses the most appropriate server for the given job. It should skip any servers
     * which do not meet the minimum requirements, and then return the server with the highest
     * core count.
     * @param servers The list of servers to choose from.
     * @param jobCores The number of cores required by the job.
     * @param jobMemory The amount of memory required by the job.
     * @param jobDisk The amount of disk usage required by the job.
     * @return The best available server to schedule the job on, or null if none can be found.
     */
    private static Server pickBestServer(Server[] servers, int jobCores, int jobMemory, int jobDisk) {
        /**
         *  For Lucas to complete only
         *
         *  To ensure there's no possibility of merge conflicts, don't edit this method
         *  unless you're Lucas and on Lucas' branch.
         */
        return null;
    }
    
}