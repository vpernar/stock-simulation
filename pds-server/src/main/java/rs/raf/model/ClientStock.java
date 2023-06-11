package rs.raf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ClientStock {
    private String symbol;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientStock cliStock = (ClientStock) o;

        return Objects.equals(symbol, cliStock.symbol);
    }

    @Override
    public int hashCode() {
        return symbol != null ? symbol.hashCode() : 0;
    }
}
