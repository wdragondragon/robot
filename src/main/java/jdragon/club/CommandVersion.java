package jdragon.club;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.User;

import java.util.ArrayList;

public class CommandVersion implements EverywhereCommand // 实现EverywhereCommand就是无论私聊群聊还是讨论组都能收到的指令
{
    // 指令属性

    public CommandProperties properties()
    {
        // 这个括号里填指令名和其他名称, 指令名必须至少有一个
        // 这个的话, 用"!v", "!version", 和"!版本"都能触发指令 (感叹号为你设置的前缀, 不一定必须要感叹号)
        return new CommandProperties("version", "v", "版本");
    }

    // 机器人接到指令后会执行这个方法 ( 实现不同的接口的话方法名不一定一样 )

    public String run(EventMessage event, User sender, String command, ArrayList<String> args)
    {
        // 处理, 返回值会自动回复回去
        // 这里因为这个指令是用来查版本的, 所以直接返回字符串了
        return "拖拉机最新版本1.700\n可用版本1.50-1.700";
    }
}