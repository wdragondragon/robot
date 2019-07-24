package typing.GroupFollowTeamWar;

import typing.Tools.SortMap;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import Cantoon.RamdomLoad;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupFollowTeamThread {
    EventGroupMessage event;
    int duan = 1;
    int length;
    String art = "";
    boolean start = false;
    Map<Long,Double> speedlist = new HashMap<Long, Double>();
    Map<Integer,List<Long>> Member = new HashMap<Integer, List<Long>>();
    Map<Integer,Integer> math = new HashMap<Integer, Integer>();
    Map<Integer,Double> avermath = new HashMap<Integer, Double>();
    Map<Long,String> namelist = new HashMap<Long, String>();
    public GroupFollowTeamThread(EventGroupMessage event,int length){
        this.event = event;
        this.length = length;
        init();
    }
    public void init(){
        Member.put(1,new ArrayList<Long>());
        Member.put(2,new ArrayList<Long>());
        avermath.put(1,0.0);
        avermath.put(2,0.0);
        math.put(1,0);
        math.put(2,0);
    }
    public void resert(){
        for(Long k:speedlist.keySet()) {
            speedlist.put(k, 0.0);
        }
        for(Integer k:avermath.keySet()) {
            avermath.put(k, 0.0);
        }
    }
    public void nextDuan(){
        String message = "临时成绩\n";
        averspeed();
        message += SortMap.SendsortValueTeamOneSpeed(speedlist,namelist);
        message += SortMap.SendsortValueTeamSpeed(avermath,math);
        event.respond(message);
        this.duan++;
        send();
        resert();

    }
    public void averspeed(){
        for(int i =1 ;i<=Member.size();i++){
            int membernum = Member.get(i).size();
            System.out.println(i+"队人数："+membernum);
            Double averspeed = 0.0;
            for( int j = 0;j<membernum;j++) {
                averspeed += speedlist.get(Member.get(i).get(j));
                System.out.println(Member.get(i).get(j));
                System.out.println(speedlist.get(Member.get(i).get(j)));
            }
            averspeed = averspeed/membernum;
            avermath.put(i,averspeed);
        }
    }
    public void send(){
        String message = "";
        while(art.length()<duan*Integer.valueOf(length))
            art+= RamdomLoad.getRamdomWenben();
        message += art.substring((duan-1)*length,duan*length);
        message = "随机团战\n" + message + "\n-----第"+duan+"段-每段"+length+"字";
        event.respond(message);
    }
    public int isEmpty(Long id){
        for(int i =1;i<=Member.size();i++){
            if(Member.get(i).contains(id)){
                return i;
            }
        }
        return -1;
    }
    public void addID(int GroupNum,Long id,String name){
        String at = "[CQ:at,qq="+id+"]\n";
        int i = isEmpty(id);
        if(i!=-1)
            event.respond(at+"你已加入了"+(i)+"队，请勿重复加入队伍");
        else {
            Member.get(GroupNum).add(id);
            speedlist.put(id, 0.0);
            namelist.put(id,name);
            event.respond(at + "加入成功");
        }
    }
    public void removeID(Long id){
        String at = "[CQ:at,qq="+id+"]\n";
        int i = isEmpty(id);
        if(i!=-1) {
            Member.get(i).remove(id);
            speedlist.remove(id);
            namelist.remove(id);
            event.respond(at + "已退出" + (i) + "队");
        }
        else
            event.respond(at + "你未曾加入何人队伍");
    }
    public Map<Integer,Integer> getMath(){
        return math;
    }
    public boolean getStartSign(){
        return  start;
    }
    public void setStartSign(boolean start){
        this.start = start;
    }
    public int getDuan(){
        return duan;
    }
    public void setIDspend(Long id,Double grade){
        speedlist.put(id,grade);
    }
    public Map<Long,Double> getSpeedlist(){
        return  speedlist;
    }
    public Map<Integer,List<Long>> getMember(){
        return Member;
    }
}
