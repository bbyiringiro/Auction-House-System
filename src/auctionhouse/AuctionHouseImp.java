/**
 * 
 */
package auctionhouse;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
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
    public List<Buyer> buyers = new ArrayList<Buyer>();
    public List<Seller> sellers = new ArrayList<Seller>();
    public SortedSet<Lot> lots = new TreeSet<>(Comparator.comparing(Lot::getLotNumber));
    public HashMap<String, List<Integer>> interestedBuyers = new HashMap<String, List<Integer>>();
    public List<Auction> auctionList = new ArrayList<Auction>();

    private String startBanner(String messageName) {
        return LS + "-------------------------------------------------------------" + LS + "MESSAGE IN: " + messageName
                + LS + "-------------------------------------------------------------";
    }

    public AuctionHouseImp(Parameters parameters) {

    }

    public Status registerBuyer(String name, String address, String bankAccount, String bankAuthCode) {
        logger.fine(startBanner("registerBuyer " + name));
        Buyer buyer = new Buyer(name, bankAccount, bankAuthCode, address);
        for (Buyer b : buyers) {
            if (b.name.equals(name)) {
                logger.fine(startBanner("registerBuyer " + name + ": FAILED"));
                return Status.error("The name provided is already in the system");
            }
        }
        buyers.add(buyer);
        return Status.OK();
    }

    public Status registerSeller(String name, String address, String bankAccount) {
        logger.fine(startBanner("registerSeller " + name));

        Seller seller = new Seller(name, bankAccount, address);
        for (Seller s : sellers) {
            if (s.name.equals(name)) {
                logger.fine(startBanner("registerSeller " + name + ": FAILED"));
                return Status.error("The name provided is already in the system");
            }
        }
        sellers.add(seller);
        return Status.OK();
    }

    public Status addLot(String sellerName, int number, String description, Money reservePrice) {
        logger.fine(startBanner("addLot " + sellerName + " " + number));

        if (!isUserExist(sellerName, "seller")) {
            logger.fine(startBanner("Add lot : FAILED"));
            return Status.error("The name provided is already in the system");
        }

        if (getLot(number) != null) {
            logger.fine(startBanner("Adding lot: FAILED"));
            return Status.error("The Lot Number assigned already exist in the system");
        }
        Lot lot = new Lot(sellerName, number, description, LotStatus.UNSOLD, reservePrice);
        lots.add(lot);
        return Status.OK();
    }

    private boolean isUserExist(String user, String type) {
        if (type == "seller") {
            for (Seller s : sellers) {
                if (s.name.equals(user)) {
                    return true;
                }
            }
        } else {
            for (Buyer s : buyers) {
                if (s.name.equals(user)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Lot getLot(int lotNumber) {
        for (Lot l : lots) {
            if (l.getLotNumber() == lotNumber) {
                return l;
            }
        }
        return null;
    }

    public List<CatalogueEntry> viewCatalogue() {
        logger.fine(startBanner("viewCatalog"));

        List<CatalogueEntry> catalogue = new ArrayList<CatalogueEntry>();
        logger.fine("Catalogue: " + catalogue.toString());
        for (Lot l : lots) {
            catalogue.add(l);
        }
        return catalogue;
    }

    public Status noteInterest(String buyerName, int lotNumber) {
        logger.fine(startBanner("noteInterest " + buyerName + " " + lotNumber));
        List<Integer> temp = new ArrayList<>();
        if (interestedBuyers.containsKey(buyerName)) {
            temp = interestedBuyers.get(buyerName);
            temp.add(lotNumber);
            interestedBuyers.put(buyerName, temp);
        } else {
            temp.add(lotNumber);
            interestedBuyers.put(buyerName, temp);
        }

        return Status.OK();
    }

    public Status openAuction(String auctioneerName, String auctioneerAddress, int lotNumber) {
        logger.fine(startBanner("openAuction " + auctioneerName + " " + lotNumber));
        Lot lot = getLot(lotNumber);
        if (lot == null) {
            logger.fine(startBanner("Opening Auction: FAILED"));
            return Status.error("The Lot Doesn't exist in the system");
        }

        if (lot.status == LotStatus.UNSOLD) {
            lot.status = LotStatus.IN_AUCTION;
            Auction auction = new Auction(lot);
            auctionList.add(auction);
            return Status.OK();
        } else {
            return Status.error("The lot should be unsold to open Action");
        }

    }

    public Status makeBid(String buyerName, int lotNumber, Money bid) {
        logger.fine(startBanner("makeBid " + buyerName + " " + lotNumber + " " + bid));
        Lot lot = getLot(lotNumber);
        if (lot.status != LotStatus.IN_AUCTION) {
            return Status.error("Lot is not in Auction");
        }

        if (lot == null) {
            logger.fine(startBanner("Make Bid: FAILED"));
            return Status.error("The Lot Doesn't exist in the system");
        }
        if (!isUserExist(buyerName, "buyer")) {
            logger.fine(startBanner("Add lot : FAILED"));
            return Status.error("The buyer name provided is already in the system");
        }
        for (Auction a : auctionList) {
            if (a.getLot().getLotNumber() == lotNumber) {
                if (!bid.lessEqual(a.highestBid)) {
                    a.bidderAndBid.put(buyerName, bid);
                    return Status.OK();
                } else {
                    return Status.error("Bid is lower than highest bid");
                }
            }
        }
        return Status.error("Lot doesn't exist");
    }

    public Status closeAuction(String auctioneerName, int lotNumber) {
        if (lot.status != LotStatus.IN_AUCTION) {
            return Status.error("Lot is not in Auction");
        }
        for (Auction a : auctionList) {
            if (a.getLot().getLotNumber() == lotNumber) {
                a.setHammerPrice();
                if (a.getLot().getReservePrice().lessEqual(a.getHammerPrice())) {
                    // bank transactions
                    // messages all who are interested
                    lot.status = LotStatus.SOLD;

                } else {
                    // notify
                    lot.status = LotStatus.UNSOLD;
                }

            }
        }
        logger.fine(startBanner("closeAuction " + auctioneerName + " " + lotNumber));

        return Status.OK();
    }
}
