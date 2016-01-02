package se.kth.id2212.project.fish.shared;

import se.kth.id2212.project.fish.shared.ProtocolStatus;

import java.io.Serializable;

/**
 * Created by marcus on 02/01/16.
 */
public class Response implements Serializable {

    private ProtocolStatus status;

    public ProtocolStatus getStatus() {
        return status;
    }

    public void setStatus(ProtocolStatus status) {
        this.status = status;
    }

}
