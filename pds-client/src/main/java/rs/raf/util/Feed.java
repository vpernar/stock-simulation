package rs.raf.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Feed {
    private String type;
    private String symbol;
    private double currentPrice;
    private List<Double> changes;

    public Feed(String symbol, double currentPrice, List<Double> changes) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.changes = changes;
        this.type = "feed";
    }

    @Override
    public String toString() {
        return "Feed: " + symbol + " $" + currentPrice + " " + changes;
    }
}
