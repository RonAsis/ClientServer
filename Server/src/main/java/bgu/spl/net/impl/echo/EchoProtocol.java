package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;

import java.time.LocalDateTime;

public class EchoProtocol implements BidiMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private int connectionId;
    private ConnectionsImpl<String> connections;
    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId=connectionId;
        this.connections=(ConnectionsImpl)connections;
    }

    @Override
    public void process(String msg) {
        shouldTerminate = "bye".equals(msg);
        System.out.println("[" + LocalDateTime.now() + "]: " + msg);
        System.out.println(createEcho(msg));
        connections.send(connectionId,msg);
    }

    private String createEcho(String message) {
        String echoPart = message.substring(Math.max(message.length() - 2, 0), message.length());
        return message + " .. " + echoPart + " .. " + echoPart + " ..";
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
