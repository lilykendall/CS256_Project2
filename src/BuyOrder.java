public class BuyOrder extends Order {

    //constructor
    public BuyOrder(long l, long ts, int traderID, int stockID, int price, int quantity) {
        super(l,ts,traderID,stockID,price,quantity);
    }

    @Override
    public boolean isSell() {
        return false;
    }

    @Override
    public int compareTo(Order o) {
        if(this.getPrice() == o.getPrice()){
            // the order that came first gets priority
            // smaller id means came earlier
            return (int)(this.getId() - o.getId());

        }
        return o.getPrice() - this.getPrice();
    }
}
