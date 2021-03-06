package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements  Connections<T>  {
    private ConcurrentHashMap<Integer,ConnectionHandler> connectionHandlerMap;
    private int id;// change form static
    private ConcurrentHashMap<String,Integer> userIdMap;
    private ConcurrentHashMap<Integer,String> idUserMap;

    /**
     * Constructor
     */
   public ConnectionsImpl(){
       connectionHandlerMap=new ConcurrentHashMap<>();
       id=0;
       userIdMap=new ConcurrentHashMap<>();
       idUserMap=new ConcurrentHashMap<>();
   }
    public int getId(){
       return this.id;
    }
    /**
     * Send the given message-msg with the ConnectionHandler that represents the connectionId.
     * @param connectionId
     * @param msg
     * @return
     */
    @Override
    public boolean send(int connectionId, T msg) {
       if(msg!=null && connectionId>0){
           ConnectionHandler handler=connectionHandlerMap.get(connectionId);
           if(handler!=null){
               handler.send(msg);
               return true;
           }
       }
        return false;
    }

    /**
     *  Send the given message-msg to  each  active client
     * @param msg
     */
    @Override
    public void broadcast(T msg) {
        if (msg != null) {
            ConnectionHandler handler;
            for (int key : connectionHandlerMap.keySet()) {
                handler = connectionHandlerMap.get(key);
                if (handler != null) {
                    handler.send(msg);
                }
            }
        }
    }

    /**
     * Disconnect the ConnectionHandler on connectionId
     * @param connectionId
     */
    @Override
    public void disconnect(int connectionId) {
        ConnectionHandler connectionHandler=connectionHandlerMap.get(connectionId);
        try {
            if (connectionHandler!=null)
                connectionHandler.close();
            connectionHandlerMap.remove(connectionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * change the current id ,add new ConnectionHandler to the map with new id.
     * @param handler
     * @return
     */
    public int addConnectionHandler(ConnectionHandler handler){
        if(handler!=null) {
            id++;//change the id
            connectionHandlerMap.put(id, handler);
            return id;
        }
        return -1;
    }
    public void  addToUserIdMap(String username,Integer idClinet){
        userIdMap.put(username,idClinet);
        idUserMap.put(idClinet,username);
    }

    public ConcurrentHashMap<String,Integer> getUserIdMap(){
        return this.userIdMap;
    }


    public String getUserOfId(Integer thisIdOfClient){
        return this.idUserMap.get(thisIdOfClient);
    }
    public void removeIdFromListOfUsers(Integer idOfUser){
        String mame=this.idUserMap.get(idOfUser);
        this.idUserMap.remove(idOfUser);
        this.userIdMap.remove(mame);
    }
    public int getIdOfUser(String name) {
           Integer result=this.userIdMap.get(name);
           if(result!=null)
                return result;
        return -1;
    }
}
