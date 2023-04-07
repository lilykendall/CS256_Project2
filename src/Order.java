public abstract class Order implements Comparable<Order>{

    private long timestamp;
    private int traderID;
    private int stockID;
    private boolean isSell;
    private int price;
    private int quantity;
    private long id;

    //constructor
    public Order(long id, long ts, int tId, int sId,int p ,int q){
        // unique number to describe the order
        this.id = id;
        timestamp = ts;
        traderID = tId;
        stockID = sId;
        price = p;
        quantity = q;
    }

    // to be implemented
    public abstract boolean isSell();

    // needs to be implemented in sell and buy order
    @Override
    public abstract int compareTo(Order o);

    // removes shares from the quantity
    public void removeShares(int shares){
        this.quantity -= shares;

    }

    public long getTimestamp(){ return timestamp;}
    public int getTraderID(){return traderID;}
    public int getStockID(){return stockID;}
    public int getPrice(){return price;}
    public int getQuantity(){return quantity;}
    public long getId(){ return id;}

}
