package game.P2P;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventPrivateMessage;
import cc.moecraft.icq.event.events.request.EventFriendRequest;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RStrangerInfo;
import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.beans.RecommendInfo;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.DownloadTools;
import game.P2P.bean.User_info;
import game.P2P.handle.Controller;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PointClient extends IcqListener implements IMsgHandlerFace {
    private Logger LOG = Logger.getLogger(PointClient.class);
    private Controller controller;
    private boolean startup_status = false;
    public PointClient(IcqHttpApi httpApi){
        controller = new Controller(httpApi);
    }
    @EventHandler
    public void friend(EventFriendRequest event){
        event.accept();
        event.getBot().getAccountManager().refreshCache();
    }
    @EventHandler
    public void Carry(EventPrivateMessage event){
        String message = event.getMessage();
        if(message.equals("-启动")){
            event.respond("已启动");
            startup_status = true;
        } else if(message.equals("-关闭")){
            startup_status = false;
        }
        if (!startup_status)return;
        RStrangerInfo userinfo = event.getSender().getInfo();
        String username = userinfo.getNickname();
        String QQnumber = userinfo.getUserId().toString();
        String usersex = userinfo.getSex();
        long userage = userinfo.getAge();
        User_info user_info = new User_info(QQnumber,username)
                .sex(usersex)
                .age(userage)
                .type("QQ");
        controller.handleMsg(message,user_info);
    }
    @Override
    public String textMsgHandle(BaseMsg msg) {
        if(msg.isGroupMsg())return null;
        String message = msg.getText();
        if(message.equals("-启动")){
            startup_status = true;
        } else if(message.equals("-关闭")){
            startup_status = false;
        }
        if (!startup_status)return null;
        controller.handleMsg(message,getWXUserInfo(msg));
        return null;
    }
    private String qrPath = System.getProperty("user.dir");
//    private String image = "C:\\Users\\Lenovo\\Desktop\\酷Q Pro\\data\\image\\";
//    private String voice = "C:\\Users\\Lenovo\\Desktop\\酷Q Pro\\data\\record\\";
        private String voice = "/root/coolq/data/record/";
        private String image = "/root/coolq/data/image/";
    @Override
    public String picMsgHandle(BaseMsg msg) {
        if(msg.isGroupMsg())return null;
        else if(!startup_status)return null;
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());// 这里使用收到图片的时间作为文件名
        fileName += ".jpg";
        String picPath = image + fileName ; // 调用此方法来保存图片
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath); // 保存图片的路径
        System.out.println("图片保存成功");
        controller.WXhandleFileMsg("#图片信息",getWXUserInfo(msg),fileName);
        return null;
    }
    @Override
    public String voiceMsgHandle(BaseMsg msg) {
        if(msg.isGroupMsg())return null;
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        fileName += ".mp3";
//        String voicePath = voice + File.separator + fileName;
        String voicePath = voice  + fileName;
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
        System.out.println( "声音保存成功");
        controller.WXhandleFileMsg("#语音信息",getWXUserInfo(msg),fileName);
        return null;
    }
    @Override
    public String viedoMsgHandle(BaseMsg msg) {
        if(msg.isGroupMsg())return null;
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String viedoPath = qrPath + "/viedo" + File.separator + fileName + ".mp4";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
        System.out.println("视频保存成功");
        controller.WXhandleFileMsg("#其他信息",getWXUserInfo(msg),viedoPath);
        return null;
    }
    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        if(msg.isGroupMsg())return null;
        String fileName = msg.getFileName();
        String filePath = qrPath + "/file" + File.separator + fileName; // 这里是需要保存收到的文件路径，文件可以是任何格式如PDF，WORD，EXCEL等。
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.MEDIA.getType(), filePath);
        System.out.println("文件" + fileName + "保存成功");
        controller.WXhandleFileMsg("#其他信息",getWXUserInfo(msg),filePath);
        return null;
    }
    @Override
    public String nameCardMsgHandle(BaseMsg msg) {
        return null;
    }
    @Override
    public void sysMsgHandle(BaseMsg msg) {
        String text = msg.getContent();
        LOG.info(text);
    }
    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        MessageTools.addFriend(msg, true); // 同意好友请求，false为不接受好友请求
        RecommendInfo recommendInfo = msg.getRecommendInfo();
        String nickName = recommendInfo.getNickName();
        return "感谢添加传话筒 "+nickName;
    }
    private User_info getWXUserInfo(BaseMsg msg){
        String id = msg.getFromUserName();
        String name = "未知";
        return new User_info(id,name)
                .age(0)
                .sex(name)
                .type("WX");
    }
}
