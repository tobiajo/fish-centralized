package se.kth.id2212.project.fish.shared;

import java.io.Serializable;

/**
 * Created by tobias on 03/01/16.
 */
public class Message implements Serializable {

    private MessageType type;
    private MessageStatus status;
    private Serializable data;

    public Message(MessageType type, MessageStatus status, Serializable data) {
        this.type = type;
        this.status = status;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public Serializable getData() {
        return data;
    }
}
