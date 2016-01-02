package se.kth.id2212.project.fish.shared;

import se.kth.id2212.project.fish.shared.ProtocolStatus;

import java.io.File;
import java.io.Serializable;

/**
 * Created by marcus on 02/01/16.
 */
public class Response implements Serializable {

    private ProtocolStatus status;
    private File file;

    public ProtocolStatus getStatus() {
        return status;
    }

    public void setStatus(ProtocolStatus status) {
        this.status = status;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
