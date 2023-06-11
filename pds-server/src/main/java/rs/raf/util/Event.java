package rs.raf.util;

import rs.raf.model.Stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

public class Event {
    public synchronized static Feed prepareEvent(Stock stock) {
        ArrayList<Double> changes = new ArrayList<>(getRecentChanges(stock.getChangeHistory()));

        return Feed.builder()
                .type("feed")
                .symbol(stock.getSymbol())
                .currentPrice(stock.getLastSale())
                .changes(changes)
                .build();
    }

    private static List<Double> getRecentChanges(NavigableMap<LocalDateTime, Double> changes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAgo = now.minusHours(1);
        LocalDateTime oneDayAgo = now.minusDays(1);
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        Double lastHourChange = getLastChangeBefore(oneHourAgo, changes);
        Double lastDayChange = getLastChangeBefore(oneDayAgo, changes);
        Double lastWeekChange = getLastChangeBefore(sevenDaysAgo, changes);

        return Arrays.asList(lastHourChange, lastDayChange, lastWeekChange);
    }

    private static Double getLastChangeBefore(LocalDateTime dateTime, NavigableMap<LocalDateTime, Double> changes) {
        Map.Entry<LocalDateTime, Double> entry = changes.floorEntry(dateTime);
        if (entry == null) {
            //return first input if it doesn't exist for certain date
            return changes.firstEntry().getValue();
        }
        return entry.getValue();
    }
}
