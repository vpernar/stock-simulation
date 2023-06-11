package rs.raf.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rs.raf.model.Stock;
import rs.raf.util.Companies;
import rs.raf.util.Database;
import rs.raf.util.Feed;

import java.util.ArrayList;
import java.util.List;

import static rs.raf.util.Event.prepareEvent;

public class MessageHandler {
    private final Database database = Database.getInstance();

    public void handleMessage(String message, int connectionId) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
        String type = jsonObject.get("type").getAsString();
        switch (type) {
            case "comany":
                handleCompanyMessage(gson.fromJson(jsonObject, Companies.class), connectionId);
                break;
            default:
                // Handle unknown message type
                break;
        }
    }

    private void handleCompanyMessage(Companies companies, int connectionId ) {
        List<String> symbols = new ArrayList<>();
        companies.getCompanies().forEach(symbol -> {
            symbols.add(symbol);
            database.subscribeToCompany(connectionId, symbol);
        });

        //send feed when subscribe to companies
        database.getLock().readLock().lock();
        try {
            symbols.forEach(s -> {
                Stock stock = database.findStockBySymbol(s);
                TCPServer.sendEvent(prepareEvent(stock), database.getSubscribers().get(stock.getSymbol()));
            });
        } finally {
            database.getLock().readLock().unlock();
        }
    }
}
