package game.PlayCards;

import cc.moecraft.icq.event.events.message.EventMessage;

import java.util.*;

import static java.util.Collections.shuffle;

public class CardClient {
    EventMessage event;
    int roomnum = 1;
    Map<Integer, Room> RoomList = new HashMap<Integer, Room>();
    Map<Long,Member> Allmember = new HashMap<Long, Member>(); //所有成员对应房间
    public CardClient(){
        initAllcards();
    }
    public void setEvent(EventMessage event) {
        this.event = event;
    }
    public int CreatRoom(Long id, String idcard, String type){    //创建房间
        if(Allmember.containsKey(id)){
            return -1;
        }
        else {
            Room room = new Room();
            Member member = Memeberinit(id,idcard,type,roomnum);//用户实例化，初始化
            room.addMemeber(id,member);
            Allmember.put(id,member);
            RoomList.put(roomnum, room);
            roomnum++;
            return roomnum-1;
        }
    }
    boolean JoinRoom(Long id,String idcard,String type,int roomid){     //加入房间
        if(Allmember.containsKey(id)||!RoomList.containsKey(roomid)){
            return false;
        }
        else{
            Member member = Memeberinit(id,idcard,type,roomid);
            RoomList.get(roomid).addMemeber(id,member);
            Allmember.put(id,member);
            return true;
        }
    }
    boolean OutRoom(Long id){
        if(!Allmember.containsKey(id)){
            return false;
        }
        else{
            int roomid = Allmember.get(id).getRoomid();
            Room room = RoomList.get(roomid);
            room.removeMemeber(id);
            Allmember.remove(id);
            if(isNull(roomid))
                RoomList.remove(roomid);
            return true;
        }
    }
    boolean ChangeReady(Long id){  //改变准备状态
        if(!Allmember.containsKey(id)){
            return false;
        }
        else{
            Allmember.get(id).setReadysign();
            return true;
        }
    }
    void init(int roomid){         //房间初始化,发牌
        shuffle(Allcards);
        int i = 0;
        Room room = RoomList.get(roomid);
        //先发牌每人17张
        for(Long k:room.getMemberList().keySet()){
            for(int j = 0;j<17;j++){
                room.getMemberList().get(k).getHandcards().add(Allcards.get(i));
                i++;
            }
        }
        //选择地主，发剩余三张
        Landlord(roomid);
    }
    void Landlord(int roomid){        //选择地主
        Random ra =new Random();
        int rondomnum = ra.nextInt(3);
        int i = 0;
        String lordcard = "";
        //随机地主
        Room room = RoomList.get(roomid);
        Map<Long,Member> memberList = room.getMemberList();
        //设置地主并给地主加3张牌
        for(Long k:memberList.keySet()){
            if(i==rondomnum) {
                memberList.get(k).setLandlord(true);
                room.setLandlord(k);
                for(int j = 51;j<54;j++) {
                    lordcard += NumToCard.get(Allcards.get(j));
                    memberList.get(k).getHandcards().add(Allcards.get(j));
                }
                break;
            }
            i++;
        }
        //排序,输出 手牌
        for(Long k:memberList.keySet()){
            List<Integer> handcards = memberList.get(k).getHandcards();
            Collections.sort(handcards);
            String handcard = "地主是"+room.getLandlord()+
                    "\n"+"地主牌是："+lordcard+"\n"+
                    "你的手牌：";
            for(Integer n:handcards)
                handcard += NumToCard.get(n);
            System.out.println(handcard);
            event.getHttpApi().sendPrivateMsg(k,handcard);//发送手牌
        }
    }
    void start(Long id){        //开始所在房间当局
        init(Allmember.get(id).getRoomid());
    }
    void end(int roomid){          //结束当局

    }
    //房间是否为空
    boolean isNull(int roomid){
        Room room = RoomList.get(roomid);
        if(room.getMemberList().size()==0)
            return true;
        else
            return false;
    }
    //是否满人
    boolean isFull(int roomid){
        Map<Long,Member> memberList = RoomList.get(roomid).getMemberList();
        if(memberList.size()>=3)
            return true;
        else
            return false;
    }
    //是否全部准备
    boolean isAllReady(Long id){
        boolean Ready = true;
        Map<Long,Member> memberList = RoomList.get(Allmember.get(id).getRoomid()).getMemberList();
        for(Long k:memberList.keySet()){
            if(!memberList.get(k).isReady()||memberList.size()<3){
                Ready = false;
                break;
            }
        }
        return Ready;
    }
    //用户初始化
    Member Memeberinit(Long id,String idcard,String type,int roomid){
        Member member = new Member();
        member.setIdcard(idcard);
        member.setMemeberID(id);
        member.setType(type);
        member.setRoomid(roomid);
        return member;
    }
    private List<Integer> Allcards = new ArrayList<Integer>();
    private Map<String,Integer> CardToNum = new HashMap<String, Integer>();
    private Map<Integer,String> NumToCard = new HashMap<Integer, String>();
    void initAllcards(){
        CardToNum.put("0",10);
        CardToNum.put("J",11);
        CardToNum.put("Q",12);
        CardToNum.put("K",13);
        CardToNum.put("A",14);
        CardToNum.put("2",15);
        CardToNum.put("z",16);
        CardToNum.put("Z",17);

        NumToCard.put(10,"0");
        NumToCard.put(11,"J");
        NumToCard.put(12,"Q");
        NumToCard.put(13,"K");
        NumToCard.put(14,"A");
        NumToCard.put(15,"2");
        NumToCard.put(16,"z");
        NumToCard.put(17,"Z");
        for(int i = 3;i<10;i++) {
            CardToNum.put(String.valueOf(i),i);
            NumToCard.put(i,String.valueOf(i));
        }
        for(int i = 3;i<=15;i++){
            for(int j = 0;j<4;j++)
                Allcards.add(i);
        }
        Allcards.add(16);
        Allcards.add(17);
        shuffle(Allcards);
    }
    //    public static void main(String args[]){
//        CardClient c = new CardClient();
//        c.CreatRoom(1L,"a","QQ");
//        if(c.JoinRoom(2L,"a","QQ",1)){
//            System.out.println("成功");
//        }
//        if(c.JoinRoom(3L,"a","QQ",1)){
//            System.out.println("成功");
//        }
//        c.start(1);
//    }
}
