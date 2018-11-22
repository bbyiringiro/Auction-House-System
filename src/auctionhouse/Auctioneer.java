package auctionhouse;

class Auctioneer extends User {
    private String address;

    public Auctioneer(String name) {
        super(name);
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
}