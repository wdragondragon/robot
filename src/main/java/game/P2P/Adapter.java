package game.P2P;

import cc.moecraft.icq.sender.IcqHttpApi;
import cn.zhouyafeng.itchat4j.api.MessageTools;


public class Adapter {
    IcqHttpApi httpAPI;
    Adapter(IcqHttpApi httpAPI){
        this.httpAPI = httpAPI;
    }
    public void SendMsgToID(String message,
                        User_info user_info){
        String type = user_info.getType();
        String id = user_info.getQQnumber();
        if(type.equals("QQ")){
            httpAPI.sendPrivateMsg(Long.valueOf(id),message);
        }else if(type.equals("WX")){
            MessageTools.sendMsgById(message,id);
        }
    }
}
