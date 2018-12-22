package bgu.spl.net.accessories;

import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

    private String name;
    private String password;
    private boolean login;
    private ConcurrentLinkedQueue<String> followList;
    private int timestamp;
    private int numberOfPost;
    private int numbetOfFollowers;

    public User(String name,String password){
        this.name=name;
        this.password=password;
        login=false;
        followList=new ConcurrentLinkedQueue();
        timestamp=0;
        this.numberOfPost=numberOfPost;
        numbetOfFollowers=0;
        numbetOfFollowers=0;
    }

    public int getNumberOfPost() {
        return numberOfPost;
    }

    public void setNumberOfPost(int numberOfPost) {
        this.numberOfPost = numberOfPost;
    }
    public int getNumberUsersTheUserIsFollowing(){
        return this.followList.size();
    }

    public int getNumbetOfFollowers() {
        return numbetOfFollowers;
    }

    public void setFollowList(ConcurrentLinkedQueue<String> followList) {
        this.followList = followList;
    }

    public void setNumbetOfFollowers(int numbetOfFollowers) {
        this.numbetOfFollowers = numbetOfFollowers;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean login(String pass) {
        if(this.password.equals(pass)) {
            timestamp = Integer.MAX_VALUE;
            this.login = true;
            return true;
        }
        else
            return false;
    }
    public void logout(int tick){
        this.login=false;
        timestamp=tick;
    }

    public ConcurrentLinkedQueue<String> getFollowList() {
        return followList;
    }

    public boolean isLogin() {
        return login;
    }

    public int getTickLogOut() {
        return timestamp;
    }
    public boolean areYouFollow(String name){
        if(followList.contains(name))
            return true;
        else
            return false;
    }
    public void addFollow(String name){
        this.followList.add(name);
    }
    public void unFollow(String name){
        this.followList.remove(name);
    }
    public void addFollowers(){
        this.numbetOfFollowers++;
    }
    public void lessFollowers(){
        this.numbetOfFollowers--;
    }
    public void addNumberOfPost(){
        this.numberOfPost++;
    }
}
