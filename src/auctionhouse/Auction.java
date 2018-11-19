package auctionhouse;

class Auction {
    private Money hammerPrice;
    private Lot lot;
    private String time;
    public Hashmap<String, Money> bidderAndBid = new Hashmap<>();
    public Money highestBid;

    public Auction(Lot l) {
        this.lot = l;
    }

    /**
     * @return the hammerPrice
     */
    public Money getHammerPrice() {
        return hammerPrice;
    }

    public void setHammerPrice() {
        this.hammerPrice = this.highestBid;
    }

    /**
     * @return the lot
     */
    public Lot getLot() {
        return lot;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }
}