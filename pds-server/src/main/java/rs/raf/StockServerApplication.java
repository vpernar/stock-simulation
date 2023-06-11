package rs.raf;

import rs.raf.util.Database;
import rs.raf.server.RPCServer;
import rs.raf.server.TCPServer;
import rs.raf.service.ScheduleService;


public class StockServerApplication {

    public static void main(String[] args) {
        Database database = Database.getInstance();
        System.out.println(database);

        ScheduleService.startScheduleService();
        RPCServer.startRpcServer();
        TCPServer.startTCPServer();
    }
}
