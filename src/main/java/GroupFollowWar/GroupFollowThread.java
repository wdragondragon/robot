package GroupFollowWar;

import cc.moecraft.icq.event.events.message.EventGroupMessage;


import java.util.HashMap;
import java.util.Map;

public class GroupFollowThread extends  Thread {
    int duan = 1;
    int length;
    Long groupid;
    EventGroupMessage event;
    boolean start = false;
    public void setStartSign(boolean strat) {
        this.start = strat;
    }
    public boolean getStartSign(){
        return start;
    }

    Map<Long,Integer> IDlist = new HashMap<Long, Integer>();
    Map<Long,Double> IDspend = new HashMap<Long, Double>();
    public GroupFollowThread(EventGroupMessage event, int length, Long groupid){
        this.event = event;
        this.length = length;
        this.groupid = groupid;
    }
    public void run(){

    }
    public void addID(Long id){
        IDlist.put(id,0);
        IDspend.put(id,0.0);
    }
    public void removeID(Long id){
        IDlist.remove(id);
        IDspend.remove(id);
    }
    public Map<Long,Integer> getIDlist(){
        return IDlist;
    }
    public Map<Long,Double> getIDspeed(){
        return IDspend;
    }

}
