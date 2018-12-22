package bgu.spl.net.Messages;

public class NotificationMessage implements  MessagesServerToClient {
    int opcode =9;
    int notificationType;
    String postinguser;
    String content;

    public  NotificationMessage(int notificationType, String postinguser,String content){
        this.notificationType=notificationType;
        this.postinguser=postinguser;
        this.content=content;
    }
    @Override

    public String getContainResult() {
        return null;
    }
}
