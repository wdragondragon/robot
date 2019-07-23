package game.P2P;

import cc.moecraft.icq.sender.IcqHttpApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class controller {
    Adapter adapter;
    IcqHttpApi httpAPI;
    List<String> roommember = new ArrayList<String>();
    HashMap<Integer, List<String>> roommap = new HashMap<Integer, List<String>>();
    HashMap<String,User_info> member = new HashMap<String, User_info>();
    int roomnum = 1;
    controller(IcqHttpApi httpApi){
        this.httpAPI = httpApi;
        adapter = new Adapter(httpApi);
    }
    public void handleMsg(String message,
                          User_info user_info){
        String id = user_info.getQQnumber();
        if(message.equals("#匹配")){
            if (!member.containsKey(id)){
                if(roommember.size()<2){
                    user_info.setRoomnumber(roomnum);
                    member.put(id,
                            user_info);//成员添加
                    roommember.add(id);//临时匹配房间成员添加
                    adapter.SendMsgToID("正在匹配",user_info);
                }
                if(roommember.size()==2){//判断是否满人
                    roommap.put(roomnum,roommember);//房间满人，放入房间map
                    roomnum++;//房间号自增
                    for (String aLong : roommember) {
                        adapter.SendMsgToID("已配对",member.get(aLong));
                    }
                    roommember = new ArrayList<String>();//创建新的临时匹配房间
                }
            }
        }else if(message.equals("#退出")){
            if(member.containsKey(id)){
                int thisQQroom = member.get(id).getRoomnumber();//获取用户所在的房间号
                System.out.println("退出房间"+thisQQroom);
                List<String> roommember1 = roommap.get(thisQQroom);//通过房间号获取房间成员
                for (String o : roommember1) {//移除房间成员
                    adapter.SendMsgToID("房间中有一人退出，房间关闭",member.get(o));
                    member.remove(o);
                }
                roommap.remove(thisQQroom);//移除房间
            }
            roommember.remove(id);//临时移除
        }else if(member.containsKey(id)){
            int thisQQroom = member.get(id).getRoomnumber();//获取用户所在的房间号
            List<String> roommember1 = roommap.get(thisQQroom);//通过房间号获取房间成员
            for (String aLong : roommember1) {
                if(!aLong.equals(id)){
                    adapter.SendMsgToID(message,member.get(aLong));
                }
            }
        }
        System.out.println(roommember);
        System.out.println(member);
        System.out.println(roommap);
    }
}
