package se.kth.id2212.project.fish;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Created by marcus on 02/01/16.
 */
public class RequestHandler {
    private Server server;
    private Socket clientSocket;


    public void handleRequest(Request request) {
        Response response = null;
        switch (request.getStatus()) {
            case REGISTER:
                break;
            case UNREGISTER:
                break;
            case REQUEST_FILE:
                break;
            case SEARCH_FILE:
                break;
        }
    }

}
