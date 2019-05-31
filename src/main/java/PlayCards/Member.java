package PlayCards;

import java.util.ArrayList;
import java.util.List;

class Member {
    private String type;
    private String idcard;
    private Long memeberID;
    private int roomid;
    private int fraction = 0;//得分
    private boolean ready = false;
    private boolean Landlord = false;//地主
    private List<Integer> handcards = new ArrayList<Integer>();
    boolean isLandlord() {
        return Landlord;
    }
    void setLandlord(boolean landlord) {
        Landlord = landlord;
    }
    List<Integer> getHandcards() {
        return handcards;
    }
    void setHandcards(List<Integer> handcards) {
        this.handcards = handcards;
    }
    int getRoomid() {
        return roomid;
    }
    void setRoomid(int roomid) {
        this.roomid = roomid;
    }
    String getType() {
        return type;
    }
     void setType(String type) {
        this.type = type;
    }
    String getIdcard() {
        return idcard;
    }
    void setIdcard(String idcard) {
        this.idcard = idcard;
    }
    public Long getMemeberID() {
        return memeberID;
    }
    void setMemeberID(long memeberID) {
        this.memeberID = memeberID;
    }
    int getFraction() {
        return fraction;
    }
    void setFraction(int fraction) {
        this.fraction = fraction;
    }
    boolean isReady() {
        return ready;
    }
    void setReadysign() {
        if(ready)
            ready = false;
        else
            ready = true;
    }
}
