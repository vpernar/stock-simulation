package rs.raf.util;

import lombok.Data;

import java.util.List;

@Data
public class Companies {
    private String type;
    public List<String> companies;

    public Companies(List<String> companies) {
        this.companies = companies;
        this.type = "comany";
    }
}
