package comp3100;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            String fullServerName = bestServer.type + " " + bestServer.id;
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

        boolean response = false;
        boolean sent = false;
        String input = "";
        String[] moreInput = {};

        while (!response) {
            if (!sent) {
                try {
                    conn.sendf("GETS %s", "All");
                    sent = true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
            }
            try {
                input = conn.read();
                if (input.contains(" ")) {
                    input = input.substring(0, input.indexOf(" "));
                    moreInput = input.substring(input.indexOf(" ")+1).split(" ");
                }

                if (input == "DATA") {
                    conn.send("OK");
                }
                else {
                    Server[] val = new Server[(moreInput.length+1)/9];
                    val[0].type = input;
                    val[0].id = Integer.valueOf(moreInput[0]);
                    val[0].state = moreInput[1];
                    val[0].curStartTime = Integer.valueOf(moreInput[2]);
                    val[0].core = Integer.valueOf(moreInput[3]);
                    val[0].mem = Integer.valueOf(moreInput[4]);
                    val[0].disk = Integer.valueOf(moreInput[5]);
                    if (val.length > 8) {
                        int j = 1;
                        for (int i = 7; i <= val.length-3; i+=9) {
                            val[j].type = input;
                            val[j].id = Integer.valueOf(moreInput[0]);
                            val[j].state = moreInput[1];
                            val[j].curStartTime = Integer.valueOf(moreInput[2]);
                            val[j].core = Integer.valueOf(moreInput[3]);
                            val[j].mem = Integer.valueOf(moreInput[4]);
                            val[j].disk = Integer.valueOf(moreInput[5]);
                            j+=1;
                        }
                    }
                    return val;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
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
        List<Server> filtered = Arrays.asList(servers);
        filtered = Arrays.asList(filtered.stream().filter(s -> s.core >= jobCores).toArray(Server[]::new));
        filtered = Arrays.asList(filtered.stream().filter(s -> s.mem >= jobMemory).toArray(Server[]::new));
        filtered = Arrays.asList(filtered.stream().filter(s -> s.disk >= jobDisk).toArray(Server[]::new));
        if (!filtered.isEmpty()) {
            Collections.sort(filtered);
            Server val = filtered.get(0);
            if (!val.state.equals("inactive") && !val.state.equals("idle")) {
                for (int i = 0; i < filtered.size(); i++) {
                    if (val.state.equals("inactive") && val.state.equals("idle")) {
                        val = filtered.get(i);
                        return val;
                    }
                }
            }
            return null;
        }
        else {
            return null;
        }
    }
    
}