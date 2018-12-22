package bgu.spl.net.Messages;

import bgu.spl.net.Future;

public abstract class MessagesClientToServer implements Message {

    private Future<MessagesServerToClient> result;

    public MessagesClientToServer(Future<MessagesServerToClient> result){
        this.result=result;
    }

    public abstract void  excute();

    public String getResult(){
        return this.result.get().getContainResult();
    }

    public void setResult(MessagesServerToClient result) {
        this.result.resolve(result);
    }
}
