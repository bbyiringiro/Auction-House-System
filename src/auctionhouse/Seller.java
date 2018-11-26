package auctionhouse;

class Seller extends User{
    private String bankAccount;

    public Seller(String name, String account, String address){
        super(name, address);
        this.bankAccount=account;
    }

    public String getAccount(){
        return this.bankAccount;
    }

}