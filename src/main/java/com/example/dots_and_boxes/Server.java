package com.example.dots_and_boxes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    public List<Client> clients = new ArrayList<>();

    public int whichturn = 1;

    void startServer() throws IOException {
        int port = 8080;
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ServerSocket ss = new ServerSocket(port, 0, ip);
            System.out.println("server started");
            while (true) {
                if (clients.size() < 2) {
                    Socket cs = ss.accept();
                    System.out.println("client connected, port: " + cs.getPort());

                    // Создаем новый поток для обработки клиента
                    Client client = new Client();
                    client.clientSocket = cs;
                    clients.add(client);
                    Thread clientThread = new Thread(new ClientHandler(client, this));
                    clientThread.start();
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendUpdate(String msg) throws IOException {
        for (Client client : clients) {
            PrintWriter out = new PrintWriter(client.clientSocket.getOutputStream(), true);
            out.println(msg);
        }
    }


    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }
}
