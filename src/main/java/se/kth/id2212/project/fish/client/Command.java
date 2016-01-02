package se.kth.id2212.project.fish.client;

/**
 * Created by marcus on 02/01/16.
 */
public class Command {
    private String fileName;
    private CommandName commandName;

    public String getFirstArgument() {
        return fileName;
    }

    public CommandName getCommandName() {
        return commandName;
    }

    public Command(CommandName commandName, String fileName ) {
        this.commandName = commandName;
        this.fileName = fileName;
    }
}
