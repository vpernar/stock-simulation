package rs.raf.client;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import rs.raf.grpc.StockServiceGrpc;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;

import static org.fusesource.jansi.Ansi.ansi;
import static rs.raf.grpc.StockServiceGrpc.StockServiceBlockingStub;
import static rs.raf.util.Constants.RPC_PORT;
import static rs.raf.util.Constants.SERVER_IP;

import rs.raf.grpc.RpcApi.*;

public class RPCClient {
    private static final SimpleDateFormat DateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_IP, RPC_PORT)
            .usePlaintext()
            .build();

    private static final StockServiceBlockingStub blockingStub = StockServiceGrpc.newBlockingStub(channel);

    public static void getAllStocks() {
        Empty request = Empty.newBuilder().build();

        Iterator<StockDto> allStocks = blockingStub.getAllStocks(request);

        while (allStocks.hasNext()) {
            StockDto stock = allStocks.next();
            printStock(stock);
        }
    }

    private static void printStock(StockDto stock) {
        AnsiConsole.systemInstall();

        Ansi.Color color = stock.getChange() > 0 ? Ansi.Color.GREEN : Ansi.Color.RED;

        AnsiConsole.sysOut().println(stock.getSymbol() + " " + stock.getCompany() + " $" + stock.getLastSale()
                + " " + ansi().fg(color).a(stock.getChange() + "% ").reset());
    }

    public static void doBid(BidRequest bidRequest) {
        Iterator<BidResponse> bids = blockingStub.bid(bidRequest);

        System.out.println("BIDS:");
        while (bids.hasNext()) {
            BidResponse bid = bids.next();
            printBids(bid);
        }
    }

    private static void printBids(BidResponse bid) {
        System.out.println(bid.getSymbol() + " $" + bid.getPrice() + " count: " + bid.getBidCount());
    }

    public static void doAsk(AskRequest askRequest) {
        Iterator<AskResponse> asks = blockingStub.ask(askRequest);

        System.out.println("ASKS:");
        while (asks.hasNext()) {
            AskResponse ask = asks.next();
            printAsks(ask);
        }
    }

    private static void printAsks(AskResponse ask) {
        System.out.println(ask.getSymbol() + " $" + ask.getPrice() + " count: " + ask.getAskCount());
    }

    public static void doOrder(OrderDto orderDto) {
        blockingStub.order(orderDto);
    }

    public static void doTransaction(TransactionsRequest transactionsRequest) {
        Iterator<TransactionsReply> trans = blockingStub.transactions(transactionsRequest);

        System.out.println("Transactions: ");
        while(trans.hasNext()){
            TransactionsReply treply = trans.next();

            Instant instant = Instant.ofEpochMilli(treply.getDate());
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            LocalDate date = dateTime.toLocalDate();

            System.out.println(DateFormat.format(date) + ":" + treply.getLine());
        }
    }
}
