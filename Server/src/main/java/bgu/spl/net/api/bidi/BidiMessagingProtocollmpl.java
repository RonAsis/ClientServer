package bgu.spl.net.api.bidi;

import bgu.spl.net.Messages.Message;

public class BidiMessagingProtocollmpl<T> implements  BidiMessagingProtocol{

    private boolean shouldTerminate = false;
    private int connectionId;
    private ConnectionsImpl<Message> connections;

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId=connectionId;
        this.connections=(ConnectionsImpl)connections;
    }

    @Override
    public void process(Object message) {
        Message mcl=(Message)message;
        mcl.excute();
        if(message!=null) {
            connections.send(connectionId,mcl);
        }
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
