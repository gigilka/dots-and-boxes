package com.example.dots_and_boxes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    String[] lines = {"1-2", "2-3", "3-4",
            "5-6", "6-7", "7-8",
            "9-10", "10-11", "11-12",
            "13-14", "14-15", "15-16",
            "1-5", "2-6", "3-7", "4-8",
            "5-9", "6-10", "7-11", "8-12",
            "9-13", "10-14", "11-15", "12-16"};

    String[][] squares = {{"1-2", "1-5", "2-6", "5-6"},
            {"2-3", "3-7", "2-6", "6-7"},
            {"3-4", "4-8", "3-7", "7-8"},
            {"5-6", "6-10", "5-9", "9-10"},
            {"6-7", "7-11", "6-10", "10-11"},
            {"7-8", "8-12", "7-11", "11-12"},
            {"9-10", "10-14", "9-13", "13-14"},
            {"10-11", "11-15", "10-14", "14-15"},
            {"11-12", "12-16", "11-15", "15-16"},};

    ArrayList<String> linesar = new ArrayList<>(List.of(lines));
    ArrayList<String> gamefield = new ArrayList<>();

    ArrayList<ArrayList<String>> squaresList = new ArrayList<>();
    public List<Client> clients = new ArrayList<>();

    public int whichturn = 1;

    void startServer() throws IOException {
        for (String[] square : squares) {
            squaresList.add(new ArrayList<>(Arrays.asList(square)));
        }

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

    public boolean checkId(int id) {
        return whichturn == id;
    }

    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }
}
