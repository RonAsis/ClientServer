package bgu.spl.net.api.bidi;

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

    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
