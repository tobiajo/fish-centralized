package se.kth.id2212.project.fish.server;

import se.kth.id2212.project.fish.server.Server;
import se.kth.id2212.project.fish.shared.Request;
import se.kth.id2212.project.fish.shared.Response;

import java.net.Socket;

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
