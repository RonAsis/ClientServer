package bgu.spl.net.accessories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class User {

    private String name;
    private String password;
    private boolean login;
    private List<String> followList;
    private int timestamp;
    private int numberOfPost;
    private AtomicInteger numberOfFollowers;

    /**
     * constructor
     * @param name
     * @param password
     */
    public User(String name,String password){
        this.name=name;
        this.password=password;
        login=false;
        followList=new ArrayList<>();
        timestamp=0;
        this.numberOfPost=0;
        numberOfFollowers =new AtomicInteger();
    }

    /**
     * return number of post
     * @return
     */
    public int getNumberOfPost() {
        return numberOfPost;
    }

    /**
     * get the number of follow
     * @return
     */
    public int getNumberUsersTheUserIsFollowing(){
        return this.followList.size();
    }

    /**
     * return the number of followers
     * @return
     */
    public int getNumberOfFollowers() {
        return numberOfFollowers.get();
    }


    public String getName() {
        return name;
    }

    /**
     * login to the user
     * @param pass for to login the user
     * @return
     */
    public boolean login(String pass) {
        if(this.password.equals(pass)) {
            this.login = true;
            return true;
        }
        else
            return false;
    }

    /**
     * the user is know logout
     * @param tick
     */
    public void logout(int tick){
        this.login=false;
        timestamp=tick;
    }

    /**
     * retun the list of follow
     * @return
     */
    public List<String> getFollowList() {
        return followList;
    }

    /**
     * return is this is user is login
     * @return
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * return the timeStamp this for know when the user was logout
     * @return
     */
    public int getTickLogOut() {
        return timestamp;
    }

    /**
     * check if this user follow after another user
     * @param name the name of user that want check if he is in the list of follow
     * @return
     */
    public boolean areYouFollow(String name){
        if(followList.contains(name))
            return true;
        else
            return false;
    }
    /**
     * add user to the list of follow
     */
    public void addFollow(String name){

        this.followList.add(name);
    }

    /**
     * remove user from the list of follow
     * @param name
     */
    public void unFollow(String name){
        this.followList.remove(name);
    }

    /**
     * add more followers to the sum
     */
    public void addFollowers(){
        Integer oldValue;
        Integer newValue;
        do{
            oldValue=this.numberOfFollowers.get();
            newValue=oldValue+1;
        }while (!this.numberOfFollowers.compareAndSet(oldValue,newValue));
    }

    /**
     * less follow from the sum of followers
     */
    public void lessFollowers(){
        Integer oldValue;
        Integer newValue;
        do{
            oldValue=this.numberOfFollowers.get();
            newValue=oldValue-1;
        }while (!this.numberOfFollowers.compareAndSet(oldValue,newValue));
    }

    /**
     * add number of post ,when this user is send message
     */
    public void addNumberOfPost(){
        this.numberOfPost++;
    }
}
