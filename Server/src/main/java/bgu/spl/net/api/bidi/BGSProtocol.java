package bgu.spl.net.api.bidi;

import bgu.spl.net.Messages.*;
import bgu.spl.net.accessories.SharedData;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BGSProtocol<T> implements  BidiMessagingProtocol{

    private boolean shouldTerminate = false;
    private int connectionId;
    private ConnectionsImpl<Message> connections;
    private ConcurrentHashMap<String,Integer> userIdMap;
    private SharedData sharedData;

    public BGSProtocol(SharedData sharedData){
        this.sharedData=sharedData;
    }
    @Override

    public void start(int connectionId, Connections connections) {
        this.connectionId=connectionId;
        this.connections=(ConnectionsImpl)connections;
        userIdMap=new ConcurrentHashMap<>();
    }

    @Override
    public void process(Object message) {
        if(message!=null) {
            Message mcl = (Message) message;
            short suc = mcl.act(sharedData);
            if (suc == 2) {
                MessageLogin messageLogin = (MessageLogin) mcl;
                userIdMap.put(messageLogin.getNameUser(), this.connectionId);
                List<Message> list = sharedData.getMesageThatDontSendToUser(messageLogin.getNameUser());
                for (Message key : list) {
                    connections.send(connectionId, key);
                }

            } else if (suc == 3) {
                MessageLogout messageLogout = (MessageLogout) mcl;
                if(userIdMap.containsKey(messageLogout.getNameUser()))
                 userIdMap.remove(messageLogout.getNameUser());
            } else if (suc == 5) {
                NotificationMessage postMessage = (NotificationMessage) mcl.getContainResult();
                List<String> listUserSentTo=postMessage.getListUserSentMessageTo();
                for (String key: listUserSentTo){
                    Integer id=this.userIdMap.get(key);
                    if(id!=null){
                        connections.send(id.intValue(), mcl.getContainResult());
                    }
                }
                return;
            }

            connections.send(connectionId, mcl.getContainResult());
        }
    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
