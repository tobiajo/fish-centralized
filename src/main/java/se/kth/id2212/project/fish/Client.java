package se.kth.id2212.project.fish;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Client {

    private static final String DEFAULT_SHARED_FILE_PATH = "shared";
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";
    private static final String DEFAULT_SERVER_PORT = "6958"; // FI5H => 6-9-5-8

    private String sharedFilePath, serverAddress, serverPort;

    public Client(String sharedFilePath, String serverAddress, String serverPort) {
        if (sharedFilePath != null) {
            this.sharedFilePath = sharedFilePath;
        } else {
            this.sharedFilePath = DEFAULT_SHARED_FILE_PATH;
        }
        if (serverAddress != null) {
            this.serverAddress = serverAddress;
        } else {
            this.serverAddress = DEFAULT_SERVER_ADDRESS;
        }
        if (serverPort != null) {
            this.serverPort = serverPort;
        } else {
            this.serverPort = DEFAULT_SERVER_PORT;
        }
    }

    public void run() {
        System.out.println("FISH client started.\n   path | " + sharedFilePath +
                "\naddress | " + serverAddress + "\n   port | " + serverPort);
        List<String> fileList = getFileList();


        promptMenu();


        // TODO: Implement a Client-Server protocol

    }


    private void promptMenu() {
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            CommandName[] options = CommandName.values();

            System.out.println("\nCommand options:");
            for(CommandName option : options) {
                System.out.println(" - " + option.name());
            }
            System.out.print("\n>");

            try {
                String userInput = consoleIn.readLine();
                executeCommand(parse(userInput));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    void executeCommand(Command command) {
        if (command == null) {
            return;
        }

        switch (command.getCommandName()) {
            case request:
                System.out.println("DUMMY: Requesting file");
                return;
            case search:
                System.out.println("DUMMY: Searching for file");
                return;
            case quit:
                System.exit(0);
            default:
                System.out.println("Illegal command");
                return;
        }

    }




    private Command parse(String userInput) {
        if (userInput == null) {
            return null;
        }

        StringTokenizer tokenizer = new StringTokenizer(userInput);
        if (tokenizer.countTokens() == 0) {
            return null;
        }

        CommandName commandName = null;
        String fileName = null;
        int userInputTokenNo = 1;

        while (tokenizer.hasMoreTokens()) {
            switch (userInputTokenNo) {
                case 1:
                    try {
                        String commandNameString = tokenizer.nextToken();
                        commandName = CommandName.valueOf(CommandName.class, commandNameString);
                    } catch (IllegalArgumentException commandDoesNotExist) {
                        System.out.println("Illegal command");
                        return null;
                    }
                    break;
                case 2:
                    fileName = tokenizer.nextToken();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Illegal command");
                    return null;
            }
            userInputTokenNo++;
        }
        return new Command(commandName, fileName);
    }



    private List<String> getFileList() {
        ArrayList<String> ret = new ArrayList<>();

        System.out.println("\nShared files:");
        File dir = new File(sharedFilePath);
        dir.mkdir();
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                System.out.println(f.getName());
                ret.add(f.getName());
            }
        }

        return ret;
    }

    public static void main(String[] args) {
        new Client(args[0], args[1], args[2]).run();
    }
}
