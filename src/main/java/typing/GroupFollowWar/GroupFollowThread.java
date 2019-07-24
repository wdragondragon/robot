package typing.GroupFollowWar;

import Tool.SortMap;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import jdragon.club.cantoon.RamdomLoad;
import java.util.HashMap;
import java.util.Map;

public class GroupFollowThread extends  Thread {
    int duan = 1;
    int length;
    Long groupid;
    EventGroupMessage event;
    boolean start = false;
    Map<Long,Integer> IDlist = new HashMap<Long, Integer>();
    Map<Long, Double> IDspendlist = new HashMap<Long,Double>();
    Map<Long,String> IDName = new HashMap<Long, String>();
    Map<Long,Double> IDletspeed = new HashMap<Long, Double>();
    String art = "";

    public GroupFollowThread(EventGroupMessage event, int length, Long groupid){
        this.event = event;
        this.length = length;
        this.groupid = groupid;
    }
    public void nextDuan(){
        this.duan++;
        String message = "临时成绩\n";
        message += SortMap.SendsortValueFollow(IDlist,IDspendlist,IDName);
        event.respond(message);
        send();
        resert();
    }
    public void send(){
        String message = "";
        while(art.length()<duan*Integer.valueOf(length))
            art+=RamdomLoad.getRamdomWenben();
        message += art.substring((duan-1)*length,duan*length);
        message = "随机混战\n" + message + "\n-----第"+duan+"段-每段"+length+"字";
        event.respond(message);
    }
    public void resert(){
        for(Long k:IDspendlist.keySet()) {
            IDspendlist.put(k, 0.0);
        }
    }
    public void addID(Long id,String name){
        IDlist.put(id,0);
        IDspendlist.put(id,0.0);
        IDName.put(id,name);
        IDletspeed.put(id,1.0);
    }
    public void removeID(Long id){
        IDlist.remove(id);
        IDspendlist.remove(id);
        IDName.remove(id);
        IDletspeed.remove(id);
    }
    public Map<Long,Integer> getIDlist(){
        return IDlist;
    }
    public int getDuan(){
        return duan;
    }
    public double getIDspend(Long id){
        return IDspendlist.get(id);
    }
    public void setIDspend(Long id,double speed) {
        IDspendlist.put(id,speed);
    }
    public Map<Long,Double> getIDspendlist(){
        return IDspendlist;
    }
    public void setStartSign(boolean start) {
        this.start = start;
    }
    public boolean getStartSign(){
        return start;
    }
    public Map<Long,String> getIDnamelist(){
        return  IDName;
    }
    public Double getLetSpeed(Long id){return IDletspeed.get(id);}
    public void setLetSpeed(Long id,Double speed){
        IDletspeed.put(id,speed);
    }
}
