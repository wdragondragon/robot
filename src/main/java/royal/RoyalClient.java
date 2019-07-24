package royal;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.event.events.notice.groupmember.EventNoticeGroupMemberChange;
import cc.moecraft.icq.event.events.notice.groupmember.increase.EventNoticeGroupMemberApprove;
import royal.ConDatabase.Fresh;
import royal.ConDatabase.InConn;

public class RoyalClient  extends IcqListener {
    public RoyalClient(){
        Fresh fresh = new Fresh();
        fresh.start();
    }
    @EventHandler
    public void AddPeople(EventNoticeGroupMemberApprove event){
        System.out.println(event.getGroupId());
        if(event.getGroupId()==3052940L) {
            Long id = event.getUserId();
            String at = "[CQ:at,qq=" + id + "]\n";
            String message = at + "该群广大部落招人：\n" + InConn.getAllmessage();
//            event.getHttpApi().sendGroupMsg(3052940L,message);
            event.getGroupMethods().respond(message);
            event.getBot().getAccountManager().refreshCache();
        }
    }
    @EventHandler
    public void chnageNum(EventNoticeGroupMemberChange event){
        event.getBot().getAccountManager().refreshCache();
    }
    @EventHandler
    public void Carry(EventGroupMessage event) {
        String message = event.getMessage();
        Long id = event.getSenderId();
        String s[] = message.split(" ");
        String at = "[CQ:at,qq=" + id + "]\n";
        Long GroupID = event.getGroupId();
        if(GroupID==3052940L&&!message.equals("#招人")&&(message.indexOf("元")!=-1||message.indexOf("招人")!=-1||message.indexOf("代打")!=-1
                ||message.indexOf("私聊")!=-1||message.indexOf("预定")!=-1||message.indexOf("价格")!=-1
        ||message.indexOf("收人")!=-1||message.indexOf("群号")!=-1)){
//            event.ban(600);
            event.delete();
            event.respond("[CQ:at,qq=1061917196]\n处理广告");
        }
        if(s.length==5&&s[0].equals("挂招")){
            if(InConn.addTribe(id,s[1],s[2],Integer.valueOf(s[3]),Integer.valueOf(s[4]))){
                event.respond(at+"添加挂招成功");
            }else
                event.respond(at+"添加挂招失败");
        }else if(s.length==2){
            if(s[0].equals("允许挂招")) {
                if (id == 1061917196L) {
                    if (InConn.changeallow(Long.valueOf(s[1]), 1))
                        event.respond("允许挂招成功");
                    else
                        event.respond("允许挂招失败");
                } else
                    event.respond("只有群主能允许挂招");
            }
            else if(s[1].equals("禁止挂招")){
                if(id == 1061917196L){
                    if(InConn.changeallow(Long.valueOf(s[1]),0))
                        event.respond("禁止挂招成功");
                    else
                        event.respond("禁止挂招失败");
                }else
                    event.respond("只有群主能禁止挂招");
            }
        }else if(message.equals("#招人")){
            event.respond(InConn.getMessage(id));
        }else if(s.length==2&&s[0].equals("删除挂招")){
            if(InConn.delete(Long.valueOf(s[1]))){
                event.respond("删除成功");
            }else
                event.respond("删除失败");
        }else if(message.equals("挂招帮助")){
            message = "挂招系统是方便群主进行群管理而统一将该群的部落集中进行招人处理一个手段\n" +
                    "挂招指令：\n" +
                    "挂招 部落名 招人宣传语 进入要求杯数 部落总杯数 = 申请挂招\n" +
                    "例：\n" +
                    "挂招 魅眸 魅眸招人快乐每一天！ 4600 55000 = 将名为魅眸的部落挂招\n" +
                    "#招人 = 呼出招人信息";
            event.respond(at+message);
        }else if(message.equals("等待允许挂招列表")){
            event.respond(InConn.waitallow());
        }
    }
}
