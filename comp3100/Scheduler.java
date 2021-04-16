package comp3100;

public class Scheduler {

    public static void scheduleJob(Connection conn, String[] args) throws Exception {
        for(int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }

    @SuppressWarnings("unused")
    private static Server[] getAllServers(Connection conn) {
        /**
         *  For Lucas to complete only
         *
         *  To ensure there's no possibility of merge conflicts, don't edit this method
         *  unless you're Lucas and on Lucas' branch.
         */
        return null;
    }

    @SuppressWarnings("unused")
    private static Server pickBestServer(Server[] servers, int jobCores) {
        /**
         *  For Lucas to complete only
         *
         *  To ensure there's no possibility of merge conflicts, don't edit this method
         *  unless you're Lucas and on Lucas' branch.
         */
        return null;
    }
    
}