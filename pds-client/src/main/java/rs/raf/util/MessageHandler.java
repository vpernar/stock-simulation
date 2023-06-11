package rs.raf.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MessageHandler {
    public void handleMessage(String message, int connectiondId) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
        String type = jsonObject.get("type").getAsString();
        switch (type) {
            case "notification":
                handleCompanyNotification(gson.fromJson(jsonObject, Notification.class), connectiondId);
                break;
            case "feed":
                handleCompanyFeed(gson.fromJson(jsonObject, Feed.class), connectiondId);
                break;
            default:
                // Handle unknown message type
                break;
        }
    }

    private void handleCompanyFeed(Feed feed, int connectiondId) {
        System.out.println(feed.getSymbol() + " " + feed.getCurrentPrice() + " " + feed.getChanges());
    }

    private void handleCompanyNotification(Notification notification, int connectiondId) {
        System.out.println(notification);
    }
}
