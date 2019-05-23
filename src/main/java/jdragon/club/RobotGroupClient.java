package jdragon.club;

import ConDatebase.ComArti;
import ConDatebase.Conn;
import ConDatebase.InConn;
import ConDatebase.OutConn;
import GroupFollowTeamWar.GroupFollowTeamThread;
import GroupFollowWar.GroupFollowThread;
import GroupWar.GroupThread;
import Tool.RegexText;
import Tool.SortMap;
import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.event.events.message.EventMessage;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotGroupClient extends IcqListener {
    boolean respondSign;
    @EventHandler
    public void Carry(EventGroupMessage event)
    {
        respondSign = false;
        CarryName(event);
        if(!respondSign)
            CarryComArti(event);
        if(!respondSign)
            CarryComGrade(event);
        if(!respondSign)
            CarryComShow(event);
        if(!respondSign)
            ShowGroupList(event);
        if(!respondSign) {
            CreateCijicom(event);
            CreateFollowCom(event);
        }
        CreateFollowTeamCom(event);
    }
    private void CarryName(EventMessage event){
        try {
//            System.out.println("名片操作");
            String set[] = event.getMessage().split(" ");
            Long id = event.getSenderId();
            String message = "[CQ:at,qq=" + id + "]\n";
            if (set[0].equals("设置名片")) {
                message += InConn.setName(set[1], id);
                event.respond(message);
                respondSign = true;
            } else if (set[0].equals("查询")) {
                message += OutConn.ShowName(set[1]);
                event.respond(message);
                respondSign = true;
            }
        }catch (Exception e){}
    }
    private void CarryComArti(EventMessage event)
    {
        try {
            RegexText rgt = new RegexText();
            String Com[] = rgt.CarryCom(event.getMessage());
            System.out.println("收集999段操作");
            if (Com[2].equals("999")) {
                String message = Com[0] + "\n" + Com[1];
                Long GroupId = rgt.getGroupID(event.toString());
                Long SendId = event.getSenderId();
                message = "群:" + GroupId + " 人:" + SendId + " 捕获赛文段：\n" + message;
                System.out.println(message);
                if (SendId.equals(robot.xiaochaiQ)) {
                    InConn.AddGroupSaiwen(GroupId, Com);
                    respondSign = true;
                }
            }
        }catch (Exception e){}
    }
    private void CarryComGrade(EventMessage event){
        try {
            if (event.getMessage().substring(0, 5).equals("第999段")) {
                Long GroupID = RegexText.getGroupID(event.toString());
                Long SendID = event.getSenderId();
                double Grade[] = RegexText.getGrade(event.getMessage());
                String message = "群号:" + GroupID + "用户:" + SendID + "\n速度:" + Grade[0] + " 击键:" + Grade[1] + " 码长:" + Grade[2];
                System.out.println(message);
                InConn.AddRobotHistory(SendID, GroupID, Grade);
                InConn.addMaxComMath(SendID, GroupID, Grade);
//                event.getHttpApi().sendPrivateMsg(1061917196L, message);
                respondSign = true;
            }
        }catch (Exception e){}
    }
    private void CarryComShow(EventMessage event){
        try {
            String message = event.getMessage();
//            System.out.println("赛文和成绩操作");
            String s[] = message.split(" ");
            s[s.length - 1] = RegexText.AddZero(s[s.length - 1]);
            Date date = Date.valueOf(s[s.length - 1]);
            Long QQnum = event.getSenderId();
            if (s.length == 2) {
                if (s[0].equals("赛文")) {
                    event.respond(ComArti.responseStr(s[1], QQnum, date, 1));
                    respondSign = true;
                } else if (s[0].equals("成绩")) {
                    event.respond(ComArti.responseStr(s[1], QQnum, date, 2));
                    respondSign = true;
                }
            } else if (s.length == 3) {
                if (s[0].equals("赛文")) {
                    event.respond(OutConn.ShowGroupSaiwen(s[1], date));
                    respondSign = true;
                } else if (s[0].equals("成绩")) {
                    respondSign = true;
                }
            } else if (s.length == 4) {
                if (s[0].equals("成绩")) {
                    event.respond(OutConn.ShowGroupIdMath(s[1], s[2], date));
                    respondSign = true;
                }
            }
        }catch (Exception e){}
    }
    private void ShowGroupList(EventMessage event){
        try {
            String message = event.getMessage();
//            System.out.println("群映射操作");
            boolean sessced = false;
            if (message.equals("群映射列表")) {
                try {
                    String sql = "select * from groupmap";
                    Connection con = Conn.getConnection();
                    ResultSet rs = Conn.getStmtSet(con, sql);
                    message = "";
                    while (rs.next()) {
                        message += rs.getString("groupname") + "：" +
                                rs.getString("groupid") + "\n";
                        sessced = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (sessced)
                    event.respond(message.substring(0, message.length() - 1));
                respondSign = true;
            } else if (message.equals("人工智障帮助")) {
                event.respond(
                        "群映射列表 = 查询各大跟打群映射的缩写名字\n" +
                                "设置名片 名片 = 设置你名片（不是群名片）\n" +
                                "查询 你的名片 = 查询你的赛文上屏成绩概况\n" +
                                "赛文 年-月-日=查询拖拉机某天赛文\n" +
                                "成绩 年-月-日 = 查询拖拉机某天赛文成绩\n" +
                                "赛文 群映射名字 年-月-日 = 查询某个群某一天的赛文\n" +
                                "成绩 你的名片 群映射名字 年-月-日 = 查询你在某个群某天赛文成绩上屏记录\n"+
                                "#随机战场 启动不需要跟打器的QQ私聊作对照区，Q群聊天框作跟打区的对战模式\n"+
                                "#随机混战 启动一个以个人为单位计分的跟打发文\n"+
                                "#随机团战 启动一个以队伍为单位计分的跟打发文"
                );
                respondSign = true;
            }
        }catch (Exception e){}
    }

    static HashMap<Long, GroupThread> GroupWarList = new HashMap<Long, GroupThread>();
    private void CreateCijicom(EventGroupMessage event) {
        try {
            String message = event.getMessage();
            String s[] = message.split(" ");
            int length = s.length;
            Long GroupID = RegexText.getGroupID(event.toString());
            Long ID = event.getSenderId();
            String at = "[CQ:at,qq="+ID+"]\n";
            if (length == 4 && s[0].equals("#随机战场")) {
                if (GroupWarList.containsKey(GroupID))
                    event.respond(at+"随机战场已存在");
                else {//总长 段长 段时间
                    GroupThread gp = new GroupThread(Integer.valueOf(s[1]), Integer.valueOf(s[2]),
                            Integer.valueOf(s[3]),event,GroupID);
                    GroupWarList.put(GroupID, gp);
                    gp.start();
                    event.respond(at+"已开启一个总长度为" + s[1] + "，一段字数为" + s[2] + "，每段间隔为" + s[3] + "秒的战场");
                }
            } else if (message.equals("#加入战场")) {
                if (GroupWarList.containsKey(GroupID)) {
                    GroupThread gp = GroupWarList.get(GroupID);
                    if (gp.getStartSign())
                        event.respond(at+"加入失败，战场已开始，请等待下一场");
                    else if (gp.getIDlist().containsKey(ID))
                        event.respond(at+"请勿重复加入,退出战场指令：#退出战场");
                    else {
                        gp.addID(ID);
                        event.respond(at+"加入成功");
                    }
                } else
                    event.respond(at+"该群还未创建战场，指令：#随机战场");
            } else if (message.equals("#退出战场")) {
                if (GroupWarList.containsKey(GroupID)) {
                    GroupThread gp = GroupWarList.get(GroupID);
                    if (gp.getStartSign())
                        event.respond(at+"退出失败，战场已开始，战场结束自动退出");
                    else if (!gp.getIDlist().containsKey(ID))
                        event.respond(at+"你未曾加入战场，无法执行退出");
                    else {
                        gp.removeID(ID);
                        event.respond(at+"退出成功");
                    }
                } else
                    event.respond(at+"该群还未创建战场，指令：#随机战场");
            }else if(message.equals("#战场启动")){
                if (GroupWarList.containsKey(GroupID)) {
                    GroupThread gp = GroupWarList.get(GroupID);
                    if (gp.getStartSign())
                        event.respond(at+"战场已启动");
                    else if (!gp.getIDlist().containsKey(ID))
                        event.respond(at+"你未曾加入战场，无法执行启动");
                    else {
                        gp.setStartSign(true);
                        event.respond("战场启动！战斗开始！");
                    }
                } else
                    event.respond(at+"该群还未创建战场，指令：#随机战场");
            }
            else if(message.equals("#战场销毁")){
                if(GroupWarList.containsKey(GroupID)){
                    GroupThread gp = GroupWarList.get(GroupID);
                    gp.stop();
                    GroupWarList.remove(GroupID);
                    event.respond(at+"战场已销毁");
                }else
                    event.respond(at+"该群还未创建战场，指令：#随机战场");
            }
            else if(message.equals("#战场帮助")||message.equals("#随机战场")){
                message = "#随机战场 文章总长度 分段长度 间隔时间 = 创建一个战场\n"+
                        "#加入战场 = 加入战场\n"+
                        "#战场成员 = 查询已加入战场的群友\n"+
                        "#退出战场 = 退出本群战场\n"+
                        "#战场启动 = 与已加入战场的群友一起进行限时分段跟打\n"+
                        "#战场销毁 = 将本群创建的战场删除";
                event.respond(message);
            }
            else if(GroupWarList.containsKey(GroupID)){
                GroupThread gp = GroupWarList.get(GroupID);
                if(message.equals(gp.message)){
                    Map<Long,Integer> idlist = gp.getIDlist();
                    int i = idlist.get(ID);
                    idlist.put(ID,i+1);
                    System.out.println("加分："+(i+1));
                }
                else if(message.equals("#战场成员")){
                    Map<Long,Integer> idlist = gp.getIDlist();
                    String number = "";
                    for(Long k:idlist.keySet()){
                        number += "用户Q号："+k+"\n";
                    }
                    number += "共"+idlist.size()+"个成员准备进入战场";
                    event.respond(number);
                }
            }
        }catch (Exception e){event.respond("未知原因，操作失败");}
    }
    static HashMap<Long, GroupFollowThread> GroupFollowWarList = new HashMap<Long, GroupFollowThread>();
    private void CreateFollowCom(EventGroupMessage event){
        try{
            String message = event.getMessage();
            String s[] = message.split(" ");
            int length = s.length;
            Long GroupID = RegexText.getGroupID(event.toString());
            Long ID = event.getSenderId();
            String at = "[CQ:at,qq="+ID+"]\n";
            if(length==2&&s[0].equals("#随机混战")) {
                if (GroupFollowWarList.containsKey(GroupID))
                    event.respond(at + "随机混战已存在，若重开请先销毁");
                else {//总长 段长 段时间
                    GroupFollowThread gp = new GroupFollowThread(event, Integer.valueOf(s[1]), GroupID);
                    GroupFollowWarList.put(GroupID, gp);
                    gp.start();
                    event.respond(at + "已开启一个每段字数为" + s[1] + "的混战");
                }
            } else if (message.equals("#加入混战")) {
                if (GroupFollowWarList.containsKey(GroupID)) {
                    GroupFollowThread gp = GroupFollowWarList.get(GroupID);
                    if (gp.getIDlist().containsKey(ID))
                        event.respond(at+"请勿重复加入,退出混战指令：#退出混战");
                    else {
                        gp.addID(ID);
                        event.respond(at+"加入成功");
                    }
                } else
                    event.respond(at+"该群还未创建混战，指令：#随机混战");
            } else if (message.equals("#退出混战")) {
                if (GroupFollowWarList.containsKey(GroupID)) {
                    GroupFollowThread gp = GroupFollowWarList.get(GroupID);
                    if (!gp.getIDlist().containsKey(ID))
                        event.respond(at + "你未曾加入混战，无法执行退出");
                    else {
                        gp.removeID(ID);
                        event.respond(at + "退出成功");
                    }
                } else
                    event.respond(at + "该群还未创建混战，指令：#随机混战");
            }else if(message.equals("#混战启动")){
                if (GroupFollowWarList.containsKey(GroupID)) {
                    GroupFollowThread gp = GroupFollowWarList.get(GroupID);
                    if (gp.getStartSign())
                        event.respond(at+"混战已启动");
                    else if (!gp.getIDlist().containsKey(ID))
                        event.respond(at+"你未曾加入混战，无法执行启动");
                    else {
                        gp.setStartSign(true);
                        event.respond("混战启动！战斗开始！");
                        gp.send();
                    }
                } else
                    event.respond(at+"该群还未创建混战，指令：#随机混战");
            }else if(message.equals("#混战结算")){
                if(GroupFollowWarList.containsKey(GroupID)){
                    String message1 = "";
                    GroupFollowThread gp = GroupFollowWarList.get(GroupID);
                    message1 += SortMap.SendsortValue(gp.getIDlist());
                    gp.stop();
                    GroupFollowWarList.remove(GroupID);
                    event.respond(at+"混战已结算\n"+message1);
                }else
                    event.respond(at+"该群还未创建混战，指令：#随机混战");
            }else if(message.equals("#混战帮助")||message.equals("#随机混战")){
                message = "#随机混战 一段长度 = 创建一个混战\n"+
                        "#加入混战 = 加入混战\n"+
                        "#混战成员 = 查询已加入混战的群友\n"+
                        "#退出混战 = 退出本群混战\n"+
                        "#混战启动 = 与已加入混战的群友一起进行分段跟打\n"+
                        "#混战结算 = 将本群创建的混战结算成绩并删除\n"+
                        "记分规则：第一名3分，第二名2分，第三名1分，其他名次无分";
                event.respond(message);
            }else if(GroupFollowWarList.containsKey(GroupID)){
                GroupFollowThread gp = GroupFollowWarList.get(GroupID);
                boolean next = true;
                try {
                    String regex = "[^0123456789]+";
                    if (message.substring(0, 1).equals("第")&&
                            Integer.valueOf(message.substring(1,5).replaceAll(regex,""))==gp.getDuan()) {
                        double Grade[] = RegexText.getGrade(event.getMessage());
                        System.out.println(gp.getIDspend(ID));
                        if(gp.getIDspend(ID)==0.0) {
                            gp.setIDspend(ID, Grade[0]);
                            System.out.println(Grade[0]+" "+Grade[1]+" "+Grade[2]);
                        }
                        for(Long k:gp.getIDspendlist().keySet())
                            if(gp.getIDspendlist().get(k)==0.0){
                                System.out.println(k);
                                next=false;
                            }
                        if(next==true){
                            gp.nextDuan();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(message.equals("#混战成员")){
                    Map<Long,Integer> idlist = gp.getIDlist();
                    String number = "";
                    for(Long k:idlist.keySet()){
                        number += "用户Q号："+k+"\n";
                    }
                    number += "共"+idlist.size()+"个成员准备进入团场";
                    event.respond(number);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static HashMap<Long, GroupFollowTeamThread> GroupFolloTeamWarList = new HashMap<Long, GroupFollowTeamThread>();
    private void CreateFollowTeamCom(EventGroupMessage event){
        try{
            String message = event.getMessage();
            String s[] = message.split(" ");
            int length = s.length;
            Long GroupID = RegexText.getGroupID(event.toString());
            Long ID = event.getSenderId();
            String at = "[CQ:at,qq="+ID+"]\n";
            if(length==2&&s[0].equals("#随机团战")) {
                if (GroupFolloTeamWarList.containsKey(GroupID))
                    event.respond(at + "随机团战已存在，若重开请先销毁");
                else {//总长 段长 段时间
                    GroupFollowTeamThread gp = new GroupFollowTeamThread(event, Integer.valueOf(s[1]));
                    GroupFolloTeamWarList.put(GroupID, gp);
//                    gp.start();
                    event.respond(at + "已开启一个每段字数为" + s[1] + "的团战");
                }
            } else if (length==2&&s[0].equals("#加入团战")) {
                if (GroupFolloTeamWarList.containsKey(GroupID)) {
                    GroupFollowTeamThread gp = GroupFolloTeamWarList.get(GroupID);
                    gp.addID(Integer.valueOf(s[1]),ID);
                } else
                    event.respond(at+"该群还未创建团战，指令：#随机团战");
            } else if (message.equals("#退出团战")) {
                if (GroupFolloTeamWarList.containsKey(GroupID)) {
                    GroupFollowTeamThread gp = GroupFolloTeamWarList.get(GroupID);
                    gp.removeID(ID);

                } else
                    event.respond(at + "该群还未创建团战，指令：#随机团战");
            }else if(message.equals("#团战启动")){
            if (GroupFolloTeamWarList.containsKey(GroupID)) {
                GroupFollowTeamThread gp = GroupFolloTeamWarList.get(GroupID);
                if (gp.getStartSign())
                    event.respond(at+"团战已启动");
                else if (gp.isEmpty(ID)==-1)
                    event.respond(at+"你未曾加入团战，无法执行启动");
                else {
                    gp.setStartSign(true);
                    event.respond("团战启动！战斗开始！");
                    gp.send();
                }
            } else
                event.respond(at+"该群还未创建团战，指令：#随机团战");
        }else if(message.equals("#团战结算")){
            if(GroupFolloTeamWarList.containsKey(GroupID)){
                String message1 = "";
                GroupFollowTeamThread gp = GroupFolloTeamWarList.get(GroupID);
                message1 += SortMap.SendsortValueTeamMath(gp.getMath());
//                gp.stop();
                GroupFolloTeamWarList.remove(GroupID);
                event.respond(at+"团战已结算\n"+message1);
            }else
                event.respond(at+"该群还未创建团战，指令：#随机团战");
        }else if(message.equals("#团战帮助")||message.equals("#随机团战")){
            message = "#随机团战 一段长度 = 创建一个混战\n"+
                    "#加入团战 队伍号 = 加入某个队伍准备团战（加入团战 1/2）\n"+
                    "#团战成员 = 查询已加入该队伍的群友\n"+
                    "#退出团战 = 退出已加入的队伍\n"+
                    "#团战启动 = 与已加入团战的群友一起进行分段跟打\n"+
                    "#团战结算 = 将本群创建的团战结算成绩并删除\n"+
                    "记分规则：只分两只队伍，赢的得一分，按照队伍平均速度计算";
            event.respond(message);
        }else if(GroupFolloTeamWarList.containsKey(GroupID)){
                GroupFollowTeamThread gp = GroupFolloTeamWarList.get(GroupID);
                boolean next = true;
                try {
                    String regex = "[^0123456789]+";
                    if (message.substring(0, 1).equals("第")&&
                            Integer.valueOf(message.substring(1,5).replaceAll(regex,""))==gp.getDuan()) {
                        double Grade[] = RegexText.getGrade(event.getMessage());
//                        System.out.println(gp.getIDspend(ID));
                        if(gp.getSpeedlist().get(ID)==0.0) {
                            gp.setIDspend(ID, Grade[0]);
                            System.out.println(Grade[0]+" "+Grade[1]+" "+Grade[2]);
                        }
                        for(Integer k:gp.getMember().keySet()){
                            List<Long> member = gp.getMember().get(k);
                            Map<Long,Double> speedlist = gp.getSpeedlist();
                            for(int i = 0;i<member.size();i++){
                                if(speedlist.get(member.get(i))==0.0) {
                                    next = false;
                                }
                            }
                        }
                        if(next==true){
                            gp.nextDuan();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(message.equals("#团战成员")){
                    String message1 = "";
                    for(Integer k:gp.getMember().keySet()){
                        List<Long> member = gp.getMember().get(k);
                        message1 += k+"队成员：\n";
                        for(int i = 0;i<member.size();i++){
                            message1 += member.get(i)+"\n";
                        }
                    }
                    event.respond(message1);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}