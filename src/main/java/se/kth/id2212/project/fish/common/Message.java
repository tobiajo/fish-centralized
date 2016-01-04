package se.kth.id2212.project.fish.common;

import java.io.Serializable;

public class Message implements Serializable {

    private MessageDescriptor descriptor;
    private Serializable data;

    public Message(MessageDescriptor descriptor, Serializable data) {
        this.descriptor = descriptor;
        this.data = data;
    }

    public MessageDescriptor getDescriptor() {
        return descriptor;
    }

    public Serializable getData() {
        return data;
    }
}
