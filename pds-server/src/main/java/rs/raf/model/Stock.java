package rs.raf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private String symbol;
    private String company;
    private double lastSale;
    private double change;
    private final NavigableMap<LocalDateTime, Double> changeHistory = new TreeMap<>();

    @Override
    public String toString() {
        String historyString = changeHistory.entrySet()
                .stream()
                .map(entry -> "(" + entry.getKey().format(DateTimeFormatter.ofPattern("dd.MM.yyyy:HH:mm"))
                        + ", " + String.format("%.2f", entry.getValue()) + ")")
                .collect(Collectors.joining(" "));

        return "symbol: " + symbol +
                ", company: "  + company +
                ", last change: " + String.format("%.2f", lastSale) +
                ", change:" + String.format("%.2f", change) +
                ",\nchangeHistory: [ " + historyString +" ]";
    }


}
