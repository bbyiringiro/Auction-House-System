package auctionhouse;

class Buyer extends User {
    private String bankAccount;
    private String bankAuthCode;

    public Buyer(String name, String account, String authCode, String address) {
        super(name, address);
        this.bankAccount = account;
        this.bankAuthCode = authCode;
    }

    public String getAccount() {
        return this.bankAccount;
    }

    public String getBankAuthCode() {
        return this.bankAuthCode;
    }
}