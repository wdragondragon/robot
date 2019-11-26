package jdragon.club;

import Cantoon.ReturnAll;
import Cantoon.ReturnLast;
import EntoZh.Translate;
import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import game.OneATwoB.O2TCarry;
import game.P2P.PointClient;
import game.PlayCards.CardCarry;
import royal.RoyalClient;
import typing.CommandVersion;
import typing.ConDatabase.AllUserNum;
import typing.ConDatabase.HeartCon;
import typing.MessageMove;
import typing.OneUserNum;
import typing.RobotGroupClient;

/**
 * game 游戏模块
 * EntoZh 翻译模块
 * Cantoon 爬取饭堂消费模块
 * royal 皇室战争群管理模块
 * typing 跟打群管理模块
 * 该类创建两种QQ与微信机器人
 */
public class robot
{
    public static Long tljGroupNum = 974172771L;
    public static  Long xiaochaiQ = 207938707L;
    public static PicqBotX bot = null;
    public static PicqBotX getInstance(){
        return bot;
    }
    public static void main(String[] args)
    {
        // 创建机器人对象 ( 传入配置 )
        bot = new PicqBotX(new PicqConfig(9999).setDebug(true));
        PicqConfig c = new PicqConfig(9999);
        c.setDebug(true);
        // 添加一个机器人账户 ( 名字, 发送URL, 发送端口 )
        bot.addAccount("Bot01", "127.0.0.1", 5700);
        PointClient pointClient = new PointClient(bot.getAccountManager().getNonAccountSpecifiedApi());
        // 注册事件监听器, 可以注册多个监听器
        bot.getEventManager().registerListeners(
                new OneUserNum(),//拖拉机
                new RobotGroupClient(),//打字群
                new MessageMove(),//消息转发
                new Translate(),//翻译
                new CardCarry(),//斗地主
                new RoyalClient(),//皇室战争群
                pointClient//传话筒
                ,new O2TCarry()
                ,new ownUse()
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
        HeartCon h = new HeartCon();
        h.start();
//        //微信机器人
//        String qrPath = System.getProperty("user.dir")+"/login";
////                "D://itchat4j//login"; // 保存登陆二维码图片的路径，这里需要在本地新建目录
////        IMsgHandlerFace msgHandler = new PointClient(); // 实现IMsgHandlerFace接口的类
//        Wechat wechat = new Wechat(pointClient, qrPath); // 【注入】
//        wechat.start(); // 启动服务，会在qrPath下生成一张二维码图片，扫描即可登陆，注意，二维码图片如果超过一定时间未扫描会过期，过期时会自动更新，所以你可能需要重新打开图片
    }
}