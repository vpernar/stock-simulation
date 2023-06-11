package rs.raf;

import rs.raf.client.RPCClient;
import rs.raf.client.TCPClient;
import rs.raf.grpc.RpcApi.*;

import java.util.Scanner;

public class StockClientApplication {
    public static volatile boolean waiter = false;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RPCClient.getAllStocks();
        TCPClient client = new TCPClient(scanner);

        while (true) {
            if(waiter) break;
        }

        String input;
        while (!(input = scanner.nextLine()).equals("#exit")) {
            if (input.startsWith("bid")) {
                String[] in = input.split(" ");
                BidRequest bidRequest = BidRequest.newBuilder()
                        .setSymbol(in[1])
                        .setBidCount(Integer.parseInt(in[2]))
                        .build();

                RPCClient.doBid(bidRequest);

            } else if (input.startsWith("ask")) {
                String[] in = input.split(" ");
                AskRequest askRequest = AskRequest.newBuilder()
                        .setSymbol(in[1])
                        .setAskCount(Integer.parseInt(in[2]))
                        .build();

                RPCClient.doAsk(askRequest);

            } else if (input.startsWith("order")) {
                String[] in = input.split(" ");
                OrderDto orderDto = OrderDto.newBuilder()
                        .setSymbol(in[1])
                        .setPrice(Double.parseDouble(in[2]))
                        .setOrderCount(Integer.parseInt(in[3]))
                        .setConnectionId(client.connectionId)
                        .setOrderType(in[4].toUpperCase())
                        .build();

                RPCClient.doOrder(orderDto);

            } else if (input.startsWith("transactions")) {
                String[] in = input.split(" ");

                TransactionsRequest transactionsRequest = TransactionsRequest.newBuilder()
                        .setSymbol(in[1])
                        .setDate(Long.parseLong(in[2]))
                        .build();

                RPCClient.doTransaction(transactionsRequest);
            }
        }
    }
}
