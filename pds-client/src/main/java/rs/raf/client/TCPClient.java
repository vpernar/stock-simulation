package rs.raf.client;

import com.google.gson.Gson;
import rs.raf.StockClientApplication;
import rs.raf.util.Companies;
import rs.raf.util.Feed;
import rs.raf.util.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import static rs.raf.util.Constants.SERVER_IP;
import static rs.raf.util.Constants.TCP_PORT;

public class TCPClient {

    private final Gson gson = new Gson();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public int connectionId;
    private final MessageHandler messageHandler = new MessageHandler();

    public TCPClient(Scanner scanner) {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_IP, TCP_PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                connectionId = Integer.parseInt(in.readLine());

                String message;
                String[] input = scanner.nextLine().split(" ");

                Companies companies = new Companies(Arrays.asList(input));
                message = gson.toJson(companies);
                out.println(message);

                for(int i = 0; i < input.length; i++) {
                    System.out.println(gson.fromJson(in.readLine(), Feed.class));
                }

                StockClientApplication.waiter = true;

                while ((message = in.readLine()) != null) {
                    messageHandler.handleMessage(message, connectionId);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
