package rs.raf.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import rs.raf.util.Database;
import rs.raf.model.Order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static rs.raf.grpc.StockServiceGrpc.StockServiceImplBase;
import static rs.raf.grpc.RpcApi.*;

public class StockService extends StockServiceImplBase {
    Database database = Database.getInstance();
    OrderService orderService = OrderService.getInstance();

    @Override
    public void getAllStocks(Empty request, StreamObserver<StockDto> responseObserver) {
        database.getStocks().values().forEach(stock -> {
            StockDto stockDto = StockDto.newBuilder()
                    .setChange(stock.getChange())
                    .setCompany(stock.getCompany())
                    .setSymbol(stock.getSymbol())
                    .setDate(stock.getChangeHistory().lastEntry().getKey().getNano())
                    .setLastSale(stock.getLastSale())
                    .build();

            responseObserver.onNext(stockDto);
        });
        responseObserver.onCompleted();
    }

    @Override
    public void ask(AskRequest request, StreamObserver<AskResponse> responseObserver) {
        List<Order> asks = database.getAsksAscending(request.getSymbol(), request.getAskCount());

        asks.forEach(order -> {
            AskResponse ask = AskResponse.newBuilder()
                    .setSymbol(order.getSymbol())
                    .setPrice(order.getPrice())
                    .setAskCount(order.getQuantity())
                    .build();
            responseObserver.onNext(ask);
        });

        responseObserver.onCompleted();
    }

    @Override
    public void bid(BidRequest request, StreamObserver<BidResponse> responseObserver) {
        List<Order> bids = database.getBidsDescending(request.getSymbol(), request.getBidCount());

        bids.forEach(order -> {
            BidResponse bid = BidResponse.newBuilder()
                    .setSymbol(order.getSymbol())
                    .setPrice(order.getPrice())
                    .setBidCount(order.getQuantity())
                    .build();
            responseObserver.onNext(bid);
        });
        responseObserver.onCompleted();
    }

    @Override
    public void order(OrderDto request, StreamObserver<Empty> responseObserver) {
        orderService.processOrder(request.getSymbol(), request.getOrderCount(), request.getPrice(),
                request.getOrderType(), request.getConnectionId());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void transactions(TransactionsRequest request, StreamObserver<TransactionsReply> responseObserver) {
        Instant instant = Instant.ofEpochMilli(request.getDate());
        LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        FileService.getTransactions(request.getSymbol(), date).forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}

