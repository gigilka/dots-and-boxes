package com.example.dots_and_boxes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Application {
    public int id = -1;
    public Socket clientSocket;
    public static PrintWriter out;
    private static BufferedReader in;
    public GameController mainCont;

    @Override
    public void start(Stage stage) throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        int port = 8080;
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        clientSocket = new Socket();
        clientSocket.connect(socketAddress);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println("client connected");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Dots&boxes");
        stage.setScene(scene);
        stage.show();
        mainCont = fxmlLoader.getController();
        mainCont.cl = this;

        new Thread(() -> {
            String response;
            try {
                while ((response = in.readLine()) != null) {
                    System.out.println("From server: " + response);
                    String[] ar = response.split(" ");
                    if (ar[0].equals("id")) {
                        id = Integer.parseInt(ar[1]);
                        System.out.println(id);
                    }

                    // Обновление элементов пользовательского интерфейса в JavaFX Application Thread
                    Platform.runLater(() -> {

                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static void sendMessage(String msg) throws IOException {
        out.println(msg);
    }

    public static void main(String[] args) {
        launch(args);
    }
}