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

    private int score = 0;

    private String line = null;
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
        Scene scene = new Scene(fxmlLoader.load(), 513, 304);
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
                        if (id == 1) {
                            Platform.runLater(() -> {
                                mainCont.turn.setText("Yours");
                            });
                        } else {
                            Platform.runLater(() -> {
                                mainCont.turn.setText("Not Yours");
                            });
                        }
                        Platform.runLater(() -> {
                            mainCont.id.setText(ar[1]);
                        });
                    }
                    if (ar[0].equals("switch2")) {
                        if (id == 1) {
                            Platform.runLater(() -> {
                                mainCont.turn.setText("Yours");
                            });
                        } else {
                            Platform.runLater(() -> {
                                mainCont.turn.setText("Not Yours");
                            });
                        }
                    }
                    if (ar[0].equals("switch1")) {
                        if (id == 2) {
                            Platform.runLater(() -> {
                                mainCont.turn.setText("Yours");
                            });
                        } else {
                            Platform.runLater(() -> {
                                mainCont.turn.setText("Not Yours");
                            });
                        }
                    }
                    if (ar[0].equals("score")) {
                        score++;
                        System.out.println(score);
                        Platform.runLater(() -> {
                            mainCont.score.setText(String.valueOf(score));
                        });
                    }
                    if (ar[0].equals("correct")) {

                        // Обновление элементов пользовательского интерфейса в JavaFX Application Thread
                        Platform.runLater(() -> {
                            switch (ar[1]) {
                                case "12":
                                    mainCont.l12.setVisible(true);
                                    break;
                                case "23":
                                    mainCont.l23.setVisible(true);
                                    break;
                                case "34":
                                    mainCont.l34.setVisible(true);
                                    break;
                                case "56":
                                    mainCont.l56.setVisible(true);
                                    break;
                                case "67":
                                    mainCont.l67.setVisible(true);
                                    break;
                                case "78":
                                    mainCont.l78.setVisible(true);
                                    break;
                                case "910":
                                    mainCont.l910.setVisible(true);
                                    break;
                                case "1011":
                                    mainCont.l1011.setVisible(true);
                                    break;
                                case "1112":
                                    mainCont.l1112.setVisible(true);
                                    break;
                                case "1314":
                                    mainCont.l1314.setVisible(true);
                                    break;
                                case "1415":
                                    mainCont.l1415.setVisible(true);
                                    break;
                                case "1516":
                                    mainCont.l1516.setVisible(true);
                                    break;
                                case "15":
                                    mainCont.l15.setVisible(true);
                                    break;
                                case "26":
                                    mainCont.l26.setVisible(true);
                                    break;
                                case "37":
                                    mainCont.l37.setVisible(true);
                                    break;
                                case "48":
                                    mainCont.l48.setVisible(true);
                                    break;
                                case "59":
                                    mainCont.l59.setVisible(true);
                                    break;
                                case "610":
                                    mainCont.l610.setVisible(true);
                                    break;
                                case "711":
                                    mainCont.l711.setVisible(true);
                                    break;
                                case "812":
                                    mainCont.l812.setVisible(true);
                                    break;
                                case "913":
                                    mainCont.l913.setVisible(true);
                                    break;
                                case "1014":
                                    mainCont.l1014.setVisible(true);
                                    break;
                                case "1115":
                                    mainCont.l1115.setVisible(true);
                                    break;
                                case "1216":
                                    mainCont.l1216.setVisible(true);
                                    break;
                            }
                        });
                    }
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