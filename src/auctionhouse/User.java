package auctionhouse;

class User {
    protected String name;
    protected static int id;
    public User(String name){
        id +=1;
        this.name=name;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}