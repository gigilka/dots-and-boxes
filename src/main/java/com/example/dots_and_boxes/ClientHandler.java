package com.example.dots_and_boxes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClientHandler implements Runnable {
    private Client client;
    private BufferedReader in;
    private PrintWriter out;

    private Server server;

    public ClientHandler(Client client, Server server) throws IOException {
        this.client = client;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(client.clientSocket.getInputStream()));
        this.out = new PrintWriter(client.clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            //BufferedReader reader = new BufferedReader(new InputStreamReader(client.clientSocket.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                // Обрабатывайте полученные сообщения здесь
                System.out.println("Received message from client: " + message);
                System.out.println(client.id);
                if (message.equals("Pane clicked")) {

                }
                if (message.equals("client connected")) {
                    out.println("id " + Integer.toString(server.clients.size()));
                }
            }
        } catch (IOException e) {
            // Обработайте ошибку чтения данных
            e.printStackTrace();
        }
    }

}
