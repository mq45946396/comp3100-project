package comp3100;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Scheduler {

    private static Server allTimeLargest = null;

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
            String fullServerName = bestServer.type + " " + bestServer.id;
            conn.sendf("SCHD %d %s", jobID, fullServerName);
            if(!conn.read().equals("OK")) {
                throw new RuntimeException("[Job " + jobID + "] Scheduling failed!");
            }
            System.out.printf("[Job %d] Scheduled on %s (t = %d)\n", jobID, fullServerName, submitTime);
        } else {
            System.out.printf("[Job %d] Could not find server to schedule!\n", jobID);
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
        try {
            // LUCAS: send GETS command
            conn.send("GETS All");

            // LUCAS: read and parse data response
            String[] data = conn.read().split(" ");
            int numServers = Integer.parseInt(data[1]);
            int maxLength  = numServers * Integer.parseInt(data[2]);
            conn.send("OK");

            Server[] servers = new Server[numServers];

            // LUCAS: read server info and store the input servers
            String[] info = conn.read(maxLength).split("\n");
            for (int i = 0; i < numServers; i++) {
                // LUCAS: parse data
                data = info[i].split(" ");
                String type = data[0];
                int id = Integer.parseInt(data[1]);
                String state = data[2];
                int cores = Integer.parseInt(data[4]);
                int mem = Integer.parseInt(data[5]);
                int disk = Integer.parseInt(data[6]);
                // LUCAS: initialize server class to store the input server
                servers[i] = new Server(type, id, cores, mem, disk, 0, 0, 0, state, 0);
            }

            // LUCAS: confirm data received and read the final '.' from the server
            conn.send("OK");
            conn.read();
            return servers;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chooses the most appropriate server for the given job (allToLargest). It should skip any servers
     * which do not meet the minimum requirements, and then return the server with the highest
     * core count.
     * @param servers The list of servers to choose from.
     * @param jobCores The number of cores required by the job.
     * @param jobMemory The amount of memory required by the job.
     * @param jobDisk The amount of disk usage required by the job.
     * @return The best available server to schedule the job on, or null if none can be found.
     */
    private static Server pickBestServer(Server[] servers, int jobCores, int jobMemory, int jobDisk) {
        // LUCAS: if we already know the largest server, skip the process of figuring out which server
        if(allTimeLargest != null) {
            return allTimeLargest;
        }

        // LUCAS: create a list of capable servers by filtering out the incapable and unavailable servers
        List<Server> filtered = Arrays.asList(servers);
        filtered = Arrays.asList(filtered.stream().filter(s -> s.core >= jobCores).toArray(Server[]::new));
        filtered = Arrays.asList(filtered.stream().filter(s -> s.mem >= jobMemory).toArray(Server[]::new));
        filtered = Arrays.asList(filtered.stream().filter(s -> s.disk >= jobDisk).toArray(Server[]::new));
        filtered = Arrays.asList(filtered.stream().filter(s -> !s.state.equals("unavailable")).toArray(Server[]::new));
        // LUCAS: sort the filtered list so that the biggest server is at the start of the list
        Collections.sort(filtered);
        if (!filtered.isEmpty()) {
            allTimeLargest = filtered.get(0);
            return filtered.get(0);
        }
        else {
            return null;
        }
    }

}
