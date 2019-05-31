package PlayCards;
import java.util.HashMap;
import java.util.Map;

class Room {
    private Long RoomID;
    private Map<Long,Member> memberList = new HashMap<Long,Member>();//该房间号对应成员
    private Long Landlord;


    Long getLandlord() {
        return Landlord;
    }

    void setLandlord(Long landlord) {
        Landlord = landlord;
    }


    Map<Long,Member>  getMemberList() {
        return memberList;
    }
    void setMemberList(Map<Long,Member>  memberList) {
        this.memberList = memberList;
    }
    Long getRoomID() {
        return RoomID;
    }
    void setRoomID(Long roomID) {
        RoomID = roomID;
    }
    void addMemeber(Long id, Member member){
        memberList.put(id,member);
    }
    void removeMemeber(Long id){
        memberList.remove(id);
    }
}
