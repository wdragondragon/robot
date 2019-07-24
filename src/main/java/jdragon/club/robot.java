package jdragon.club;

import typing.ConDatabase.*;
import cn.zhouyafeng.itchat4j.Wechat;
import game.P2P.PointClient;
import game.PlayCards.CardCarry;
import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import jdragon.club.cantoon.ReturnAll;
import jdragon.club.cantoon.ReturnLast;
import royal.RoyalClient;
import typing.CommandVersion;
import typing.MessageMove;
import typing.RobotGroupClient;

public class robot
{
//    public static Long tljGroupNum = 974172771L;
    public static  Long xiaochaiQ = 207938707L;
    public static void main(String[] args)
    {
        // 创建机器人对象 ( 传入配置 )
        PicqBotX bot = new PicqBotX(new PicqConfig(9999).setDebug(true));
        // 添加一个机器人账户 ( 名字, 发送URL, 发送端口 )
        bot.addAccount("Bot01", "127.0.0.1", 5700);
        PointClient pointClient = new PointClient(bot.getAccountManager().getNonAccountSpecifiedApi());
        // 注册事件监听器, 可以注册多个监听器
        bot.getEventManager().registerListeners(
                new OneUserNum(),
                new RobotGroupClient(),
                new MessageMove(),
                new Translate(),
                new CardCarry(),
                new RoyalClient(),
                pointClient
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
        //微信机器人
        String qrPath = System.getProperty("user.dir")+"/login";
//                "D://itchat4j//login"; // 保存登陆二维码图片的路径，这里需要在本地新建目录
//        IMsgHandlerFace msgHandler = new PointClient(); // 实现IMsgHandlerFace接口的类
        Wechat wechat = new Wechat(pointClient, qrPath); // 【注入】
        wechat.start(); // 启动服务，会在qrPath下生成一张二维码图片，扫描即可登陆，注意，二维码图片如果超过一定时间未扫描会过期，过期时会自动更新，所以你可能需要重新打开图片
    }
}