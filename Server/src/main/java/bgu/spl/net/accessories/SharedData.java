package bgu.spl.net.accessories;

import bgu.spl.net.Messages.Message;
import bgu.spl.net.Messages.NotificationMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedData {

        private ConcurrentHashMap<Integer, NotificationMessage> messagePostAndPM;
        private ConcurrentHashMap<String,User> users;
        private AtomicInteger tick;
        private Object postMsgLock=new Object();
        private Object pmMsgLock=new Object();
        private Object registerLock=new Object();

    /**
     * constructor
     */
    public SharedData(){
        messagePostAndPM=new ConcurrentHashMap<>();
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

    /**
     * change the status of the user to login
     * @param name
     * @param password
     * @return
     */
    public boolean login(String name,String password){
        synchronized (registerLock) {
            User user = users.get(name);
            if (user == null || user.isLogin())
                return false;
            else if (user.login(password))
                return true;
            else
                return false;
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean logout(String name){
        synchronized (postMsgLock) {
            if (name != null) {
                User user = users.get(name);
                if (user != null && user.isLogin()) {
                    user.logout(tick.get());
                    return true;
                } else
                    return false;
            }
            return false;
        }
    }

    /**
     * follow after user from the list
     * @param listUsers
     * @param name
     * @return
     */
    public List<String> followUser(List<String> listUsers, String name){
        synchronized (registerLock) {
        User user=users.get(name);//the user that want do follow
        List<String> result=new ArrayList<>();//the result that return
        if(user==null || user.isLogin()==false)// if the user is not exist or is logout
            return result;
        else {
            for (String key : listUsers) {
                User followUser = users.get(key);
                if (followUser == null)// if the user from the list is not exist
                    continue;
                else {
                    if (user.areYouFollow(key))//if the user is already follow this user from the list
                        continue;
                    else {
                        user.addFollow(key);//follow after the user from the list that calls key
                        result.add(key);//add to the list that return when end pass all the list that get
                        followUser.addFollowers();//add to the user calls key that he has a new follower;
                    }
                }
            }
            return result;
        }
        }
    }

    /**
     * unFollow after the user that find in the list
     * @param listUsers
     * @param name
     * @return
     */
    public  List<String> unFollowUser(List<String> listUsers, String name){
        synchronized (registerLock) {
            User user=users.get(name);//the user that want do un-follow
        List<String> result=new ArrayList<>();//the result that return
        if(user==null || user.isLogin()==false)// if the user is not exist or is logout
            return result;
        else {
            for (String key : listUsers) {
                User followUser = users.get(key);
                if (followUser == null)// if the user from the list is not exist
                    continue;
                else {
                    if (!user.areYouFollow(key))//if the user is not follow after this user from the list
                        continue;
                    else {
                        user.unFollow(key);//follow after the user from the list that calls key
                        result.add(key);//add to the list that return when end pass all the list that get
                        followUser.lessFollowers();//add to the user calls key that he has a new follower;
                    }
                }
            }
            return result;
        }
        }
    }

    /**
     * add Notifaction  message to the list
     * @param notificationMessage
     */
    public void addNotifactionToMessages(Message notificationMessage){
        this.messagePostAndPM.put(channgeTick(),(NotificationMessage)notificationMessage);

    }

    /**
     * get message that don't send to user
     * @param name
     * @return
     */
    public List<Message> getMesageThatDontSendToUser(String name){
        List<Message> result = new ArrayList<>();
        synchronized (postMsgLock) {
            int tickCurrnt = this.tick.get();
            int tickLastOfUser = this.users.get(name).getTickLogOut();
            while (tickCurrnt <= tickLastOfUser) {
                NotificationMessage notificationMessage = this.messagePostAndPM.get(tickLastOfUser);
                if (notificationMessage != null && notificationMessage.checkIfFindInTheListOfUsers(name))
                    result.add(notificationMessage);
                tickLastOfUser++;
            }
        }
        return result;
    }

    /**
     * send post message
     * @param name
     * @param list
     * @param msg
     * @return
     */
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

    /**
     * merge two list
     * @param a
     * @param b
     * @return
     */
    private List<String> mergeList(List<String> a,List<String> b){
        for (String value:b){
            if(!a.contains(value))
                a.add(value);
        }
        return  a;
    }

    /**
     * change the tick of the message use for post message
     * @return
     */
    private int channgeTick(){
        int oldValue;
        int newValue;
        do{
            oldValue=this.tick.get();
            newValue=oldValue+1;
        }while(!this.tick.compareAndSet(oldValue,newValue));
        return newValue;
    }

    /**
     * send message pm
     * @param name
     * @param nameUserGetMsg
     * @param msg
     * @return
     */
    public boolean sendMessagePM(String name,String nameUserGetMsg,String msg){
        synchronized (postMsgLock){
            User userSent=this.users.get(name);
            User userGetTheMsg=this.users.get(nameUserGetMsg);
            if (userSent==null || userSent.isLogin()==false || userGetTheMsg==null)
                return false;
            return true;
        }
    }

    /**
     * get list of users that register
     * @param name
     * @return
     */
    public List<String> getUserNameListRegister(String name){
        synchronized (registerLock){
            List<String> result=new ArrayList<>();
            User user=this.users.get(name);
            if(user==null || user.isLogin()==false)
                return result;
            result.addAll(this.users.keySet());
            return result;
        }
    }

    /**
     * return stat of user
     * @param name
     * @return
     */
    public short[] getStatUser(String name,String nameOfStat){
        synchronized (registerLock) {
            User user = this.users.get(name);
            User userStat = this.users.get(nameOfStat);
            if (user == null || user.isLogin() == false || userStat == null)
                return null;
            short[] result = new short[3];
            result[0] = (short) userStat.getNumberOfPost();
            result[1] = (short) userStat.getNumberOfFollowers();
            result[2] = (short) userStat.getNumberUsersTheUserIsFollowing();
            return result;
        }
    }
}
