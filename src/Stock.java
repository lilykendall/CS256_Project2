import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Stock {

    // stores the buy orders and sell orders in their respective PQ
    private PriorityQueue<Order> buyOrders;
    private PriorityQueue<Order> sellOrders;

    // if this is true, will print verbose statement out while matching
    private boolean isVerbose = false;

    // keeps track of the number of matches for the end of day statement
    private int matches = 0;

    // PQs for keeping track of and updating the median
    private PriorityQueue<Integer> topPrices = new PriorityQueue<>();
    private PriorityQueue<Integer> bottomPrices = new PriorityQueue<>(Collections.reverseOrder());

    // stores the actual medain
    private int median;

    // stores the number of transactions made
    private int numTransactions;

    // constructor
    public Stock(){
        buyOrders = new PriorityQueue<>();
        sellOrders = new PriorityQueue<>();
    }

    public int getNumTransactions(){
        return numTransactions;
    }

    public int getMedian(){
        return median;
    }


    // adds an order to the PQs
    public void addOrder(Order o){
        if(o.isSell()){
            sellOrders.add(o);
        }
        else if(!o.isSell()){
            buyOrders.add(o);
        }

    }

    // sets verbose to true
    public void setVerbose(){
        isVerbose = true;
    }



    // matches up stocks and performs transactions
    public void performMatches(List<Trader> traders){
        // matches is reset to 0 everytime perform matches is called because the total matches
        // is added to the tally in the simulation everytime perform matches is done
        matches = 0;

        // while there is a match between a sell order and buy order
        while(canMatch()){

            // get the the sell order and buy order
            Order buy = buyOrders.peek();
            Order sell = sellOrders.peek();

            // increment matches and transactions
            matches++;
            numTransactions++;

            // stores the price the stock is being sold for
            int price;

            // if the buy order came in before the sell order, use the buy orders price
            if(buy.getId() < sell.getId()){
                price = buy.getPrice();
            }
            // if not, use the sell orders price
            else{
                price = sell.getPrice();
            }

            // add the price to the median count and re-update the median
            updateMedian(price);

            // the amount of shares bought is the lowest quantity out of the two orders
            int shares = Math.min(buy.getQuantity(), sell.getQuantity());

            // perform the transaction
            buy.removeShares(shares);
            sell.removeShares(shares);

            // remove the order if one of the orders is out of stocks to buy/sell
            if(buy.getQuantity() == 0) {
                buyOrders.remove();
            }

            if(sell.getQuantity() == 0){
                sellOrders.remove();
            }

            // if verbose is on, print out the verbose statement
            if(isVerbose){
                System.out.println("Trader " + buy.getTraderID() +
                        " purchased " + shares +
                        " shares of Stock " + sell.getStockID() +
                        " from Trader " + sell.getTraderID() +
                        " for $" + price + "/share" );
            }

            // update trader info
            traders.get(buy.getTraderID()).buy(shares, price);
            traders.get(sell.getTraderID()).sell(shares, price);

        }

    }

    public void updateMedian(int price){
        // if the two PQS are empty, add to the top half and the median is automatically the first order
        if(topPrices.isEmpty() && bottomPrices.isEmpty()){
            median = price;
            topPrices.add(price);
        }

        // if the price is greater than the median, add to the top half
        else{
            if(price > median){
                topPrices.add(price);
            }
            // if the price is less than the median, add to the bottom half
            else{
                bottomPrices.add(price);
            }

            // check to see if one of the PQs has 2 more than the other
            if(bottomPrices.size() - topPrices.size() == 2){
                topPrices.add(bottomPrices.remove());
            }
            if(topPrices.size() - bottomPrices.size() == 2){
                bottomPrices.add(topPrices.remove());
            }

            // whichever PQ has more elements will have the median
            if(bottomPrices.size() > topPrices.size()){
                median = bottomPrices.element();
            }
            else if(topPrices.size() > bottomPrices.size()){
                median = topPrices.element();
            }
            // if they have the same amt of elements
            else{
                median = (bottomPrices.element() + topPrices.element()) / 2;
            }
        }
    }

    private boolean canMatch(){
        // check if two orders can be matched at the head of the PQs
        if(buyOrders.isEmpty() || sellOrders.isEmpty()){
            // we cannot match
            return false;
        }

        return sellOrders.peek().getPrice() <= buyOrders.peek().getPrice();
    }

    public int getMatches(){
        return matches;
    }

}
