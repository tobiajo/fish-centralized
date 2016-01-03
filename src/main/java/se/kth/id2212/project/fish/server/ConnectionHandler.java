package se.kth.id2212.project.fish.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Server server;
    private Socket clientSocket;
    private RequestHandler requestHandler;

    public ConnectionHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        requestHandler = new RequestHandler(server);
    }

    @Override
    public void run() {

        register();

        while(true) {
            try {

                Request request = (Request) in.readObject();

                requestHandler.handleRequest(request);

            } catch (IOException e) {

                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    private void register() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
            Request registerRequest = (Request) in.readObject();

            Response response = requestHandler.register(clientSocket, registerRequest);
            out.writeObject(response);
            out.flush();


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
