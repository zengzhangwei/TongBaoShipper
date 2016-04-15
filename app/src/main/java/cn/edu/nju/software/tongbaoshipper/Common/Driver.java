package cn.edu.nju.software.tongbaoshipper.Common;

public class Driver {

    /**
     * 司机id
     */
    private int id;
    /**
     * 司机昵称
     */
    private String nickName;
    /**
     * 司机手机号
     */
    private String phoneNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
