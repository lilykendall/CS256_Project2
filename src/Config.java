import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class Config {

    boolean verbose = false;
    boolean median = false;
    boolean traderInfo = false;

    public Config(String[] args) {

        // creating command line args
        LongOpt[] commandArgs = {
                new LongOpt("verbose", LongOpt.NO_ARGUMENT, null, 'v'),
                new LongOpt("median", LongOpt.NO_ARGUMENT, null, 'm'),
                new LongOpt("trader-info", LongOpt.NO_ARGUMENT, null, 'i')
        };

        Getopt opt = new Getopt("Project 2", args, "vmi", commandArgs);
        opt.setOpterr(true);

        int choice;

        // loop through command line args
        while ((choice = opt.getopt()) != -1) {
            // only sets the variables to be true, the actions of the args will be implemented elsewhere
            switch (choice) {

                case 'v':
                    verbose = true;
                    break;

                case 'm':
                    median = true;
                    break;


                case 'i':
                    traderInfo = true;
                    break;


                default:
                    System.err.println("Error: unknown command line argument");
                    System.exit(1);
            }


        }
    }

    public boolean isVerbose(){
        return verbose;
    }

    public boolean isMedian(){
        return median;
    }

    public boolean isTraderInfo(){
        return traderInfo;

    }
}
