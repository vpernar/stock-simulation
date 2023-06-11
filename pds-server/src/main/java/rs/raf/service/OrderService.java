package rs.raf.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import rs.raf.model.ClientStock;
import rs.raf.util.Database;
import rs.raf.model.Order;
import rs.raf.model.OrderType;
import rs.raf.model.Stock;
import rs.raf.server.TCPServer;
import rs.raf.util.Feed;
import rs.raf.util.Notification;
import rs.raf.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.locks.ReadWriteLock;

import static rs.raf.util.Event.prepareEvent;

@Slf4j
@Getter
public class OrderService {
    private final Database database = Database.getInstance();
    private final ReadWriteLock database_lock = database.getLock();
    private static OrderService INSTANCE;

    private OrderService() {
    }

    public static OrderService getInstance() {
        if (INSTANCE == null) INSTANCE = new OrderService();
        return INSTANCE;
    }

    //maybe run this in new Thread?
    public void processOrder(String symbol, int orderCount, double price, String orderType, int connectionId) {
        OrderType oType = OrderType.valueOf(orderType);
        Order order = Order.builder()
                .symbol(symbol)
                .price(price)
                .quantity(orderCount)
                .connectionId(connectionId)
                .build();

        if (oType.equals(OrderType.BUY)) {
            processBuying(order, order.getConnectionId());
        } else {
            processSelling(order, order.getConnectionId());
        }
    }

//    private void processBuying(Order buyOrder, int buyerConnectionId) {
//        Stock event = null;
//        Notification sellerNotification = null;
//        Notification buyerNotification = null;
//        Integer sellerConnectionId = null;
//        Order offer = null;
//
//        database_lock.writeLock().lock();
//        try {
//            Pair<Integer, Order> pair = database.findAskBySymbolAndPrice(buyOrder.getSymbol(), buyOrder.getPrice(), buyerConnectionId);
//            //offer of seller
//            offer = pair.getSecond();
//            sellerConnectionId = pair.getFirst();
//
//            if (sellerConnectionId != null && offer != null) {
//                log.info("Processing buying for client: " + buyerConnectionId + ", for order" + buyOrder);
//                int cnt = offer.getQuantity() - buyOrder.getQuantity();
//
//                //update ask or bid (if needed)
//                offer.setQuantity(cnt);
//                if (cnt == 0) {
//                    database.removeFromAsk(offer, sellerConnectionId);
//                } else if (cnt < 0) { //seller had less than buyer so rest are going to bids
//                    database.removeFromAsk(offer, sellerConnectionId);
//                    buyOrder.setQuantity(Math.abs(cnt));
//                    database.addToBids(buyOrder, buyerConnectionId);
//                } //nothing if cnt > 0, offer already is updated
//
//                //update buyers stocks
//                ClientStock buyerStock = new ClientStock(buyOrder.getSymbol(), buyOrder.getQuantity());
//                database.updateClientStock(buyerConnectionId, buyerStock);
//
//                //update sellers stocks
//                ClientStock sellerStock = new ClientStock(offer.getSymbol(), offer.getQuantity());
//                database.updateClientStock(sellerConnectionId, sellerStock);
//
//                //update lastprice and change history
//                event = database.updateLastPrice(offer.getSymbol(), offer.getPrice());
//
//                sellerNotification = new Notification("You sold (" + offer.getQuantity() + ") stocks: " + offer.getSymbol() + " for: $" + offer.getPrice());
//                buyerNotification = new Notification("You bought (" + buyOrder.getQuantity() + ") stocks: " + buyOrder.getSymbol() + " for: $" + buyOrder.getPrice());
//
//                database.setLastUpdate(LocalDateTime.now());
//
//                log.info("[BUY] Exchange processed for buyer: " + buyerConnectionId + ", for order" + buyOrder + ", seller: " + sellerConnectionId);
//            } else {
//                log.info("There was no offer to buy for client: " + buyerConnectionId + ", for order: " + buyOrder + "\nAdding buy request to bids.");
//                database.addToBids(buyOrder, buyerConnectionId);
//            }
//        } finally {
//            database_lock.writeLock().unlock();
//            //send event if there was a change
//            if (event != null)
//                TCPServer.sendEvent(prepareEvent(event), database.getSubscribers().get(buyOrder.getSymbol()));
//            if (sellerNotification != null && buyerNotification != null) {
//                TCPServer.notifyClient(sellerConnectionId, sellerNotification);
//                TCPServer.notifyClient(buyerConnectionId, buyerNotification);
//            }
//            if (offer != null && sellerConnectionId != null)
//                FileService.writeToFile(offer, buyerConnectionId, sellerConnectionId);
//        }
//    }

    private void processBuying(Order buyOrder, int buyerConnectionId) {
        Stock event = null;
        Notification sellerNotification = null;
        Notification buyerNotification = null;
        Integer sellerConnectionId = null;
        Order offer = null;

        database_lock.writeLock().lock();
        try {
            offer = database.findAskBySymbolAndPrice(buyOrder.getSymbol(), buyOrder.getPrice());
            //offer of seller

            if (offer != null) {
                sellerConnectionId = offer.getConnectionId();

                log.info("Processing buying for client: " + buyerConnectionId + ", for order" + buyOrder);
                int cnt = offer.getQuantity() - buyOrder.getQuantity();

                //update ask or bid (if needed)
                offer.setQuantity(cnt);
                if (cnt == 0) {
                    database.removeFromAsk(offer);
                } else if (cnt < 0) { //seller had less than buyer so rest are going to bids
                    database.removeFromAsk(offer);
                    buyOrder.setQuantity(Math.abs(cnt));
                    database.addToBids(buyOrder);
                } //nothing if cnt > 0, offer already is updated

                //update buyers stocks
                ClientStock buyerStock = new ClientStock(buyOrder.getSymbol(), buyOrder.getQuantity());
                database.updateClientStock(buyerConnectionId, buyerStock);

                //update sellers stocks
                ClientStock sellerStock = new ClientStock(offer.getSymbol(), offer.getQuantity());
                database.updateClientStock(sellerConnectionId, sellerStock);

                //update lastprice and change history
                event = database.updateLastPrice(offer.getSymbol(), offer.getPrice());

                sellerNotification = new Notification("You sold (" + buyOrder.getQuantity() + ") stocks: " + offer.getSymbol() + " for: $" + offer.getPrice());
                buyerNotification = new Notification("You bought (" + buyOrder.getQuantity() + ") stocks: " + buyOrder.getSymbol() + " for: $" + offer.getPrice());

                database.setLastUpdate(LocalDateTime.now());

                log.info("[BUY] Exchange processed for buyer: " + buyerConnectionId + ", for order" + buyOrder + ", seller: " + sellerConnectionId);
            } else {
                log.info("There was no offer to buy for client: " + buyerConnectionId + ", for order: " + buyOrder + "\nAdding buy request to bids.");
                database.addToBids(buyOrder);
            }
        } finally {
            database_lock.writeLock().unlock();
            //send event if there was a change
            if (event != null)
                TCPServer.sendEvent(prepareEvent(event), database.getSubscribers().get(buyOrder.getSymbol()));
            if (sellerNotification != null && buyerNotification != null) {
                TCPServer.notifyClient(sellerConnectionId, sellerNotification);
                TCPServer.notifyClient(buyerConnectionId, buyerNotification);
            }
            if (offer != null && sellerConnectionId != null)
                FileService.writeToFile(offer, buyerConnectionId, sellerConnectionId);
        }
    }

    private void processSelling(Order sellOrder, int sellerConnectionId) {
        Stock event = null;
        Notification sellerNotification = null;
        Notification buyerNotification = null;
        Integer buyerConnectionId = null;
        Order offer = null;

        database_lock.writeLock().lock();
        try {
            //First check if the seller has enough stock
            if (!database.checkStocksForSelling(sellOrder, sellerConnectionId)) {
                sellerNotification = new Notification("You dont have enoguh " + sellOrder + " stocks to sell!");
                log.info("Client: " + sellerConnectionId + " has no enough stocks: " + sellOrder + " to sell.");
                return;
            }

            offer = database.findBidBySymbolAndPrice(sellOrder.getSymbol(), sellOrder.getPrice());
            //offer of seller

            if (offer != null) {
                buyerConnectionId = offer.getConnectionId();

                log.info("Processing selling for client: " + sellerConnectionId + ", for order" + sellOrder);
                int cnt = offer.getQuantity() - sellOrder.getQuantity();

                //update ask or bid (if needed)
                offer.setQuantity(cnt);
                if (cnt == 0) {
                    database.removeFromAsk(offer);
                } else if (cnt < 0) { //buyer had less than seller so rest are going to asks
                    database.removeFromBid(offer);
                    sellOrder.setQuantity(Math.abs(cnt));
                    database.addtoAsks(sellOrder);
                } //nothing if cnt > 0, offer already is updated

                //update seller stocks
                ClientStock sellerStock = new ClientStock(sellOrder.getSymbol(), sellOrder.getQuantity());
                database.updateClientStock(sellerConnectionId, sellerStock);

                //update buyer stocks
                ClientStock buyerStock = new ClientStock(offer.getSymbol(), offer.getQuantity());
                database.updateClientStock(buyerConnectionId, buyerStock);

                //update lastprice and change history
                event = database.updateLastPrice(offer.getSymbol(), offer.getPrice());

                buyerNotification = new Notification("You bought (" + sellOrder.getQuantity() + ") stocks: " + offer.getSymbol() + " for: $" + offer.getPrice());
                sellerNotification = new Notification("You sold (" + sellOrder.getQuantity() + ") stocks:" + sellOrder.getSymbol() + " for: $" + offer.getPrice());

                database.setLastUpdate(LocalDateTime.now());
                log.info("[SELL] Exchange processed for seller: " + sellerConnectionId + ", for order" + sellOrder + ", buyer: " + buyerConnectionId);
            } else {
                log.info("There was no offer to sell for client: " + sellerConnectionId + ", for order: " + sellOrder + "\nAdding sell request to asks.");
                database.addtoAsks(sellOrder);
            }
        } finally {
            database_lock.writeLock().unlock();
            //send event if there was a change
            if (event != null)
                TCPServer.sendEvent(prepareEvent(event), database.getSubscribers().get(sellOrder.getSymbol()));

            if (buyerNotification != null)
                TCPServer.notifyClient(buyerConnectionId, buyerNotification);

            if (sellerNotification != null)
                TCPServer.notifyClient(sellerConnectionId, sellerNotification);

            if (offer != null && buyerConnectionId != null)
                FileService.writeToFile(offer, buyerConnectionId, sellerConnectionId);
        }
    }

//    private void processSelling(Order sellOrder, int sellerConnectionId) {
//        Stock event = null;
//        Notification sellerNotification = null;
//        Notification buyerNotification = null;
//        Integer buyerConnectionId = null;
//        Order offer = null;
//
//        database_lock.writeLock().lock();
//        try {
//            //First check if the seller has enough stock
//            if (!database.checkStocksForSelling(sellOrder, sellerConnectionId)) {
//                sellerNotification = new Notification("You dont have enoguh " + sellOrder + " stocks to sell!");
//                log.info("Client: " + sellerConnectionId + " has no enough stocks: " + sellOrder + " to sell.");
//                return;
//            }
//
//            Pair<Integer, Order> pair = database.findBidBySymbolAndPrice(sellOrder.getSymbol(), sellOrder.getPrice(), sellerConnectionId);
//            //offer of seller
//            offer = pair.getSecond();
//            buyerConnectionId = pair.getFirst();
//
//            if (buyerConnectionId != null && offer != null) {
//                log.info("Processing selling for client: " + sellerConnectionId + ", for order" + sellOrder);
//                int cnt = offer.getQuantity() - sellOrder.getQuantity();
//
//                //update ask or bid (if needed)
//                offer.setQuantity(cnt);
//                if (cnt == 0) {
//                    database.removeFromAsk(offer, sellerConnectionId);
//                } else if (cnt < 0) { //buyer had less than seller so rest are going to asks
//                    database.removeFromBid(offer, sellerConnectionId);
//                    sellOrder.setQuantity(Math.abs(cnt));
//                    database.addtoAsks(sellOrder, buyerConnectionId);
//                } //nothing if cnt > 0, offer already is updated
//
//                //update seller stocks
//                ClientStock sellerStock = new ClientStock(sellOrder.getSymbol(), sellOrder.getQuantity());
//                database.updateClientStock(sellerConnectionId, sellerStock);
//
//                //update buyer stocks
//                ClientStock buyerStock = new ClientStock(offer.getSymbol(), offer.getQuantity());
//                database.updateClientStock(buyerConnectionId, buyerStock);
//
//                //update lastprice and change history
//                event = database.updateLastPrice(offer.getSymbol(), offer.getPrice());
//
//                buyerNotification = new Notification("You bought (" + offer.getQuantity() + ") stocks: " + offer.getSymbol() + " for: $" + offer.getPrice());
//                sellerNotification = new Notification("You sold (" + sellOrder.getQuantity() + ") stocks:" + sellOrder.getSymbol() + " for: $" + sellOrder.getPrice());
//
//                database.setLastUpdate(LocalDateTime.now());
//                log.info("[SELL] Exchange processed for seller: " + sellerConnectionId + ", for order" + sellOrder + ", buyer: " + buyerConnectionId);
//            } else {
//                log.info("There was no offer to sell for client: " + sellerConnectionId + ", for order: " + sellOrder + "\nAdding sell request to asks.");
//                database.addtoAsks(sellOrder, sellerConnectionId);
//            }
//        } finally {
//            database_lock.writeLock().unlock();
//            //send event if there was a change
//            if (event != null)
//                TCPServer.sendEvent(prepareEvent(event), database.getSubscribers().get(sellOrder.getSymbol()));
//
//            if (buyerNotification != null)
//                TCPServer.notifyClient(buyerConnectionId, buyerNotification);
//
//            if (sellerNotification != null)
//                TCPServer.notifyClient(sellerConnectionId, sellerNotification);
//
//            if (offer != null && buyerConnectionId != null)
//                FileService.writeToFile(offer, buyerConnectionId, sellerConnectionId);
//        }
//    }
}
