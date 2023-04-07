
public class Trader {

    // variables
    private int traderID;
    private int stocksBought;
    private int stocksSold;
    private int netSales;

    // constructor
    public Trader(int iD){
        traderID = iD;
        stocksBought = 0;
        stocksSold = 0;
        netSales = 0;

    }

    // updates a trader's net sales and stocks bought when a trader makes a purchase
    public void buy(int shares, int price) {
        stocksBought += shares;
        netSales -= (price * shares);

    }

    // updates the amount of stocks a trader has sold and net sales when a trader sells stocks
    public void sell(int shares, int price) {
        //TODO implement
        stocksSold += shares;
        netSales += (price * shares);
    }

    // get methods that retrieve trader variables
    public int getTraderID() {
        return traderID;
    }

    public int getNetSales() {
        return netSales;
    }

    public int getStocksBought() {
        return stocksBought;
    }

    public int getStocksSold(){
        return stocksSold;
    }
}
