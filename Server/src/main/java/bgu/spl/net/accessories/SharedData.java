package bgu.spl.net.accessories;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedData {
        ConcurrentHashMap<Integer,String> messagePost;
        ConcurrentHashMap<Integer,String> messagePM;
        ConcurrentHashMap<String,User> users;
        AtomicInteger tick;

    /**
     * Singleton of ShareData
     */
    private static class SingletonHolder {
            private static SharedData instance = new SharedData();
        }
        private SharedData() {
            messagePost=new ConcurrentHashMap<>();
            messagePM=new ConcurrentHashMap<>();
            users=new ConcurrentHashMap<>();
            tick=new AtomicInteger();
        }
        public static SharedData getInstance() {
            return SingletonHolder.instance;
        }

    /**
     * Register user to the server .
      * @param name
     * @param password
     * @return if its successful return true else false
     */
    public boolean register(String name,String password){
        if(name!=null && password!=null) {
            User successful = users.putIfAbsent(name, new User(name,password));
            if (successful != null)
                return true;
        }
        return false;
    }

    public void sendMessagePost(String msg){
        //this.messagePost.a;
    }
    public void sendMessagePM(String msg){

    }
    public void followUser(String userName, String name){

    }
    public  void unfollowUser(String userName, String nameUnfollow){

    }
}
