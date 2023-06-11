package rs.raf.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import rs.raf.model.ClientStock;
import rs.raf.model.Order;
import rs.raf.model.Stock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static rs.raf.util.Constants.FILE_URL;

@Slf4j
@Getter
public class Database {
    //id-stock
    private final Map<Integer, Stock> stocks = new ConcurrentHashMap<>();
    //connectionId-stocks
    private final Map<Integer, List<ClientStock>> clientsStocks = new ConcurrentHashMap<>();
    //connectionId-order(list offers for buying)
//    private final Map<Integer, List<Order>> bids = new ConcurrentHashMap<>();
    //connectionId-order(list offers for selling)
//    private final Map<Integer, List<Order>> asks = new ConcurrentHashMap<>();
    //company symbol - list users
    private final Map<String, List<Integer>> subscribers = new ConcurrentHashMap<>();
    private LocalDateTime lastUpdate = LocalDateTime.now();


    //compan symnol - List<Pair<Integer, Order> Pair<connectionId, bid>
    private final Map<String, List<Order>> newBids = new ConcurrentHashMap<>();
    private final Map<String, List<Order>> newAsks = new ConcurrentHashMap<>();


    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static Database INSTANCE;

    private Database() {
        init();
    }

    public static Database getInstance() {
        if (INSTANCE == null) INSTANCE = new Database();
        return INSTANCE;
    }

    public void subscribeToCompany(int connectionId, String symbol) {
        lock.readLock().lock();
        try {
            if (subscribers.containsKey(symbol)) {
                subscribers.get(symbol).add(connectionId);
            }
        } finally {
            lock.readLock().unlock();
        }
        log.info("Client with connection id: " + connectionId + " subscribed to:" + symbol);
    }

    //finds first matching ask and return that connId, ask
//    public Pair<Integer, Order> findAskBySymbolAndPrice(String symbol, double price, int connectionId) {
//        return asks.entrySet().stream()
//                .filter(entry -> entry.getKey() != connectionId)
//                .flatMap(entry -> entry.getValue().stream()
//                        .map(order -> new AbstractMap.SimpleEntry<>(entry.getKey(), order)))
//                .filter(entry -> entry.getValue().getSymbol().equals(symbol) && entry.getValue().getPrice() == price)
//                .findFirst()
//                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
//                .orElse(new Pair<>(null, null));
//    }
    public Order findAskBySymbolAndPrice(String symbol, double price) {
        return newAsks.get(symbol).stream()
                .filter(ask -> ask.getPrice() <= price)
                .min(Comparator.comparing(Order::getPrice))
                .orElse(null);
    }

//    public Pair<Integer, Order> findBidBySymbolAndPrice(String symbol, double price, int connectionId) {
//        return bids.entrySet().stream()
//                .filter(entry -> entry.getKey() != connectionId)
//                .flatMap(entry -> entry.getValue().stream()
//                        .map(order -> new AbstractMap.SimpleEntry<>(entry.getKey(), order)))
//                .filter(entry -> entry.getValue().getSymbol().equals(symbol) && entry.getValue().getPrice() == price)
//                .findFirst()
//                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
//                .orElse(new Pair<>(null, null));
//    }

    public Order findBidBySymbolAndPrice(String symbol, double price) {
        return newBids.get(symbol).stream()
                .filter(bid -> bid.getPrice() >= price)
                .max(Comparator.comparing(Order::getPrice))
                .orElse(null);
    }

    public Stock updateLastPrice(String symbol, double newSale) {
        Stock stock = findStockBySymbol(symbol);
        double change = (stock.getLastSale() - newSale) / stock.getLastSale();
        stock.setLastSale(newSale);
        stock.setChange(change);
        stock.getChangeHistory().put(LocalDateTime.now(), change);
        return stock;
    }

    public Stock findStockBySymbol(String symbol) {
        return stocks.values().stream()
                .filter(stock -> stock.getSymbol().equals(symbol))
                .findFirst()
                .orElse(null);
    }

    public void updateClientStock(int connectionId, ClientStock clientStock) {
        List<ClientStock> stocks;

        if (clientsStocks.containsKey(connectionId)) {
            stocks = clientsStocks.get(connectionId);
            if (!stocks.contains(clientStock))
                stocks.add(clientStock);
            else {
                ClientStock existing = stocks.get(stocks.indexOf(clientStock));
                existing.setQuantity(existing.getQuantity() + clientStock.getQuantity());
            }
        } else {
            stocks = new ArrayList<>();
            stocks.add(clientStock);
            clientsStocks.put(connectionId, stocks);
        }
    }

//    public void addToBids(Order order, int connectionId) {
//        List<Order> orders;
//
//        //if he has some bids, add to his list
//        if (bids.containsKey(connectionId)) {
//            orders = bids.get(connectionId);
//            if (!orders.contains(order)) {
//                orders.add(order);
//            } else {
//                Order existing = orders.get(orders.indexOf(order));
//                existing.setQuantity(existing.getQuantity() + order.getQuantity());
//            }
//            //if not then add new entry
//        } else {
//            orders = new ArrayList<>();
//            orders.add(order);
//            bids.put(connectionId, orders);
//        }
//    }

    public void addToBids(Order order) {
        List<Order> orders;

        //if he has some bids, add to his list
        if (newBids.containsKey(order.getSymbol())) {
            orders = newBids.get(order.getSymbol());
            if (!orders.contains(order)) {
                orders.add(order);
            } else {
                Order existing = orders.get(orders.indexOf(order));
                existing.setQuantity(existing.getQuantity() + order.getQuantity());
            }
            //if not then add new entry
        } else {
            orders = new ArrayList<>();
            orders.add(order);
            newBids.put(order.getSymbol(), orders);
        }
    }

    //    public void addtoAsks(Order order, int connectionId) {
//        List<Order> orders;
//
//        //if he has some asks, add to his list
//        if (asks.containsKey(connectionId)) {
//            orders = asks.get(connectionId);
//            if (!orders.contains(order)) {
//                orders.add(order);
//            } else {
//                Order existing = orders.get(orders.indexOf(order));
//                existing.setQuantity(existing.getQuantity() + order.getQuantity());
//            }
//            //if not then add new entry
//        } else {
//            orders = new ArrayList<>();
//            orders.add(order);
//            asks.put(connectionId, orders);
//        }
//    }
    public void addtoAsks(Order order) {
        List<Order> orders;

        //if he has some asks, add to his list
        if (newAsks.containsKey(order.getSymbol())) {
            orders = newAsks.get(order.getSymbol());
            if (!orders.contains(order)) {
                orders.add(order);
            } else {
                Order existing = orders.get(orders.indexOf(order));
                existing.setQuantity(existing.getQuantity() + order.getQuantity());
            }
            //if not then add new entry
        } else {
            orders = new ArrayList<>();
            orders.add(order);
            newAsks.put(order.getSymbol(), orders);
        }
    }

//    public List<Order> getAsksAscending(String symbol, int quantity) {
//        lock.readLock().lock();
//        try {
//            return asks.values().stream()
//                    .flatMap(Collection::stream)
//                    .filter(order -> order.getSymbol().equals(symbol)
//                            && order.getQuantity() >= quantity)
//                    .sorted(Comparator.comparing(Order::getPrice))
//                    .collect(Collectors.toList());
//
//        } finally {
//            lock.readLock().unlock();
//        }
//    }

    public List<Order> getAsksAscending(String symbol, int quantity) {
        lock.readLock().lock();
        try {
            return newAsks.get(symbol).stream()
                    .filter(order -> order.getQuantity() >= quantity)
                    .sorted(Comparator.comparing(Order::getPrice))
                    .collect(Collectors.toList());

        } finally {
            lock.readLock().unlock();
        }
    }

//    public List<Order> getBidsDescending(String symbol, int quantity) {
//        lock.readLock().lock();
//        try {
//            return bids.values().stream()
//                    .flatMap(Collection::stream)
//                    .filter(order -> order.getSymbol().equals(symbol)
//                            && order.getQuantity() >= quantity)
//                    .sorted(Comparator.comparing(Order::getPrice, Comparator.reverseOrder()))
//                    .collect(Collectors.toList());
//        } finally {
//            lock.readLock().unlock();
//        }
//    }

    public List<Order> getBidsDescending(String symbol, int quantity) {
        lock.readLock().lock();
        try {
            return newBids.get(symbol).stream()
                    .filter(order -> order.getQuantity() >= quantity)
                    .sorted(Comparator.comparing(Order::getPrice, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }


    private void init() {
        try {
            AtomicInteger id = new AtomicInteger();

            BufferedReader reader = new BufferedReader(new FileReader(FILE_URL));
            Stream<String> lines = reader.lines().skip(1);

            lines.forEachOrdered(line -> {
                String[] buff = line.split(",");
                Stock stock = Stock.builder()
                        .symbol(buff[0])
                        .company(buff[1])
                        .lastSale(Double.parseDouble(buff[2]))
                        .change(Double.parseDouble(buff[3]))
                        .build();
                stocks.put(id.getAndIncrement(), stock);
                stock.getChangeHistory().put(LocalDateTime.now(), stock.getChange());
                subscribers.put(stock.getSymbol(), new ArrayList<>());
                newBids.put(stock.getSymbol(), new ArrayList<>());
                newAsks.put(stock.getSymbol(), new ArrayList<>());

                clientsStocks.put(0, List.of(new ClientStock("MCD", 5), new ClientStock("GOOG", 1)));
            });

            System.out.println(clientsStocks);
            reader.close();

        } catch (FileNotFoundException e) {
            log.error("Error while reading file: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Error while reading line from file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        stocks.forEach((id, stock) -> {
            sb.append(id).append(": ").append(stock.toString()).append("\n");
        });
        return "\tDATABASE\n" + sb;
    }

//    public void removeFromAsk(Order ask, int connectionId) {
//        lock.writeLock().lock();
//        try {
//            asks.get(connectionId).remove(ask);
//        } finally {
//            lock.writeLock().unlock();
//        }
//    }

    public void removeFromAsk(Order ask) {
        lock.writeLock().lock();
        try {
            newAsks.get(ask.getSymbol()).remove(ask);
        } finally {
            lock.writeLock().unlock();
        }
    }

//    public void removeFromBid(Order bid, int connectionId) {
//        lock.writeLock().lock();
//        try {
//            bids.get(connectionId).remove(bid);
//        } finally {
//            lock.writeLock().unlock();
//        }
//    }

    public void removeFromBid(Order bid) {
        lock.writeLock().lock();
        try {
            newBids.get(bid.getSymbol()).remove(bid);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean checkStocksForSelling(Order sellOrder, int sellerConnectionId) {
        if (clientsStocks.containsKey(sellerConnectionId)) {
            return clientsStocks.get(sellerConnectionId).stream()
                    .anyMatch(clientStock -> clientStock.getSymbol().equals(sellOrder.getSymbol()) && clientStock.getQuantity() >= sellOrder.getQuantity());
        } else {
            return false;
        }
    }
}
