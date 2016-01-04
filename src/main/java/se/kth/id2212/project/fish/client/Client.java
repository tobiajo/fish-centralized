package se.kth.id2212.project.fish.client;

import se.kth.id2212.project.fish.common.Message;
import se.kth.id2212.project.fish.common.MessageDescriptor;
import se.kth.id2212.project.fish.common.ProtocolException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private static final String DEFAULT_SHARED_FILE_PATH = "shared";
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";
    private static final String DEFAULT_SERVER_PORT = "6958"; // FI5H => 6-9-5-8

    private String sharedFilePath, serverAddress, serverPort;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client() {
        this(DEFAULT_SHARED_FILE_PATH, DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT);
    }

    public Client(String sharedFilePath, String serverAddress, String serverPort) {
        this.sharedFilePath = sharedFilePath;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void run() {
        System.out.println("Connecting to " + serverAddress + ":" + serverPort + "...");
        if (connect() && register()) prompt();
    }

    private boolean connect() {
        try {
            socket = new Socket(serverAddress, Integer.parseInt(serverPort));
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected");
            return true;
        } catch (IOException e) {
            System.out.println("Connecting failed");
            return false;
        }
    }

    private boolean register() {
        try {
            out.writeObject(new Message(MessageDescriptor.REGISTER, getFileList()));
            Message m = (Message) in.readObject();
            if (m.getDescriptor() != MessageDescriptor.REGISTER_OK) {
                throw new ProtocolException();
            }
            System.out.println("Registered");
            return true;
        } catch (IOException | ClassNotFoundException | ProtocolException e) {
            System.out.println("Registration failed");
            return false;
        }
    }

    private ArrayList<String> getFileList() {
        ArrayList<String> ret = new ArrayList<>();

        System.out.println("Shared files:");
        File dir = new File(sharedFilePath);
        dir.mkdir();
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                System.out.println("  " + sharedFilePath + "/" + f.getName());
                ret.add(f.getName());
            }
        }

        return ret;
    }

    private void prompt() {
        System.out.println("\nFISH client ready.");
        for (boolean stop = false; !stop; ) {
            System.out.print("\n1. Search\n2. Exit\n> ");
            switch (new Scanner(System.in).nextLine()) {
                case "1":
                    try {
                        search();
                    } catch (IOException | ClassNotFoundException | ProtocolException e) {
                        System.out.println("Search failed");
                    }
                    break;
                case "2":
                    try {
                        unregister();
                        System.out.println("Unregistered");
                    } catch (IOException | ClassNotFoundException | ProtocolException e) {
                        System.out.println("Unregistration failed");
                        return;
                    }
                    stop = true;
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }

    private void search() throws IOException, ClassNotFoundException, ProtocolException {
        System.out.print("Request: ");
        out.writeObject(new Message(MessageDescriptor.SEARCH, new Scanner(System.in).nextLine()));
        Message m = (Message) in.readObject();
        if (m.getDescriptor() != MessageDescriptor.SEARCH_RESULT) {
            throw new ProtocolException();
        }
        if (m.getData() != null) {
            System.out.println("Available at:");
            ((ArrayList<String>) m.getData()).forEach(address -> System.out.println(address));
        } else {
            System.out.println("File not found");
        }
    }

    private void unregister() throws IOException, ClassNotFoundException, ProtocolException {
        out.writeObject(new Message(MessageDescriptor.UNREGISTER, null));
        Message m = (Message) in.readObject();
        if (m.getDescriptor() != MessageDescriptor.UNREGISTER_OK) {
            throw new ProtocolException();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            new Client().run();
        } else if (args.length == 3) {
            new Client(args[0], args[1], args[2]).run();
        } else {
            System.out.println("error: invalid number of arguments");
        }
    }
}
