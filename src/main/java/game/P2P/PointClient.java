package game.P2P;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventPrivateMessage;
import cc.moecraft.icq.event.events.request.EventFriendRequest;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RStrangerInfo;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
public class PointClient extends IcqListener implements IMsgHandlerFace {
    IcqHttpApi httpAPI;
    controller controller;
    boolean startup_status = false;
    public PointClient(IcqHttpApi httpApi){
        this.httpAPI = httpApi;
        controller = new controller(httpApi);
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
            startup_status = true;
        } else if(message.equals("-关闭")){
            startup_status = false;
        }
        if (startup_status == false)return;
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
        String message = msg.getText();
        if(message.equals("-启动")){
            startup_status = true;
        } else if(message.equals("-关闭")){
            startup_status = false;
        }
        if (startup_status == false)return null;
        String id = msg.getFromUserName();
        String name = "未知";
        User_info user_info = new User_info(id,name)
                .age(0)
                .sex(name)
                .type("WX");
        controller.handleMsg(message,user_info);
        return null;
    }

    @Override
    public String picMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String voiceMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String viedoMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String nameCardMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public void sysMsgHandle(BaseMsg msg) {

    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        return null;
    }
}
