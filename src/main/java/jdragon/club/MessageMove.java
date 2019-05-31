package jdragon.club;

import Tool.RegexText;
import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventGroupMessage;

public class MessageMove  extends IcqListener {
    boolean open = false;
    @EventHandler
    public void Carry(EventGroupMessage event)
    {
        String message = event.getMessage();
        Long GroupID = RegexText.getGroupID(event.toString());
        Long ID = event.getSenderId();
        if(message.equals("开启转发")) {
            if(ID==1061917196L) {
                open = true;
                event.respond("小鹤双拼练习一群，二群聊天转发开启");
            }else
                event.respond("无操作权限");
            return ;
        } else if (message.equals("关闭转发")) {
            if(ID==1061917196L) {
                open = false;
                event.respond("小鹤双拼练习一群，二群聊天转发关闭");
            }else
                event.respond("无操作权限");
            return ;
        }
        String returnMes = ID+"\n"+message;
        String temp = "";

        if(message.length()>5)
            temp = message.substring(0,4);
        if(open&&ID!=207938707L&&!temp.equals("第999段")) {
            if (GroupID == 522394334L)
                event.getHttpApi().sendGroupMsg(723795668L, returnMes);
            else if (GroupID == 723795668L)
                event.getHttpApi().sendGroupMsg(522394334L, returnMes);
        }
    }
}
