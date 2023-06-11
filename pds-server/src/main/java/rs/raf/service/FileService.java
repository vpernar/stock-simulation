package rs.raf.service;

import lombok.extern.slf4j.Slf4j;
import rs.raf.grpc.RpcApi;
import rs.raf.model.Order;
import rs.raf.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static rs.raf.util.Constants.TFILE_URL;


@Slf4j
public class FileService {
    public static final Lock lock = new ReentrantLock();
    private static final Map<Pair<LocalDateTime, String>, String> transactions = new ConcurrentHashMap<>();

    public static void writeToFile(Order order, int buyerConnectionId, int sellerConnectionId) {
        new Thread(() -> {
            lock.lock();
            try {
                PrintWriter out = new PrintWriter(new FileWriter(TFILE_URL));
                String line = "Order: [" + order + " - buyer: " + buyerConnectionId + "] - seller: " + sellerConnectionId;
                out.println(line);
                transactions.put(new Pair<>(LocalDateTime.now(), order.getSymbol()), line);
                out.close();
            } catch (IOException e) {
                log.error("Failed to write transactions: " + e.getMessage());
            } finally {
                lock.unlock();
            }
        }).start();
    }

    public static List<RpcApi.TransactionsReply> getTransactions(String symbol, LocalDateTime date) {
        List<RpcApi.TransactionsReply> ret = new ArrayList<>();
            lock.lock();
            try {
                transactions.entrySet().stream()
                        .filter(entry -> entry.getKey().getFirst().toLocalDate().isEqual(date.toLocalDate())
                                && entry.getKey().getSecond().equals(symbol))
                        .forEach(entry -> {
                            RpcApi.TransactionsReply transaction = RpcApi.TransactionsReply.newBuilder()
                                    .setLine(entry.getValue())
                                    .setDate(entry.getKey().getFirst().toEpochSecond(ZoneOffset.UTC) * 1000L)
                                    .build();
                            ret.add(transaction);
                        });
                return ret;
            } finally {
                lock.unlock();
            }
    }
}
