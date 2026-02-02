package com.ricci.springframework.test.bean;

public class UserService {

    private String uID;

    private UserDao userDao;

    public UserService() {
    }

    public void queryUserInfo() {
        System.out.println("查询用户信息：" + userDao.queryUserName(uID));
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("").append(uID);
        return sb.toString();
    }
}
