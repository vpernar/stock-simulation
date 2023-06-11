package rs.raf.server;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class ServerThread implements Runnable {
    private final int connectionId;
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final MessageHandler messageHandler = new MessageHandler();
    private final Gson gson = new Gson();

    public ServerThread(Socket socket) {
        try {
            this.socket = socket;
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.connectionId = TCPServer.id.getAndIncrement();
            TCPServer.connectedClients.add(this);
        } catch (IOException e) {
            log.error("Error creating server thread: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            out.println(connectionId);
            String message;
            while ((message = in.readLine()) != null) {
                String finalMessage = message;
                new Thread(() -> messageHandler.handleMessage(finalMessage, connectionId)).start();
            }
        } catch (IOException e) {
            log.error("Error reading from socket: " + e.getMessage());
        } finally {
            try {
                if (socket != null) socket.close();
                if(out != null) out.close();
                if(in != null) in.close();
                TCPServer.connectedClients.remove(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void sendMessage(Object message) {
        out.println(gson.toJson(message));
    }

}
