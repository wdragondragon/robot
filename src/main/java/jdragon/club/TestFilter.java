package jdragon.club;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.local.EventLocalSendMessage;

public class TestFilter extends IcqListener
{
    @EventHandler
    public void onAllLocalMessageEvent(EventLocalSendMessage event) // 监听所有发送消息的事件
    {
        // 获取消息
        String message = event.getMessage();
        System.out.println(message);
        // 这里可以做任何处理
        // 我把所有"%prefix%"变量替换成了"!"
        message = message.replace("%prefix%", "!");

        // 设置消息, 因为这个事件是在发送之前执行的, 所以这样设置的消息能生效
        // 设置为 null 就能拦截了
        event.setMessage(message);
    }
}