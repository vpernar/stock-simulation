package rs.raf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Order {
    int connectionId;
    String symbol;
    double price;
    int quantity;
    OrderType orderType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (connectionId != order.connectionId) return false;
        if (Double.compare(order.price, price) != 0) return false;
        if (!Objects.equals(symbol, order.symbol)) return false;
        return orderType == order.orderType;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = connectionId;
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (orderType != null ? orderType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return symbol + price + quantity + orderType;
    }
}
