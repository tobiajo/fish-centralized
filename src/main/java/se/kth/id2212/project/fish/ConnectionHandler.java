package se.kth.id2212.project.fish;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ConnectionHandler implements Runnable {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Server server;
    private Socket clientSocket;

    public ConnectionHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        register();
        try {
            String searchRequest = (String) in.readObject();
            List<String> sharers = server.searchFileLists(searchRequest);
            out.writeObject(sharers);
        } catch (IOException e) {
            // connection lost
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        unregister();
    }

    private void register() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            List<String> fileList = (List<String>) in.readObject();
            server.addFileList(clientSocket, fileList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void unregister() {
        try {
            server.removeFileList(clientSocket);
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
