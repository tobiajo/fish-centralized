package se.kth.id2212.project.fish.server;

import se.kth.id2212.project.fish.common.Message;
import se.kth.id2212.project.fish.common.MessageDescriptor;
import se.kth.id2212.project.fish.common.ProtocolException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Server server;
    private Socket socket;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (connect() && register()) serve();
        server.removeFileList(socket);
        disconnect();
    }

    private void clientPrint(String msg) {
        System.out.println("(" + socket.getInetAddress().getHostAddress() + ") " + msg);
    }

    private boolean connect() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            clientPrint("Connected");
            return true;
        } catch (IOException e) {
            clientPrint("Connecting failed");
            return false;
        }
    }

    private boolean register() {
        try {
            Message m = (Message) in.readObject();
            if (m.getDescriptor() != MessageDescriptor.REGISTER) {
                throw new ProtocolException();
            }
            ArrayList<String> sharedFiles = (ArrayList<String>) m.getData();
            server.addFileList(socket, sharedFiles);
            out.writeObject(new Message(MessageDescriptor.REGISTER_OK, null));
            clientPrint("Registered");
            return true;
        } catch (IOException | ClassNotFoundException | ProtocolException e) {
            clientPrint("Registration failed");
            return false;
        }
    }

    private void serve() {
        for (boolean stop = false; !stop; ) {
            try {
                Message m = (Message) in.readObject();
                switch (m.getDescriptor()) {
                    case SEARCH:
                        search((String) m.getData());
                        break;
                    case UNREGISTER:
                        unregister();
                        stop = true;
                        break;
                    default:
                        throw new ProtocolException();
                }
            } catch (IOException e) {
                stop = true; // connection error => break loop
            } catch (ClassNotFoundException | ProtocolException e) {
                clientPrint("Received invalid message!");
            }
        }
    }

    private void search(String request) throws IOException {
        ArrayList<String> result = server.searchFileLists(request);
        out.writeObject(new Message(MessageDescriptor.SEARCH_RESULT, result));
        clientPrint("Searched");
    }

    private void unregister() throws IOException {
        out.writeObject(new Message(MessageDescriptor.UNREGISTER_OK, null));
        clientPrint("Unregistered");
    }

    private boolean disconnect() {
        try {
            in.close();
            out.close();
            socket.close();
            clientPrint("Disconnected");
            return true;
        } catch (IOException e) {
            clientPrint("Disconnecting failed");
            return false;
        }
    }
}
