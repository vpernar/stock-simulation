package rs.raf.server;

import lombok.extern.slf4j.Slf4j;
import rs.raf.util.Feed;
import rs.raf.util.Notification;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static rs.raf.util.Constants.TCP_PORT;

@Slf4j
public class TCPServer {
    public static final List<ServerThread> connectedClients = new CopyOnWriteArrayList<>(); //connected clients
    private static ServerSocket serverSocket = null;
    public static AtomicInteger id = new AtomicInteger();

    public static void startTCPServer() {
        try {
            serverSocket = new ServerSocket(TCP_PORT);
            log.info("TCP server started!");

            while (true) {
                Socket socket = serverSocket.accept();
                log.info("Client connected, port: " + socket.getPort());
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.submit(new ServerThread(socket));
            }

        } catch (IOException e) {
            log.error("Error starting the server: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public synchronized static void sendEvent(Feed feed, List<Integer> connections) {
        connectedClients.forEach(serverThread -> connections.forEach(connection -> {
            if (connection == serverThread.getConnectionId())
                serverThread.sendMessage(feed);
        }));
    }

    public synchronized static void notifyClient(int connectionId, Notification message) {
        connectedClients.stream()
                .filter(serverThread -> serverThread.getConnectionId() == connectionId)
                .findFirst()
                .ifPresent(serverThread -> serverThread.sendMessage(message));
    }
}
