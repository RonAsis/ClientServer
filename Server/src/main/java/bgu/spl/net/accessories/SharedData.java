package bgu.spl.net.accessories;

import bgu.spl.net.Messages.Message;
import bgu.spl.net.Messages.NotificationMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedData {
        private ConcurrentHashMap<Integer, NotificationMessage> messagePost;
        private ConcurrentLinkedQueue<String> messagePM;
        private ConcurrentHashMap<String,User> users;
        private AtomicInteger tick;
        private Object postMsgLock=new Object();
        private Object pmMsgLock=new Object();
        private Object registerLock=new Object();
    /**
     * Singleton of ShareData
     */
//    private static class SingletonHolder {
//            private static SharedData instance = new SharedData();
//        }
//        private SharedData() {
//            messagePost=new ConcurrentHashMap<>();
//            messagePM=new ConcurrentLinkedQueue<>();
//            users=new ConcurrentHashMap<>();
//            tick=new AtomicInteger();
//        }
//        public static SharedData getInstance() {
//            return SingletonHolder.instance;
//        }

    public SharedData(){
        messagePost=new ConcurrentHashMap<>();
            messagePM=new ConcurrentLinkedQueue<>();
            users=new ConcurrentHashMap<>();
            tick=new AtomicInteger();
    }
    /**
     * Register user to the server .
      * @param name
     * @param password
     * @return if its successful return true else false
     */
    public boolean register(String name,String password) {
        synchronized (registerLock) {
            if (name != null && password != null) {
                User successful = users.putIfAbsent(name, new User(name, password));
                if (successful == null)
                    return true;
            }
            return false;
        }
    }
    public boolean login(String name,String password){
        User user=users.get(name);
        if(user==null || user.isLogin())
            return false;
        else if(user.login(password))
            return  true;
        else
            return  false;

    }
    public boolean logout(String name){
        if(name!=null) {
            User user = users.get(name);
            if (user != null && user.isLogin()) {
                user.logout(tick.get());
                return true;
            } else
                return false;
        }
        return false;

    }
    public List<String> followUser(List<String> listUsers, String name){
        User user=users.get(name);//the user that want do follow
        List<String> result=new ArrayList<>();//the result that return
        if(user==null || user.isLogin()==false)// if the user is not exist or is logout
            return result;
        else{
            for (String key: listUsers){
                User followUser=users.get(key);
                if(followUser==null)// if the user from the list is not exist
                    continue;
                else{
                    if(user.areYouFollow(key))//if the user is already follow this user from the list
                        continue;
                    else{
                        user.addFollow(key);//follow after the user from the list that calls key
                        result.add(key);//add to the list that return when end pass all the list that get
                        followUser.addFollowers();//add to the user calls key that he has a new follower;
                    }
                }
            }
            return result;
        }
    }
    public  List<String> unFollowUser(List<String> listUsers, String name){
        User user=users.get(name);//the user that want do follow
        List<String> result=new ArrayList<>();//the result that return
        if(user==null || user.isLogin()==false)// if the user is not exist or is logout
            return result;
        else{
            for (String key: listUsers){
                User followUser=users.get(key);
                if(followUser==null)// if the user from the list is not exist
                    continue;
                else{
                    if(!user.areYouFollow(key))//if the user is not follow this user from the list
                        continue;
                    else{
                        user.unFollow(key);//follow after the user from the list that calls key
                        result.add(key);//add to the list that return when end pass all the list that get
                        followUser.lessFollowers();//add to the user calls key that he has a new follower;
                    }
                }
            }
            return result;
        }
    }
    public void addNotifactionToMessagesPost(Message notificationMessage){
        this.messagePost.put(channgeTick(),(NotificationMessage)notificationMessage);

    }
    public List<Message> getMesageThatDontSendToUser(String name){
        List<Message> result=new ArrayList<>();
        int tickCurrnt=this.tick.get();
        int tickLastOfUser=this.users.get(name).getTickLogOut();
        while (tickCurrnt<=tickLastOfUser){
             NotificationMessage notificationMessage= this.messagePost.get(tickLastOfUser);
            if( notificationMessage!=null &&notificationMessage.checkIfFindInTheListOfUsers(name))
                result.add(notificationMessage);
            tickLastOfUser++;
        }
        return result;
    }
    public List<String> sendMessagePost(String name,List<String> list,String msg) {
       synchronized (postMsgLock){
        User user = this.users.get(name);
        // if the user don't exist or don't login
        if (user == null || user.isLogin() == false)
            return null;
        List<String> mergeList=mergeList(list,user.getFollowList());
        user.addNumberOfPost();
            return mergeList;
    }
}
    private List<String> mergeList(List<String> a,List<String> b){
        for (String value:b){
            if(!a.contains(value))
                a.add(value);
        }
        return  a;
    }
    private int channgeTick(){
        int oldValue;
        int newValue;
        do{
            oldValue=this.tick.get();
            newValue=oldValue+1;
        }while(!this.tick.compareAndSet(oldValue,newValue));
        return newValue;
    }
    public boolean sendMessagePM(String name,String nameUserGetMsg,String msg){
        synchronized (pmMsgLock){
            User userSent=this.users.get(name);
            User userGetTheMsg=this.users.get(nameUserGetMsg);
            if (userSent==null || userSent.isLogin()==false || userGetTheMsg==null)
                return false;
            this.messagePM.add(msg);
            return true;
        }
    }
    public ConcurrentLinkedQueue<String> getUserNameListRegister(String name){
        synchronized (registerLock){
            ConcurrentLinkedQueue<String> result=new ConcurrentLinkedQueue<>();
            User user=this.users.get(name);
            if(user!=null || user.isLogin()==false)
                return result;
            result.addAll(this.users.keySet());
            return result;
        }
    }
    public String getStatUser(String name, String nameUserStat){
        User user=this.users.get(name);
        User userStat=this.users.get(nameUserStat);
        String result = "";
        if (user==null || user.isLogin()==false || userStat==null)
            return "";
        String delimeter = "\0";//between all name;
        result=result+userStat.getNumberOfPost()+delimeter+userStat.getNumbetOfFollowers()+delimeter+userStat.getNumberUsersTheUserIsFollowing();
        return  result;
    }
}
