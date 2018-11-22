package auctionhouse;

class Buyer extends User{
    private String bankAccount;
    private String bankAuthCode;
    private String address;

    public Buyer(String name, String account, String authCode, String address){
        super(name);
        this.bankAccount=account;
        this.bankAuthCode=authCode;
        this.address=address;
    }

    public String getAccount(){
        return this.bankAccount;
    }

    public String getAddress(){
        return this.address;
    }

    public String getBankAuthCode(){
        return this.bankAuthCode;
    }
}