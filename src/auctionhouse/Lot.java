package auctionhouse;

class Lot extends CatalogueEntry {
    private Money reservePrice;
    private String sellerName;

    public Lot(String sellerName, int lotNumber, String description, LotStatus status, Money reservePrice) {
        super(lotNumber, description, status);
        this.reservePrice = reservePrice;
        this.sellerName = sellerName;
    }

    public int getLotNumber() {
        return lotNumber;
    }

    /**
     * @return the reservePrice
     */
    public Money getReservePrice() {
        return reservePrice;
    }
}