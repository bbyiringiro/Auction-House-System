package auctionhouse;

import java.util.HashMap;

class Auction {
    private Money hammerPrice;
    private Lot lot;
    public HashMap<String, Money> bidderAndBid = new HashMap<String, Money>();
    public Money highestBid;
    public Buyer highestBidder;
    public String auctioneer;

    public Auction(Lot l, String initialBid) {
        this.lot = l;
        this.highestBid= new Money(initialBid);
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
     * @param auctioneer the auctioneer to set
     */
    public void setAuctioneer(String auctioneer) {
        this.auctioneer = auctioneer;
    }

    /**
     * @return the auctioneer
     */
    public String getAuctioneer() {
        return auctioneer;
    }


    /**
     * @return the highestBidder
     */
    public Buyer getHighestBidder() {
        return highestBidder;
    }

    /**
     * @return the highestBid
     */
    public Money getHighestBid() {
        return highestBid;
    }
}