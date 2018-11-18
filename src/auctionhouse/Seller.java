package auctionhouse;

class Seller extends User{
    private String bankAccount;
    private String address;

    public Seller(String name, String account, String address){
        super(name);
        this.bankAccount=account;
        this.address=address;
    }

    public String getAccount(){
        return this.bankAccount;
    }

    public String getAddress(){
        return this.address;
    }
}