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
    private static final String DEFAULT_SHARE_PORT = "6959";

    private static final boolean DEBUG = false;

    public String getSharedFilePath() {
        return sharedFilePath;
    }

    public String getSharePort() {
        return sharePort;
    }

    private String sharedFilePath, serverAddress, serverPort, sharePort;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client() {
        this(DEFAULT_SHARED_FILE_PATH, DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT, DEFAULT_SHARE_PORT);
    }

    public Client(String sharedFilePath, String serverAddress, String serverPort, String sharePort) {
        this.sharedFilePath = sharedFilePath;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.sharePort = sharePort;
    }

    public void run() {
        System.out.println("Connecting to " + serverAddress + ":" + serverPort + "...");

        if(DEBUG != true) {
            System.out.println("Sharing files");
            share();
        }

        if(DEBUG == true) {
            try {
                fetch("test.txt", "127.0.0.1", ".");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            }


        }



        if (connect() && register())  {
            prompt();
        }

    }


    private void share() {

        ShareThread shareThread = new ShareThread(this);
        shareThread.start();

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

    public ArrayList<String> getFileList() {
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

    private void fetch(String fileName, String address, String destinationPath) throws IOException, ClassNotFoundException, ProtocolException {
        Socket sourceSocket = new Socket(address, Integer.parseInt(sharePort));
        out = new ObjectOutputStream(sourceSocket.getOutputStream());
        out.flush();

        out.writeObject(new Message(MessageDescriptor.FETCH_FILE, destinationPath + "/" + fileName));

        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream out = new BufferedOutputStream(fos);
        byte[] buffer = new byte[1024];
        int count;
        InputStream in = sourceSocket.getInputStream();
        while((count = in.read(buffer)) >= 0) {
            fos.write(buffer, 0, count);
        }

        System.out.println("File downloaded!");


    }

    private void search() throws IOException, ClassNotFoundException, ProtocolException {
        System.out.print("Request: ");
        String fileName = new Scanner(System.in).nextLine();

        out.writeObject(new Message(MessageDescriptor.SEARCH, fileName));
        Message m = (Message) in.readObject();
        if (m.getDescriptor() != MessageDescriptor.SEARCH_RESULT) {
            throw new ProtocolException();
        }

        final int[] seedNumber = {1};
        StringBuilder sb = new StringBuilder();
        if (m.getData() != null) {
            System.out.println("Available at:");
        ((ArrayList<String>) m.getData()).forEach(address -> {System.out.println(seedNumber[0] + ". "+ address); seedNumber[0]++; });
            System.out.println("Do you want to download the file?");

            System.out.print("\n1. Yes\n2. No\n> ");

            switch (new Scanner(System.in).nextLine()) {
                case "1":
                    // TODO: prompt user input for which seed to download from

                    break;
                case "2":
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }

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
        } else if (args.length == 4) {
            new Client(args[0], args[1], args[2], args[3]).run();
        } else {
            System.out.println("error: invalid number of arguments");
        }
    }
}
