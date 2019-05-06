package jdragon.club;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventPrivateMessage;

public class TestListener extends IcqListener
{
    @EventHandler
    public void onPMEvent(EventPrivateMessage event)
    {
        System.out.println("接到消息");

        // 判断消息是不是这段文字, 如果是就回复那段文字, 很简单的测试_(:з」∠)_
        if (event.getMessage().equals("你以为这是 yangjinhe/maintain-robot?"))
            event.respond("其实是我 HyDevelop/PicqBotX 哒!");
    }
}