package comp3100;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

public class Scheduler {

    private static final HashMap<String, Integer> totalCores = new HashMap<String, Integer>();
    private static Server allTimeLargest = null;

    public static void scheduleJob(Connection conn, String[] args, boolean verbose) throws Exception {
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
            String fullServerName = bestServer.getServerName();
            conn.sendf("SCHD %d %s", jobID, fullServerName);
            String resp = conn.read();
            if(!resp.equals("OK")) {
                if(verbose) {
                    String schd = String.format("SCHD %d %s", jobID, fullServerName);
                    System.err.printf("[SERVER] %s\n\tSchedule command: %s\n\tJob details: c = %d m = %d d = %d\n\n", resp, schd, cores, memory, disk);
                }
                throw new RuntimeException("[Job " + jobID + "] Scheduling failed!");
            }
            if(verbose) System.out.printf("[Job %d] Scheduled on %s (t = %d, jc = %d, sc = %d)\n", jobID, fullServerName, submitTime, cores, bestServer.core);
        } else {
            if(verbose) System.out.printf("[Job %d] Could not find server to schedule!\n", jobID);
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
     * Chooses the most appropriate server for the given job.
     * @param servers The list of servers to choose from.
     * @param jobCores The number of cores required by the job.
     * @param jobMemory The amount of memory required by the job.
     * @param jobDisk The amount of disk usage required by the job.
     * @return The best available server to schedule the job on, or null if none can be found.
     */
    private static Server pickBestServer(Server[] servers, int jobCores, int jobMemory, int jobDisk) {
        // populate the map of total cores before scheduling any jobs and
        // find the largest server
        if(totalCores.isEmpty()) {
            for(Server s : servers) {
                totalCores.put(s.getServerName(), s.core);
                if(allTimeLargest == null || s.core > allTimeLargest.core) {
                    allTimeLargest = s;
                }
            }
        }

        // remove any servers which do not meet the memory or disk requirement
        List<Server> serverList = Arrays.asList(servers);
        serverList = serverList.stream().filter((s) -> {
            return s.mem >= jobMemory && s.disk >= jobDisk;
        }).collect(Collectors.toList());

        // if no servers match criteria just schedule to largest
        if(serverList.isEmpty()) return allTimeLargest;

        // sort the servers by the difference of job cores and available cores (ascending)
        // also creates a bias for the difference if the server is currently booting
        Collections.sort(serverList, (a, b) -> {
            int diffA = Math.abs(a.core - jobCores) / a.getBootingBias();
            int diffB = Math.abs(b.core - jobCores) / b.getBootingBias();
            return diffA - diffB;
        });

        // check if server has enough cores
        if(serverList.get(0).core >= jobCores) {
            return serverList.get(0);
        }

        // otherwise allocate it to the largest overall server by sorting by
        // core count (descending order)
        Collections.sort(serverList, (a, b) -> {
            return totalCores.get(b.getServerName()) - totalCores.get(a.getServerName());
        });
        return serverList.get(0);
    }

}
