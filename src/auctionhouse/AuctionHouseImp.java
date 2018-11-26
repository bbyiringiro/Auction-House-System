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
// javac -sourcepath . -cp
// ../../lib/hamcrest-core-1.3.jar:../../lib/junit-4.12.jar *.java
public class AuctionHouseImp implements AuctionHouse {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();
    public List<Buyer> buyers = new ArrayList<Buyer>();
    public List<Seller> sellers = new ArrayList<Seller>();
    public List<Auctioneer> auctioneers = new ArrayList<Auctioneer>();
    public SortedSet<Lot> lots = new TreeSet<>(Comparator.comparing(Lot::getLotNumber));
    public HashMap<Integer, List<String>> interestedBuyers = new HashMap<>();
    public List<Auction> auctionList = new ArrayList<Auction>();
    public Parameters parameters;

    private String startBanner(String messageName) {
        return LS + "-------------------------------------------------------------" + LS + "MESSAGE IN: " + messageName
                + LS + "-------------------------------------------------------------";
    }

    public AuctionHouseImp(Parameters parameters) {
        this.parameters = parameters;
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

        if (getUser(sellerName, "seller") == null) {
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

    private User getUser(String user, String type) {
        switch (type) {
        case "seller":
            for (Seller s : sellers) {
                if (s.name.equals(user)) {
                    return s;
                }
            }
            break;
        case "buyer":
            for (Buyer b : buyers) {
                if (b.name.equals(user)) {
                    return b;
                }
            }
            break;
        case "auctioneer":
            for (Auctioneer a : auctioneers) {
                if (a.name.equals(user)) {
                    return a;
                }
            }
            break;
        default:
            return null;
        }

        return null;
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
        List<String> temp = new ArrayList<>();
        if (interestedBuyers.containsKey(lotNumber)) {
            temp = interestedBuyers.get(lotNumber);
            temp.add(buyerName);
            interestedBuyers.put(lotNumber, temp);
        } else {
            temp.add(buyerName);
            interestedBuyers.put(lotNumber, temp);
        }

        return Status.OK();
    }

    public Status openAuction(String auctioneerName, String auctioneerAddress, int lotNumber) {
        Auctioneer auctioneer = (Auctioneer) getUser(auctioneerName, "auctioneer");
        if (auctioneer != null) {
            if (!auctioneer.getAddress().equals(auctioneerAddress))
                return Status.error("The auctioneer address and Auctioneer do not match");
        } else {
            return Status.error("The auctioneer does not exist");
        }
        logger.fine(startBanner("openAuction " + auctioneerName + " " + lotNumber));
        Lot lot = getLot(lotNumber);
        if (lot == null) {
            logger.fine(startBanner("Opening Auction: FAILED"));
            return Status.error("The Lot Doesn't exist in the system");
        }

        if (lot.status == LotStatus.UNSOLD) {
            lot.status = LotStatus.IN_AUCTION;
            Auction auction = new Auction(lot);
            auction.setAuctioneer(auctioneerName);
            auctionList.add(auction);
            for (String buyerName : interestedBuyers.get(lotNumber)) {
                this.parameters.messagingService.auctionOpened(getUser(buyerName, "buyer").getAddress(), lotNumber);
            }
            return Status.OK(); // SUCCESS
        } else {
            return Status.error("The lot should be unsold to open Action");
        }

    }

    public Status makeBid(String buyerName, int lotNumber, Money bid) {
        List<String> temp = new ArrayList<>();
        logger.fine(startBanner("makeBid " + buyerName + " " + lotNumber + " " + bid));
        Lot lot = getLot(lotNumber);
        if (lot.status != LotStatus.IN_AUCTION) {
            return Status.error("Lot is not in Auction");
        }

        if (lot == null) {
            logger.fine(startBanner("Make Bid: FAILED"));
            return Status.error("The Lot Doesn't exist in the system");
        }
        if (getUser(buyerName, "buyer") == null) {
            logger.fine(startBanner("Add lot : FAILED"));
            return Status.error("The buyer name provided is already in the system");
        }

        for (Auction a : auctionList) {
            if (a.getLot().getLotNumber() == lotNumber) {
                if (!bid.lessEqual(a.highestBid.add(this.parameters.increment))) {

                    for (String buyername : interestedBuyers.get(lotNumber)) {
                        this.parameters.messagingService.bidAccepted(getUser(buyername, "buyer").getAddress(),
                                lotNumber, bid);
                    }
                    this.parameters.messagingService
                            .bidAccepted(getUser(a.getLot().getSellerName(), "seller").getAddress(), lotNumber, bid);
                    this.parameters.messagingService.bidAccepted(getUser(a.getAuctioneer(), "auctioneer").getAddress(),
                            lotNumber, bid);

                    if (interestedBuyers.containsKey(lotNumber)) {
                        temp = interestedBuyers.get(lotNumber);
                        temp.add(buyerName);
                        interestedBuyers.put(lotNumber, temp);
                    } else {
                        temp.add(buyerName);
                        interestedBuyers.put(lotNumber, temp);
                    }

                    a.bidderAndBid.put(buyerName, bid);
                    a.highestBid = bid;
                    a.highestBidder = (Buyer) getUser(buyerName, "buyer");
                    return Status.OK();
                } else {
                    return Status.error("Bid is lower than highest bid");
                }
            }
        }
        return Status.error("Lot doesn't exist");
    }

    public Status closeAuction(String auctioneerName, int lotNumber) {
        Lot lot = getLot(lotNumber);
        if (lot.status != LotStatus.IN_AUCTION) {
            return Status.error("Lot is not in Auction");
        }
        for (Auction a : auctionList) {
            if (a.getLot().getLotNumber() == lotNumber) {
                a.setHammerPrice();
                if (a.getLot().getReservePrice().lessEqual(a.getHammerPrice())) {
                    Buyer sender = a.getHighestBidder();
                    Seller receiver = (Seller) getUser(a.getLot().getSellerName(), "seller");

                    if (this.parameters.bankingService.transfer(sender.getAccount(), sender.getBankAuthCode(),
                            this.parameters.houseBankAccount,
                            a.getHighestBid().addPercent(this.parameters.buyerPremium)) == new Status(Status.Kind.OK)) {
                        for (String buyername : interestedBuyers.get(lotNumber)) {
                            this.parameters.messagingService.lotSold(getUser(buyername, "buyer").getAddress(),
                                    lotNumber);

                        }
                        this.parameters.messagingService
                                .lotSold(getUser(getLot(lotNumber).getSellerName(), "seller").getAddress(), lotNumber);

                        if (this.parameters.bankingService.transfer(this.parameters.houseBankAccount,
                                this.parameters.houseBankAuthCode, receiver.getAccount(),
                                a.getHighestBid().addPercent(-this.parameters.commission)) == new Status(
                                        Status.Kind.OK)) {
                            lot.status = LotStatus.SOLD;
                            return new Status(Status.Kind.SALE);
                        } else {
                            lot.status = LotStatus.SOLD_PENDING_PAYMENT;// todo: logging
                            return new Status(Status.Kind.SALE_PENDING_PAYMENT);
                        }

                    } else {
                        lot.status = LotStatus.SOLD_PENDING_PAYMENT;
                        return new Status(Status.Kind.SALE_PENDING_PAYMENT);
                    }

                    // bank transactions
                    // messages all who are interested

                } else {
                    // notify
                    lot.status = LotStatus.UNSOLD;
                    for (String buyername : interestedBuyers.get(lotNumber)) {
                        this.parameters.messagingService.lotUnsold(getUser(buyername, "buyer").getAddress(), lotNumber);

                    }
                    this.parameters.messagingService
                            .lotSold(getUser(getLot(lotNumber).getSellerName(), "seller").getAddress(), lotNumber);
                    return new Status(Status.Kind.NO_SALE);
                }

            }
        }
        logger.fine(startBanner("closeAuction " + auctioneerName + " " + lotNumber));

        return Status.OK();
    }
}
