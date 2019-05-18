package GroupWar;

import cc.moecraft.icq.event.events.message.EventGroupMessage;
import jdragon.club.RamdomLoad;
import Tool.SortMap;

import java.util.HashMap;
import java.util.Map;

public class GroupThread extends Thread {
    Map<Long,Integer> IDlist = new HashMap<Long, Integer>();
    boolean start = false;  //ս��������־��ֹ�ظ�����
    int length,onelength,time,nowtime = 1;
    public String message = "";
    Long groupid;
    EventGroupMessage event;
    public GroupThread(int length, int onelength, int time, EventGroupMessage event,Long groupid){
        this.length = length;
        this.onelength = onelength;
        this.time = time;
        this.event = event;
        this.groupid = groupid;
    }
    public void run(){
        //��ȡ����
        String art = "";
        boolean setsign = false;//���÷������ı�־
        Count count = new Count(time);//����ʱ��
        count.start();
        while(true) {
            try {
                sleep(100);
                if(start) {
                    art = RamdomLoad.getRamdomWenben();
                    while(art.length()<Integer.valueOf(length))
                        art+=RamdomLoad.getRamdomWenben();
                    System.out.println(art);
                    count.setRunSign(true);
                }
                while (start) {
                    try {
                        sleep(1);
                        if (count.getTime() == time) {
                            count.setTime(0);
                            nowtime++;
                            System.out.println("��һ��");
                            setsign = false;
                        }
                        if (count.getTime() == 0) {
                            if (!setsign) { //��ֹ����0ʱ�ظ���������
                                for (Long k : IDlist.keySet()) {
                                    if (nowtime * onelength < length)
                                        message = art.substring((nowtime - 1) * onelength, nowtime * onelength);
                                    else if ((nowtime - 1) * onelength < length)
                                        message = art.substring((nowtime - 1) * onelength, length);
                                    else if (nowtime * onelength > length)
                                        start = false;
                                    if (start) {
                                        event.getHttpApi().sendPrivateMsg(k, message);
                                        System.out.println(nowtime+" "+message);
                                        setsign = true;
                                    }
                                }
                                if (!start) {
                                    count.setRunSign(false);
                                    String chengji = "";
                                    chengji += SortMap.SendsortValue(IDlist)+"���ѷ��գ�ս���ر�";
                                    event.getHttpApi().sendGroupMsg(groupid, chengji);
                                }

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){}
        }
    }
    public Map<Long,Integer> getIDlist(){
        return IDlist;
    }
    public void addID(Long id){
        IDlist.put(id,0);
    }
    public boolean getStartSign(){
        return  start;
    }
    public void setStartSign(boolean sign){
        this.start = sign;
        nowtime = 1;
    }
    public void removeID(Long id){
        IDlist.remove(id);
    }
}
