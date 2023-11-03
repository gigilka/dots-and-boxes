package com.example.dots_and_boxes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class ClientHandler implements Runnable {

    private Integer firstPoint = null, secondPoint = null;
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
                String[] ar = message.split(" ");

                if (fieldIsEmpty()) {

                }
                //System.out.println(client.id); always ret -1 cause of order of code
                if (ar[0].equals("idpo")) {
                    System.out.println(server.checkId(Integer.parseInt(ar[2])));

                    if (!server.checkId(Integer.parseInt(ar[2]))) {
                        out.println("not your turn"); // check for turn id
                    } else {
                        if (firstPoint == null) {
                            firstPoint = Integer.parseInt(ar[1]);
                        } else {
                            secondPoint = Integer.parseInt(ar[1]);
                            if (firstPoint > secondPoint) {
                                int temp = firstPoint;
                                firstPoint = secondPoint;
                                secondPoint = temp;
                            }
                            if (!turnIsCorrect(firstPoint, secondPoint)) {
                                out.println("incorrect");
                                firstPoint = null;
                                secondPoint = null;
                            } else {
                                server.sendUpdate("correct " + firstPoint + secondPoint);
                                server.gamefield.add(firstPoint + "-" + secondPoint);
                                firstPoint = null;
                                secondPoint = null;
                                int tmpscore = isSquare();
                                if ((tmpscore == 0)) {
                                    server.sendUpdate("switch" + ar[2]);
                                    switch (server.whichturn) {
                                        case 1:
                                            server.whichturn = 2;
                                            break;
                                        case 2:
                                            server.whichturn = 1;
                                            break;
                                    }
                                } else switch (ar[2]) {
                                    case "1":
                                        server.score1 += tmpscore;
                                        out.println("score " + server.score1);
                                        break;
                                    case "2":
                                        server.score2 += tmpscore;
                                        out.println("score " + server.score2);
                                        break;
                                }

                            }
                            if (fieldIsEmpty()) {
                                if (server.score1>server.score2)
                                    server.sendUpdate("winner 1");
                                else server.sendUpdate("winner 2");
                            }
                        }
                        System.out.println(firstPoint + " " + secondPoint);

                    }
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


    private boolean turnIsCorrect(Integer fp, Integer sp) {

        String line = fp + "-" + sp;
        for (int i = 0; i < server.linesar.size(); i++) {
            if (line.equals(server.linesar.get(i))) {
                server.linesar.remove(i);
                return true;
            }
        }
        return false;
    }

    private int isSquare() {
        int scorecount = 0;
        ArrayList<Integer> is = new ArrayList<>();
        for (int i = 0; i < server.squaresList.size(); i++) {
            int count = 0;
            for (int j = 0; j < server.squaresList.get(0).size(); j++) {
                for (int k = 0; k < server.gamefield.size(); k++) {
                    if (server.gamefield.get(k).equals(server.squaresList.get(i).get(j))) {
                        count++;
                    }
                }
            }
            if (count == 4) {
                //server.squaresList.remove(i);
                is.add(i);
                scorecount++;
            }
        }
        for (int f = 0; f < is.size(); f++) {
            int indexToRemove = is.get(f) - f;
            server.squaresList.remove(indexToRemove);
        }
        return scorecount;
    }

    private boolean fieldIsEmpty() {
        return server.linesar.isEmpty();
    }

}
