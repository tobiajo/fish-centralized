package se.kth.id2212.project.fish;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marcus on 02/01/16.
 */
public class Request implements Serializable{


    private ProtocolStatus status;
    private List<String> sharedFiles;

    public ProtocolStatus getStatus() {
        return status;
    }

    public void setStatus(ProtocolStatus status) {
        this.status = status;
    }

    public List<String> getSharedFiles() {
        return sharedFiles;
    }

    public void setSharedFiles(List<String> sharedFiles) {
        this.sharedFiles = sharedFiles;
    }


}

