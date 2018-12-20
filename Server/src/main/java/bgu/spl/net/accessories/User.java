package bgu.spl.net.accessories;

import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

    private String name;
    private String password;
    private boolean login;
    private ConcurrentLinkedQueue<User> followList;
    private int tickLogOut;

    public User(String name,String password){
        this.name=name;
        this.password=password;
        login=false;
        followList=new ConcurrentLinkedQueue();
        tickLogOut=0;
    }
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void Login() {
        tickLogOut=Integer.MAX_VALUE;
        this.login =true;
    }
    public void logout(int tick){
        this.login=false;
        tickLogOut=tick;
    }

    public ConcurrentLinkedQueue<User> getFollowList() {
        return followList;
    }

    public boolean isLogin() {
        return login;
    }

    public int getTickLogOut() {
        return tickLogOut;
    }

}
