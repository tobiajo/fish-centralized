package se.kth.id2212.project.fish.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ShareThread extends Thread {
    Client client;

    public ShareThread(Client client) {
        this.client = client;

    }

    public void run() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Integer.parseInt(client.getSharePort()));

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ShareHandler(client, socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
