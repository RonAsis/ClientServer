package bgu.spl.net.api.bidi;

import bgu.spl.net.Messages.*;
import bgu.spl.net.accessories.SharedData;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BGSProtocol<T> implements  BidiMessagingProtocol{

    private boolean shouldTerminate = false;
    private int connectionId;
    private ConnectionsImpl<Message> connections;
    private SharedData sharedData;

    public BGSProtocol(SharedData sharedData){
        this.sharedData=sharedData;
    }
    @Override

    public void start(int connectionId, Connections connections) {
        this.connectionId=connectionId;
        this.connections=(ConnectionsImpl)connections;
    }

    @Override
    public void process(Object message) {
        if(message!=null) {
            Message mcl = (Message) message;
            short suc = mcl.act(sharedData,connections.getUserOfId(connectionId));
            if (suc == 2) {
                MessageLogin messageLogin = (MessageLogin) mcl;
                connections.send(connectionId, mcl.getContainResult());// send ack
                connections.addToUserIdMap(messageLogin.getNameUser(), this.connectionId);
                List<Message> list = sharedData.getMesageThatDontSendToUser(messageLogin.getNameUser());
                for (Message key : list) {
                    connections.send(connectionId, key);
                }
                return;
            } else if (suc == 3) {
                ConcurrentHashMap<String,Integer> userIdMap=connections.getUserIdMap();
                MessageLogout messageLogout = (MessageLogout) mcl;
                if(userIdMap.containsKey(messageLogout.getNameUser()))
                 userIdMap.remove(messageLogout.getNameUser());
                this.connections.removeIdFromListOfUsers(this.connectionId);
            } else if (suc == 5) {
                ConcurrentHashMap<String,Integer> userIdMap=connections.getUserIdMap();
                NotificationMessage postMessage = (NotificationMessage) mcl.getContainResult();
                List<String> listUserSentTo=postMessage.getListUserSentMessageTo();
                for (String key: listUserSentTo){
                    Integer id=userIdMap.get(key);
                    if(id!=null){
                        connections.send(id.intValue(), mcl.getContainResult());
                    }
                }
                connections.send(this.connectionId, new AckMessage(5));
                return;
            }
              else if(suc==6){
                PmMessage pmMessage=(PmMessage)message;
                int checkLogin=connections.getIdOfUser(pmMessage.getUserSentMessageTo());
                if(checkLogin!=-1) {
                    connections.send(connections.getIdOfUser(pmMessage.getUserSentMessageTo()), mcl.getContainResult());
                }
                connections.send(this.connectionId, new AckMessage(6));
                return;
            }
            boolean isSend=connections.send(connectionId, mcl.getContainResult());
            if(isSend && suc==3){
                //this.connections.disconnect(this.connectionId);
                this.shouldTerminate=true;
            }
        }
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }

    public int getConnectionId(){
        return this.connectionId;
    }
}
