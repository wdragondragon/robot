package jdragon.club;

import  ConDatebase.*;
import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;

public class robot
{
    public static Long tljGroupNum = 974172771L;
    public static  Long xiaochaiQ = 207938707L;
    public static void main(String[] args)
    {
        // 创建机器人对象 ( 传入配置 )
        PicqBotX bot = new PicqBotX(new PicqConfig(9999).setDebug(true));

        // 添加一个机器人账户 ( 名字, 发送URL, 发送端口 )
        bot.addAccount("Bot01", "127.0.0.1", 5700);

        // 注册事件监听器, 可以注册多个监听器
        bot.getEventManager().registerListeners(
                new OneUserNum(),
                new RobotGroupClient()
        );
        // 启用指令管理器
        // 这些字符串是指令前缀, 比如指令"!help"的前缀就是"!"
        bot.enableCommandManager("bot -", "!", "/", "~");

        // 注册指令, 可以注册多个指令
        bot.getCommandManager().registerCommands(
                new CommandSay(),//返回自己名字
                new ReturnLast(),//返回最后一条消费记录
                new ReturnAll(),//返回当日全部消费记录
                new CommandVersion(),//返回版本
                new AllUserNum()
        );
        // 启动机器人, 不会占用主线程
        bot.startBot();
    }
}