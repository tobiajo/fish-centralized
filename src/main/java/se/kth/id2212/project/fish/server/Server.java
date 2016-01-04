package se.kth.id2212.project.fish.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    private static final String DEFAULT_SERVER_PORT = "6958"; // FI5H => 6-9-5-8

    private String serverPort;
    private HashMap<Socket, ArrayList<String>> fileLists = new HashMap<>();

    public Server() {
        this(DEFAULT_SERVER_PORT);
    }

    public Server(String serverPort) {
        this.serverPort = serverPort;
    }

    public void run() {
        try {
            System.out.println("Opening server socket " + serverPort + "...");
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(serverPort));
            System.out.println("\nFISH server ready.\n");
            for (;;) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(this, socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFileList(Socket clientSocket, ArrayList<String> fileList) {
        fileLists.put(clientSocket, fileList);
    }

    public void removeFileList(Socket clientSocket) {
        fileLists.remove(clientSocket);
    }

    public ArrayList<String> searchFileLists(String request) {
        ArrayList<String> ret = new ArrayList<>();

        fileLists.keySet().forEach(socket -> {
            fileLists.get(socket).forEach(file -> {
                if (file.equals(request)) {
                    ret.add(socket.getInetAddress().getHostAddress());
                }
            });
        });
        if (ret.size() == 0) return null;

        return ret;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            new Server().run();
        } else if (args.length == 1) {
            new Server(args[0]).run();
        } else {
            System.out.println("error: invalid number of arguments");
        }
    }
}
