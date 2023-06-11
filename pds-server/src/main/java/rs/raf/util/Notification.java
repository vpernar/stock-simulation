package rs.raf.util;

import lombok.Data;

@Data
public class Notification {
    private String type;
    private String message;

    public Notification(String message) {
        this.type = "notification";
        this.message = message;
    }
}
