package bgu.spl.net.api.bidi;

import bgu.spl.net.Messages.MessagesClientToServer;
import bgu.spl.net.Messages.MessagesServerToClient;

public class BidiMessagingProtocollmpl implements  BidiMessagingProtocol{

    private boolean shouldTerminate = false;
    private int connectionId;
    private ConnectionsImpl<String> connections;

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId=connectionId;
        this.connections=(ConnectionsImpl)connections;
    }

    @Override
    public void process(Object message) {
        MessagesClientToServer mcl=(MessagesClientToServer)message;
        mcl.excute();
        //MessagesClientToServer msl=mcl.getResult();
        if(message!=null) {
            //this.connections.send(this.connectionId,message);
        }
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
