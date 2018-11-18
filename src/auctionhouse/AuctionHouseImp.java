/**
 * 
 */
package auctionhouse;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.logging.Logger;

/**
 * @author pbj
 *
 */
public class AuctionHouseImp implements AuctionHouse {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();
    private List<Buyer> buyers= new ArrayList<Buyer>();
    private List<Seller> sellers= new ArrayList<Seller>();
    public SortedSet<Lot> lots = new TreeSet<>(Comparator.comparing(Lot::getLotNumber));


    
    private String startBanner(String messageName) {
        return  LS 
          + "-------------------------------------------------------------" + LS
          + "MESSAGE IN: " + messageName + LS
          + "-------------------------------------------------------------";
    }
   
    public AuctionHouseImp(Parameters parameters) {

    }

    public Status registerBuyer(
            String name,
            String address,
            String bankAccount,
            String bankAuthCode) {
        logger.fine(startBanner("registerBuyer " + name));
        Buyer buyer= new Buyer(name, bankAccount, bankAuthCode, address);
        for(Buyer b : buyers){
            if(b.name.equals(name)){
            logger.fine(startBanner("registerBuyer " + name + ": FAILED"));
                return Status.error("The name provided is already in the system");
            }
        }
        buyers.add(buyer);        
        return Status.OK();
    }

    public Status registerSeller(
            String name,
            String address,
            String bankAccount) {
        logger.fine(startBanner("registerSeller " + name));

        Seller seller= new Seller(name, bankAccount, address);
        for(Seller s : sellers){
            if(s.name.equals(name)){
            logger.fine(startBanner("registerSeller " + name + ": FAILED"));
                return Status.error("The name provided is already in the system");
            }
        }
        sellers.add(seller);        
        return Status.OK();   
    }

    public Status addLot(
            String sellerName,
            int number,
            String description,
            Money reservePrice) {
        logger.fine(startBanner("addLot " + sellerName + " " + number));

        Lot lot= new Lot(sellerName, number, description, LotStatus.UNSOLD, reservePrice);
        for(Lot l : lots){
            if(l.getLotNumber()==number){
            logger.fine(startBanner("Adding lot: FAILED"));
                return Status.error("The Lot Number assigned already exist in the system");
            }
        }
        lots.add(lot); 
        return Status.OK();    
    }

    public List<CatalogueEntry> viewCatalogue() {
        logger.fine(startBanner("viewCatalog"));
        
        List<CatalogueEntry> catalogue = new ArrayList<CatalogueEntry>();
        logger.fine("Catalogue: " + catalogue.toString());
        for (Lot l: lots){
            catalogue.add(l);
        }
        return catalogue;
    }

    public Status noteInterest(
            String buyerName,
            int lotNumber) {
        logger.fine(startBanner("noteInterest " + buyerName + " " + lotNumber));
        
        return Status.OK();   
    }

    public Status openAuction(
            String auctioneerName,
            String auctioneerAddress,
            int lotNumber) {
        logger.fine(startBanner("openAuction " + auctioneerName + " " + lotNumber));
        
        return Status.OK();
    }

    public Status makeBid(
            String buyerName,
            int lotNumber,
            Money bid) {
        logger.fine(startBanner("makeBid " + buyerName + " " + lotNumber + " " + bid));

        return Status.OK();    
    }

    public Status closeAuction(
            String auctioneerName,
            int lotNumber) {
        logger.fine(startBanner("closeAuction " + auctioneerName + " " + lotNumber));
 
        return Status.OK();  
    }
}
