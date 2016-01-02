package se.kth.id2212.project.fish.server;

import se.kth.id2212.project.fish.server.Server;
import se.kth.id2212.project.fish.shared.ProtocolStatus;
import se.kth.id2212.project.fish.shared.Request;
import se.kth.id2212.project.fish.shared.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Created by marcus on 02/01/16.
 */
public class RequestHandler {
    private Server server;

    public RequestHandler(Server server) {
       this.server = server;

    }


    public Response handleRequest(Request request) {
        Response response = null;
        switch (request.getStatus()) {
            case UNREGISTER:
                // TODO: Implement
                break;
            case SEARCH_FILE:
                // TODO: Implement
                break;
        }
        return response;
    }


    public Response register(Socket clientSocket, Request request) {
        List<String> sharedFiles = request.getSharedFiles();

        server.addFileList(clientSocket, sharedFiles);
        Response response = new Response();
        response.setStatus(ProtocolStatus.OK);
        System.out.println("\nA Client has been successfully registered!");
        System.out.println("The following files where added: ");

        sharedFiles.forEach(file -> System.out.println(file));
        System.out.println();

        return response;
    }



}
