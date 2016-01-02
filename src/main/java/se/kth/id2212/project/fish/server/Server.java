package se.kth.id2212.project.fish.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Server {

    private static final String DEFAULT_SERVER_PORT = "6958"; // FI5H => 6-9-5-8

    private String serverPort;
    private HashMap<Socket, List<String>> fileLists;

    public Server(String serverPort) {
        if (serverPort != null) {
            this.serverPort = serverPort;
        } else {
            this.serverPort = DEFAULT_SERVER_PORT;
        }
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(serverPort));
            System.out.println("FISH server started.\nPort | " + serverSocket.getLocalPort());
            for (;;) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ConnectionHandler(this, clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFileList(Socket clientSocket, List<String> fileList) {
        fileLists.put(clientSocket, fileList);
    }

    public void removeFileList(Socket clientSocket) {
        fileLists.remove(clientSocket);
    }

    public List<String> searchFileLists(String request) {
        ArrayList<String> ret = new ArrayList<>();

        Iterator it = fileLists.keySet().iterator();
        while (it.hasNext()) {
            Socket s = (Socket) it.next();
            for (String file : fileLists.get(s)) {
                if (file.equals(request)) {
                    ret.add(s.getInetAddress().getHostAddress());
                }
            }
        }

        return ret;
    }

    public static void main(String[] args) {
        new Server(args[0]).run();
    }
}
