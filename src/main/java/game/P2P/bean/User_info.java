package game.P2P.bean;

public class User_info {
    private String username;
    private String sex;
    private String QQnumber;
    private String type;
    private int roomnumber;
    private long age;
    User_info(String QQnumber, String username){
        this.QQnumber = QQnumber;
        this.username = username;
    }
    public User_info sex(String sex){
        this.sex = sex;
        return this;
    }
    public User_info type(String type){
        this.type = type;
        return this;
    }
    public User_info age(long age){
        this.age = age;
        return this;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getQQnumber() {
        return QQnumber;
    }
    public int getRoomnumber() {
        return roomnumber;
    }
    public void setRoomnumber(int roomnumber) {
        this.roomnumber = roomnumber;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }
}
