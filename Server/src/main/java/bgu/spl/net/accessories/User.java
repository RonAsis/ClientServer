package bgu.spl.net.accessories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class User {

    private String name;
    private String password;
    private boolean login;
    private List<String> Followers;
    private int timestamp;
    private int numberOfPost;
    private AtomicInteger NumFollowing;

    /**
     * constructor
     * @param name
     * @param password
     */
    public User(String name,String password){
        this.name=name;
        this.password=password;
        login=false;
        Followers =new ArrayList<>();
        timestamp=0;
        this.numberOfPost=0;
        NumFollowing =new AtomicInteger();
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
        return this.Followers.size();
    }

    /**
     * return the number of followers
     * @return
     */
    public int getNumFollowing() {
        return NumFollowing.get();
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
    public List<String> getFollowers() {
        return Followers;
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
    public boolean areYouFollowBy(String name){
        if(Followers.contains(name))
            return true;
        else
            return false;
    }
    /**
     * add user to the list of follow
     */
    public void addFollow(String name){

        this.Followers.add(name);
    }

    /**
     * remove user from the list of follow
     * @param name
     */
    public void unFollow(String name){
        this.Followers.remove(name);
        System.out.println();
    }

    /**
     * add more followers to the sum
     */
    public void addFollowing(){
        Integer oldValue;
        Integer newValue;
        do{
            oldValue=this.NumFollowing.get();
            newValue=oldValue+1;
        }while (!this.NumFollowing.compareAndSet(oldValue,newValue));
    }

    /**
     * less follow
     * the sum of followers
     */
    public void lessFollowing(){
        Integer oldValue;
        Integer newValue;
        do{
            oldValue=this.NumFollowing.get();
            newValue=oldValue-1;
        }while (!this.NumFollowing.compareAndSet(oldValue,newValue));
    }

    /**
     * add number of post ,when this user is send message
     */
    public void addNumberOfPost(){
        this.numberOfPost++;
    }
}
