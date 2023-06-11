package rs.raf.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;
import rs.raf.service.StockService;

import java.io.IOException;

import static rs.raf.util.Constants.RPC_PORT;

@Slf4j
public class RPCServer {

    public static void startRpcServer() {
        new Thread(() -> {
            Server server = ServerBuilder
                    .forPort(RPC_PORT)
                    .addService(new StockService())
                    .addService(ProtoReflectionService.newInstance())
                    .build();

            try {
                server.start();
                log.info("RPC server started!");

                server.awaitTermination();
            } catch (IOException | InterruptedException e) {
                log.error("Error with RPC server: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();
    }
}
