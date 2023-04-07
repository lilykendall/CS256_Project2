import java.util.ArrayList;
import java.util.Scanner;

public class StockMarketSimulation {

    private Config config;

    private ArrayList<Stock> stocks;

    private ArrayList<Trader> traders;

    private Scanner in;

    private long currId = 0;
    public StockMarketSimulation(Config c) {
        config = c;

        // read in an initial configuration
        in = new Scanner(System.in);

        // COMMENT: <COMMENT>
        // MODE: <INPUT_MODE>
        // NUM_TRADERS: <NUM_TRADERS>
        // NUM_STOCKS: <NUM_STOCKS>

        //skip over the comment
        in.nextLine();

        // throw away the header
        in.next();

        // TL or PR mode
        String mode = in.next();

        in.next();
        // get the number of traders from the top of the file
        int numTraders = in.nextInt();

        in.next();
        // get the number of stocks from the top of the file
        int numStocks = in.nextInt();

        // construct ALs with the correct capacity
        traders = new ArrayList<>(numTraders);
        // adding traders to the traders AL
        for(int i = 0; i < numTraders; i++){
            traders.add(new Trader(i));
        }

        stocks = new ArrayList<>(numStocks);
        // adding empty stocks to the stocks AL
        for(int i = 0; i < numStocks; i++){
            stocks.add(new Stock());

            // if verbose is in the command line, set verbose mode for the stock
            if(config.isVerbose()){
                stocks.get(i).setVerbose();
            }
        }

        //check for PR mode
        if(mode.equals("PR")){
            //RANDOM_SEED: <SEED>
            //NUMBER_OF_ORDERS: <NUM_ORDERS>
            //ARRIVAL_RATE: <ARRIVAL_RATE>

            in.next();
            int seed = in.nextInt();

            in.next();
            int numOrder = in.nextInt();

            in.next();
            int arrivalRate = in.nextInt();

            in = P2Random.PRInit(seed, numTraders, numStocks, numOrder, arrivalRate);

        }
    }

    public void simulate(){
        // main processing loop for simulation

        long currTime = 0;
        int totalMatches = 0;

        System.out.println("Processing orders...");

        while(in.hasNextLong()){
            Order nextOrder = getNextOrder();

            // if the timestamp is negative or less than the current timestamp, it is an invalid timestamp
            if(nextOrder.getTimestamp() < 0 || nextOrder.getTimestamp() < currTime){
                System.err.println("Error: invalid timestamp");
                System.exit(1);
            }

            // if the next order is above the current time
            if(nextOrder.getTimestamp() > currTime){
                // do anything that needs to be done when the timestamp changes

                // print out median mode if the flag is set
                if(config.isMedian()){
                    for(int i = 0; i< stocks.size(); i++){
                        //i is the stock ID
                        Stock s = stocks.get(i);
                        if(s.getNumTransactions() > 0){
                            int median = s.getMedian();
                            System.out.println("Median match price of Stock " +
                                    i + " at time " + currTime + " is $" + median);
                        }
                    }
                }

                // set the current time to the next order's timestamp
                currTime = nextOrder.getTimestamp();
            }

            Stock s = stocks.get(nextOrder.getStockID());
            // add the order to that particular stock
            s.addOrder(nextOrder);
            // check and match with other orders in that stock
            s.performMatches(traders);
            totalMatches += s.getMatches();


        }

        //since the timestamp will not be higher than the curr time stamp in the last time stamp,
        // the median will need to be printed again after we read through all the lines.
        if(config.isMedian()) {
            for (int i = 0; i < stocks.size(); i++) {
                //i is the stock ID
                Stock s = stocks.get(i);
                if (s.getNumTransactions() > 0) {
                    int median = s.getMedian();
                    System.out.println("Median match price of Stock " +
                            i + " at time " + currTime + " is $" + median);
                }
            }
        }

        // end of day
        System.out.println("---End of Day---");
        System.out.println("Orders Processed: " + totalMatches);

        //output the trader info
        if(config.isTraderInfo()){
            System.out.println("---Trader Info---");
            for(Trader t: traders){
                System.out.println("Trader " + t.getTraderID() + " bought " + t.getStocksBought() +
                         " and sold " + t.getStocksSold() + " for a net transfer of $" + t.getNetSales());
            }

        }



    }

    private Order getNextOrder(){
        // scans through a line in the file and returns a new order
        // <TIMESTAMP> <BUY/SELL> T<TRADER_ID> S<STOCK_NUM> $<PRICE> #<QUANTITY>

        long ts = in.nextLong();
        String intent = in.next();
        int traderID = Integer.parseInt(in.next().substring(1));
        int stockID = Integer.parseInt(in.next().substring(1));
        int price = Integer.parseInt(in.next().substring(1));
        int quantity = Integer.parseInt(in.next().substring(1));

        // check if the traderId is negative or greater than the amount of trader IDs listed
        if(traderID < 0 || traderID >= traders.size()){
            System.err.println("Error: trader ID not in range");
            System.exit(1);
        }

        // check if the stockId is negative or greater than the amount of stock IDs listed
        if(stockID < 0 || stockID >= stocks.size()){
            System.err.println("Error: stock ID not in range");
            System.exit(1);
        }

        // check if the quantity or price is negative
        if(quantity <= 0 || price <= 0){
            System.err.println("Error: price and/or quantity must be positive non-zero integers");
            System.exit(1);
        }

        // create a sell order or buy order based on what the line states
        if(intent.equals("SELL")){
            return new SellOrder(currId++, ts, traderID, stockID, price, quantity);
        }
        else{
            // buy order
            return new BuyOrder(currId++, ts, traderID, stockID, price, quantity);
        }

    }
}
