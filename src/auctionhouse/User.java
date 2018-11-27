package auctionhouse;

public class User {
    protected String address;
    protected String name;
    protected static int id;
    public User(String name, String address){
        id +=1;
        this.address=address;
        this.name=name;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    
    public String getAddress(){
        return address;
    }
    
}