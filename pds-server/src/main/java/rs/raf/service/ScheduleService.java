package rs.raf.service;

import lombok.extern.slf4j.Slf4j;
import rs.raf.util.Database;
import rs.raf.model.Stock;
import rs.raf.server.TCPServer;
import rs.raf.util.Feed;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

import static rs.raf.util.Event.prepareEvent;

@Slf4j
public class ScheduleService {
    private static final Database database = Database.getInstance();
    private static final ReadWriteLock database_lock = database.getLock();
    private static boolean shouldSendEvents = false;


    public static void startScheduleService() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(() -> {

            database_lock.readLock().lock();
            try {
                Duration duration = Duration.between(LocalDateTime.now(), database.getLastUpdate());
                shouldSendEvents = duration.compareTo(Duration.ofMinutes(6)) >= 0;

                if (!shouldSendEvents) {
                    log.info("Feed from scheduler");

                    database.getStocks().values().forEach(stock ->
                            TCPServer.sendEvent(prepareEvent(stock), database.getSubscribers().get(stock.getSymbol())));
                }
            } finally {
                database_lock.readLock().unlock();
            }

        }, 0, 6, TimeUnit.MINUTES);
    }
}
