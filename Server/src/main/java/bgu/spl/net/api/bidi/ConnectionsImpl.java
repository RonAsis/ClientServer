package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.ConnectionHandler;

import java.util.List;

public class ConnectionsImpl<T> implements  Connections<T>  {
    List<ConnectionHandler> connectionHandlerList;
    @Override
    public boolean send(int connectionId, T msg) {
        return false;
    }

    @Override
    public void broadcast(T msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
