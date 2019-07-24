package game.P2P;

import cc.moecraft.icq.sender.IcqHttpApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Controller {
    private Adapter adapter;//适配器对象
    private List<String> roommember = new ArrayList<String>();//临时匹配信息
    private HashMap<Integer, List<String>> roommap = new HashMap<Integer, List<String>>();//全部房间信息
    private HashMap<String,User_info> member = new HashMap<String, User_info>();//全部用户信息
    private int roomnum = 1;
    Controller(IcqHttpApi httpApi){
        adapter = new Adapter(httpApi);//创建适配器来控制发送给QQ还是WX
    }
    void handleMsg(String message,
                   User_info user_info){
        try {
            String id = user_info.getQQnumber();
            String type = user_info.getType();
            if (!member.containsKey(id)) {
                if (message.equals("#匹配")) {
                    //该用户不在成员中且发送匹配字段时添加用户操作
                    //成员信息中设置房间号
                    user_info.setRoomnumber(roomnum);
                    //成员添加
                    member.put(id, user_info);
                    //临时匹配房间成员添加
                    roommember.add(id);
                    adapter.SendMsgToID("正在匹配", id, type);
                    //判断是否满人
                    if (roommember.size() == 2) {
                        //房间满人，放入房间map
                        roommap.put(roomnum, roommember);
                        roomnum++;
                        try {
                            for (int i = 0; i < roommember.size(); i++) {
                                String sendtoid = roommember.get(i);
                                String totype = member.get(sendtoid).getType();
                                String linkfromid = roommember.get((i + 1) % 2);
                                String sendtype = member.get(linkfromid).getType();
                                adapter.SendMsgToID("已配对，对方使用" + sendtype, sendtoid, totype);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //创建新的临时匹配房间，这时roommember.size清零
                        roommember = new ArrayList<String>();
                    }
                }
            } else if (member.containsKey(id)) {
                //获取用户所在的房间号
                int thisQQroom = member.get(id).getRoomnumber();
                //通过房间号获取房间成员
                List<String> roommember1 = roommap.get(thisQQroom);
                if (message.equals("#退出")) {
                    for (String sendtoid : roommember1) {//移除房间成员
                        adapter.SendMsgToID("房间中有一人退出，房间关闭", sendtoid, type);
                        member.remove(sendtoid);
                    }
                    roommap.remove(thisQQroom);//移除房间
                    roommember.remove(id);//临时移除
                } else {
                    for (String sendtoid : roommember1) {
                        if (!sendtoid.equals(id))
                            adapter.SendMsgToID(message, sendtoid, member.get(sendtoid).getType());
                    }
                }
            }
            System.out.println(roommember);
            System.out.println(roommap);
            System.out.println(member);
        }catch (Exception e){e.printStackTrace();}
    }
    void WXhandleFileMsg(String message,
                         User_info user_info,
                         String filepath){
        String id = user_info.getQQnumber();
        String type = user_info.getType();
        if(member.containsKey(id)) {
            //获取用户所在的房间号
            int thisQQroom = member.get(id).getRoomnumber();
            //通过房间号获取房间成员
            List<String> roommember1 = roommap.get(thisQQroom);
            if (message.equals("#图片信息")) {
                for (String sendtoid : roommember1) {
                    if (!sendtoid.equals(id)) {
                        adapter.SendWXPicMsgToID(filepath, sendtoid, member.get(sendtoid).getType());
                    }
                }
            } else if (message.equals("#其他信息")) {
                for (String sendtoid : roommember1){
                    if(!sendtoid.equals(id)){
                        adapter.SendWXFileMsgToID(filepath,sendtoid,member.get(sendtoid).getType());
                    }
                }
            }else if(message.equals("#语音信息")){
                for (String sendtoid : roommember1){
                    if(!sendtoid.equals(id)){
                        adapter.SendWXVoiceToID(filepath,sendtoid,member.get(sendtoid).getType());
                    }
                }
            }
        }
    }
}
