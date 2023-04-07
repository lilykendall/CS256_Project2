public class Main {

    public static void main(String[] args) {
        Config c = new Config(args);

        // create a new stock market
        StockMarketSimulation s = new StockMarketSimulation(c);

        // simulate the stock market
        s.simulate();
    }

}
