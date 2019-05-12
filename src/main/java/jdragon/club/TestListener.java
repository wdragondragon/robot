package jdragon.club;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventPrivateMessage;

public class TestListener extends IcqListener
{
    @EventHandler
    public void onPMEvent(EventPrivateMessage event)
    {
        String message,fuhao;
        message = event.getMessage();
        fuhao = message.substring(message.length()-2);
        message = replaceYouMe(message);
        if(fuhao.equals("吗？")||fuhao.equals("啊？")||fuhao.equals("呢？")||fuhao.equals("吧？")) {
            event.respond(message.substring(0,message.length()-2)+"！");
            return;
        }
        fuhao = message.substring(message.length()-1);
        // 判断消息是不是这段文字, 如果是就回复那段文字, 很简单的测试_(:з」∠)_
        if(fuhao.equals("?")||fuhao.equals("？")) {
            event.respond(message.substring(0,message.length()-1)+"！");
            return;
        }
        event.respond(message);
    }
    String  replaceYouMe(String message){
        message = message.replace("我","&");
        message = message.replace("你","我");
        message = message.replace("&","你");
        return message;
    }
}