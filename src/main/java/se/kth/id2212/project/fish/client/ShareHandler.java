package se.kth.id2212.project.fish.client;

import se.kth.id2212.project.fish.common.Message;
import se.kth.id2212.project.fish.common.MessageDescriptor;

import java.io.*;
import java.net.Socket;

public class ShareHandler implements Runnable{
    private ObjectInputStream in;
    private ObjectOutputStream out;
    Socket socket;
    Client client;


    public ShareHandler(Client client, Socket socket) {
        this.socket = socket;
        this.client = client;
    }



    @Override
    public void run() {

        serve();
    }

    private void serve() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            OutputStream out = socket.getOutputStream();

            Message request = (Message) in.readObject();
            if(request.getDescriptor() == MessageDescriptor.FETCH_FILE) {
                String fileName = (String) request.getData();


                File file = new File(client.getSharedFilePath() + "/" + fileName);

                int count;
                byte[] buffer = new byte[Math.toIntExact(file.length())];

                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                while((count = in.read(buffer)) > 0) {
                    out.write(buffer, 0 , count);
                    out.flush();
                }
                socket.close();



            } else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


}
