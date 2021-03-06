package typing;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.event.events.message.EventPrivateMessage;
import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.message.MessageBuilder;
import cc.moecraft.icq.sender.message.components.ComponentAt;
import cc.moecraft.icq.sender.message.components.ComponentImage;
import jdragon.club.robot;
import typing.ConDatabase.ComArti;
import typing.ConDatabase.Conn;
import typing.ConDatabase.InConn;
import typing.ConDatabase.OutConn;
import typing.GroupFollowTeamWar.GroupFollowTeamThread;
import typing.GroupFollowWar.GroupFollowThread;
import typing.GroupWar.GroupThread;
import typing.ShortCoding.BetterTyping;
import typing.Tools.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static java.io.File.separator;

public class RobotGroupClient extends IcqListener {
    boolean respondSign;
    boolean init = false;
    boolean noticesign = false;
    public static boolean automati_inclusion_sign;//收集赛文成绩标记
    public static HashMap<Long,Boolean> grouplist;//判断群是否已收


    HashMap<String,BetterTyping> betterTypingHashMap = new HashMap<>();
    public RobotGroupClient(){
        //定时收集赛文程序
        grouplist = OutConn.getGroupList();
        automati_inclusion_sign = false;
        Automatic_Inclusion automatic_inclusion = new Automatic_Inclusion();
        automatic_inclusion.start();

        loadCodeFile();
//        betterTypingHashMap.put("词组提示码表",new BetterTyping("词组提示码表"));
    }
    @EventHandler
    public void CarryBoth(EventMessage event) throws IOException {
        String message = event.getMessage();
        if(message.length()>1&&message.substring(0,1).equals("？")){
            BetterTyping betterTyping = betterTypingHashMap.get("词组提示码表");
            betterTyping.changecolortip(message.substring(1));
            betterTyping.compalllength();
            event.respond("标点顶点屏理论码长："+ RegexText.FourOutFiveIn(betterTyping.getDingKeylength())+" 总键数"+betterTyping.getDingalllength()
                    +"\n"+new ComponentImage(Createimg.drawTipImg(betterTyping.getSubscriptInstances())));
        }else if(message.length()>5&&message.substring(0,5).equals("一词不漏 ")){
            BetterTyping betterTyping = betterTypingHashMap.get("词组提示码表");
            betterTyping.changecolortip(message.substring(5));
            betterTyping.compalllength();
            event.respond("标点顶点屏理论码长："+ RegexText.FourOutFiveIn(betterTyping.getDingKeylength())+" 总键数"+betterTyping.getDingalllength()
                    +"\n"+new ComponentImage(Createimg.drawTipImg(betterTyping.getSubscriptInstances())));
        }
        else if(message.contains("？")&&betterTypingHashMap.containsKey(message.substring(0,message.indexOf("？")))){
            BetterTyping betterTyping = betterTypingHashMap.get(message.substring(0,message.indexOf("？")));
            betterTyping.changecolortip(message.substring(message.indexOf("？")+1));
            betterTyping.compalllength();
            event.respond(message.substring(0,message.indexOf("？"))+" 标点顶点屏理论码长："+ RegexText.FourOutFiveIn(betterTyping.getDingKeylength())+" 总键数："+betterTyping.getDingalllength()
                    +"\n"+new ComponentImage(Createimg.drawTipImg(betterTyping.getSubscriptInstances())));
        }else if(message.equals("#更新词库")){
            loadCodeFile();
        }else if(message.equals("#词库列表")){
            List<String> codeNameList = CodeFilesName.getCodeFilesName();
            StringBuffer stringBuffer = new StringBuffer();
            for(String codeName : codeNameList)
                stringBuffer.append(codeName+"\n");
            event.respond(stringBuffer.toString());
        }
        BetterTyping.setSubscriptInstanceNull();
    }
    @EventHandler
    public void CarryPrivate(EventPrivateMessage event){
        String message = event.getMessage();
        if(message.equals("#我的投稿")){
            event.respond(OutConn.lookmeAllGroupSaiwen(event.getSenderId()));
        }else if(message.equals("#取消广播")){
            noticesign = false;
            event.respond("广播被关闭");
        }else if (message.equals("#广播")){
            noticesign = true;
            event.respond("广播开启");
        }else if(noticesign&&initGroupList.adminList.contains(event.senderId)){
            for (Long aLong : grouplist.keySet()) {
                event.getHttpApi().sendGroupMsg(aLong,message);
            }
        }
        String []s = message.split("\\s+");
        if(s.length==3&&s[0].equals("#赛文投稿")){
            char []a = s[2].toCharArray();
            for(char b : a){
                if(!WordSet.get().contains(b)){
                    event.respond("文章有无法识别的字符");
                    return;
                }
            }
            event.respond(InConn.AddAllGroupSaiwenWait(s[1],s[2],event.getSenderId()));
            event.getHttpApi().sendGroupMsg(206666041L,"有新投稿："+s[1]+" 投稿自QQ号："+event.getSenderId()+"\n"+s[2]+"\n-----第9999段-共"+s[2].length()+"字");
//            event.respond(InConn.AddAllGroupSaiwen(s[1],s[2],event.getSenderId()));
        }else if(s.length==4&&s[0].equals("#赛文投稿")){
            event.respond(InConn.AddAllGroupSaiwenDate(RegexText.AddZero(s[1]),s[2],s[3],event.getSenderId()));
        }else if(s.length==4&&s[0].equals("#修改赛文")){
            event.respond(InConn.ChangeAllGroupSaiwen(RegexText.AddZero(s[1]),s[2],s[3],event.getSenderId(),false));
        }else if(s.length==4&&s[0].equals("#比赛投稿")) {
            event.respond(InConn.AddRobotSaiwen(s[1],s[2], s[3], event.getSenderId(), event.getSender().getInfo().getNickname()));
        }else if(s.length==2&&s[0].equals("清零")&&initGroupList.adminList.contains(event.senderId)){
            event.respond(InConn.Deleterobotinfo(Long.valueOf(s[1])));
        }
    }
    @EventHandler
    public void Carry(EventGroupMessage event)
    {
        IcqHttpApi httpApi = event.getHttpApi();
        try {
            if (!init) {
                initGroupList.init(httpApi);
                init = true;
            }
            respondSign = false;
            CarryName(event);
            if (!respondSign)
                CarryComArti(event);//收集赛文
            if (!respondSign)
                CarryComGrade(event);//收集赛文成绩
            if (!respondSign)
                CarryComShow(event);//发送成绩和赛文
            if (!respondSign)
                ShowGroupList(event);//群映射列表
            //每天收图
            if (automati_inclusion_sign) {
                Automatic_inclusion(event);
            }
            //发送各群的历史成绩
            if (!respondSign)
                grouphistory(event);
            //创建比赛场地
            if (!respondSign) {
                CreateCijicom(event);//创建跟打战场
                CreateFollowCom(event);//创建跟打战场
                CreateFollowTeamCom(event);//创建团队跟打战场
            }
            if(chanllgelist.containsKey(event.getSenderId())&&!respondSign){
                event.respond("[CQ:at,qq=" + event.getSenderId() + "]你还有文章未完成");
            }
            if(!respondSign&&RegexText.returnduan(event.getMessage())==-1){
                InConn.AddGroupCheatNum(event.getMessage().length(),event.getSenderId());
            }
            if(!respondSign){
                passSaiwen(event);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void passSaiwen(EventGroupMessage event) {
        String[] s = event.getMessage().split("\\s+");
        if (!(event.getGroupId() == 206666041L)) return;
        if (event.getMessage().equals("#所有已通过")){
            event.respond(OutConn.lookAllGroupSaiwen());
        }else if (event.getMessage().equals("#所有投稿")) {
            event.respond(OutConn.getAllAllGroupSaiwenWait());
        } else if(s.length==2){
            switch (s[0]) {
                case "#查看已通过":
                    event.respond(OutConn.ShowAllGroupSaiwen(Date.valueOf(RegexText.AddZero(s[1]))));
                    break;
                case "#删除已通过":
                    event.respond(InConn.DeleteAllGroupSaiwenByDate(RegexText.AddZero(s[1])));
                    break;
                case "#同意":
                    Object[] ret = {Integer.valueOf(s[1]), null,null};
                    String message = InConn.WaitToUpload(ret,true);
                    event.respond(message + " 审核者：" + event.getSenderId());
                    event.getHttpApi().sendPrivateMsg(Long.valueOf(ret[1].toString()), message + " 审核者：" + event.getSenderId() + " 审核文章："+ret[2].toString());
                    break;
                case "#拒绝":
                    Object[] ret1 = {Integer.valueOf(s[1]), null,null};
                    String message1 = InConn.WaitToUpload(ret1, false);
                    event.respond(message1 + " 审核者：" + event.getSenderId());
                    event.getHttpApi().sendPrivateMsg(Long.valueOf(ret1[1].toString()), message1 + " 审核者：" + event.getSenderId() + " 审核文章："+ ret1[2].toString());
                    break;

                case "#查看投稿":
                    event.respond(OutConn.getAllGroupSaiwenWatiById(Integer.valueOf(s[1])));
                    break;
            }
        }else if (s.length == 4 && s[0].equals("#修改赛文")) {
            event.respond(InConn.ChangeAllGroupSaiwen(RegexText.AddZero(s[1]), s[2], s[3], event.getSenderId(),true));
        }
    }
    public void loadCodeFile(){
        List<String> codeNameList = CodeFilesName.getCodeFilesName();

        //最佳编码码表加载
        for(String codeName : codeNameList){
            betterTypingHashMap.put(codeName,new BetterTyping(codeName));
        }
    }
    public void grouphistory(EventGroupMessage event){
        String message = event.getMessage();
        long groupid = event.getGroupId();
        String []s = message.split(" ");
        if(s[0].equals("历史成绩")){
            if(s.length==2) {
                s[1] = RegexText.AddZero(s[1]);
                String image = "typinggroup" + separator + groupid + "-" + s[1] + ".jpg";
                event.respond("[CQ:image,file=" + image + "]");
                respondSign = true;
            }else if(s.length==3){
                if(initGroupList.QQGroupNameMap.containsKey(s[1])) {
                    s[2] = RegexText.AddZero(s[2]);
                    String image = "typinggroup" + separator + initGroupList.QQGroupNameMap.get(s[1]) + "-" + s[2] + ".jpg";
                    event.respond("[CQ:image,file=" + image + "]");
                    respondSign = true;
                }else{
                    event.respond("无该群");
                }
            }
        }
    }
    private void Automatic_inclusion(EventGroupMessage event){
        System.out.println("收图");
        String message = event.getMessage();
        int imageindex = message.indexOf("url=");
        long groupid = event.getGroupId();
        long sendid = event.getSenderId();
        String []s = message.split(" ");
        if(sendid == robot.xiaochaiQ&&grouplist.containsKey(groupid)) {
            if (grouplist.get(groupid)&&imageindex != -1) {
                System.out.println(grouplist.get(groupid));
                message = message.substring(imageindex + 4, message.length() - 1);
                String filename = "typinggroup/"+groupid +"-"+ new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + ".jpg";
                    String image = "/root/coolq/data/image/" + filename;
                System.out.println(image);
                DownloadMsg.downloadMsg(message, image);
                grouplist.put(groupid,false);
                event.getHttpApi().sendGroupMsg(robot.tljGroupNum, "[CQ:image,file="+filename+"]");
                for (Long o : grouplist.keySet()) {
                    if(grouplist.get(o)==false){
                        automati_inclusion_sign = false;
                    }else {
                        automati_inclusion_sign = true;
                        break;
                    }
                }
            }else if(message.equals("没有找到今天的比赛成绩！")){
                grouplist.put(groupid,false);
                for (Long o : grouplist.keySet()) {
                    if(grouplist.get(o)==false){
                        automati_inclusion_sign = false;
                    }else {
                        automati_inclusion_sign = true;
                        break;
                    }
                }
            }
        }
    }
    private void CarryName(EventGroupMessage event){
        try {
            System.out.println("名片操作");
            Long id = event.getSenderId();
            String message = "[CQ:at,qq=" + id + "]\n";
            if(event.getMessage().equals("#刷新缓存")){
                event.getGroupSender().refreshInfo();
                event.getGroup().refreshInfo();
                event.getBot().getAccountManager().refreshCache();
                initGroupList.init(event.getHttpApi());
//                event.respond("刷新成功，你现在的群名片为"+event.getGroupSender().getInfo().getCard());
                event.respond("刷新成功，你现在的群名片为"+event.getHttpApi().getGroupMemberInfo(event.getGroupId(),id).getData().getCard());
                respondSign = true;
            }
            String set[] = event.getMessage().split(" ");
            if (set[0].equals("#设置名片")) {
                message += InConn.setName(set[1], id);
                event.respond(message);
                respondSign = true;
            } else if (set[0].equals("#查询")) {
                System.out.println(event.getMessage());
                Matcher m = RegexText.isAt(event.getMessage());
                if(m.find()){
                    System.out.println(m.group(1));
                    message += OutConn.ShowName(m.group(1));
                }
                else {
                    message += OutConn.ShowName(set[1]);
                }
                event.respond(message);
                respondSign = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
            Long GroupID = RegexText.getGroupID(event.toString());
            Long SendID = event.getSenderId();

            if (event.getMessage().length()>5&&event.getMessage().substring(0, 5).equals("第999段")) {
                double Grade[] = RegexText.getGrade(event.getMessage());
                String message = "群号:" + GroupID + "用户:" + SendID + "\n速度:" + Grade[0] + " 击键:" + Grade[1] + " 码长:" + Grade[2];
                System.out.println(message);
                long MaxQQ = OutConn.getTodayMaxByGroupId(GroupID,Grade[0],RegexText.returnduan(event.getMessage()));
                if(MaxQQ!=0L&&MaxQQ!=SendID){
                    event.respond(new MessageBuilder().add(new ComponentAt(MaxQQ)).add("你第一被抢了").toString());
                }
                if(GroupID!=726064238L)
                    InConn.AddRobotHistory(SendID, GroupID, Grade);
                InConn.addMaxComMath(SendID, GroupID, Grade);
//                event.getHttpApi().sendPrivateMsg(1061917196L, message);
                respondSign = true;
            }else if(event.getMessage().length()>6&&event.getMessage().substring(0, 6).equals("第9999段")){
                System.out.println("第9999段");
                double Grade[] = RegexText.getGrade(event.getMessage());
                System.out.println(Grade);
                long MaxQQ = OutConn.getTodayMaxByGroupId(GroupID,Grade[0],RegexText.returnduan(event.getMessage()));
                if(MaxQQ!=0L&&MaxQQ!=SendID){
                    event.respond(new MessageBuilder().add(new ComponentAt(MaxQQ)).add("你联赛第一被抢了").toString());
                }
                InConn.AddRobotHistory(SendID, GroupID, Grade);
                InConn.addMaxAllGroupComMath(SendID, GroupID, Grade,event.getSender().getInfo().getNickname());
                respondSign = true;
            }else if(RegexText.returnduan(event.getMessage())==1&&chanllgelist.containsKey(SendID)){
                System.out.println("挑战段");
                double Grade[] = RegexText.getGrade(event.getMessage());
                event.respond(InConn.addRobotSaiwenMath(SendID,GroupID,Grade,
                        event.getSender().getInfo().getNickname(),
                        chanllgelist.get(SendID)));
                chanllgelist.remove(SendID);
                respondSign = true;
            }else if(event.getMessage().equals("#取消上传")){
                if(chanllgelist.containsKey(SendID)) {
                    chanllgelist.remove(SendID);
                    event.respond("取消成功");
                }
                else
                    event.respond("取消你妹，你都没开始！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    HashMap<Long,String> chanllgelist = new HashMap<>();
    private void CarryComShow(EventGroupMessage event){
        try {
            String message = event.getMessage();
            System.out.println("赛文和成绩操作");
            Long QQnum = event.getSenderId();
            if(message.equals("#成绩")){
                String path = OutConn.ShowGroupIdMath(QQnum, initGroupList.QQGroupMap.get(event.getGroupId()), Conn.getdate(),event.getHttpApi().getGroupMemberInfo(event.getGroupId(),QQnum).getData().getCard());
                if(path.equals("无收录成绩"))event.respond(path);
                else event.respond("[CQ:image,file="+ path +"]");
            }else if(message.equals("#拖拉机成绩")){
                String path = ComArti.responseStr(Conn.getdate().toString(), QQnum, Conn.getdate(), 2);
                if(path.equals("无该天赛文成绩"))event.respond(path);
                else event.respond("[CQ:image,file="+ path +"]");
                respondSign = true;
            }else if(message.equals("#联赛")){
                event.respond(OutConn.ShowAllGroupSaiwen(Conn.getdate()));
                respondSign = true;
            }else if(message.equals("#联赛成绩")){
                String path = ComArti.responseStr(Conn.getdate().toString(), QQnum, Conn.getdate(), 3);
                if(path.equals("无该天赛文成绩"))event.respond(path);
                else event.respond("[CQ:image,file="+ path +"]");
                respondSign = true;
            }else if(message.equals("#统计成绩")){
                String path =  ComArti.responseStr(Conn.getdate().toString(), QQnum, Conn.getdate(), 4);
                if(path.equals("无该天赛文成绩"))event.respond(path);
                else event.respond("[CQ:image,file="+ path +"]");
                respondSign = true;
            }
            else if(message.equals("#拖拉机详情")){
                event.respond(OutConn.tljinfo());
                respondSign = true;
            }
            String s[] = message.split(" ");
            if(s.length==2&&s[0].equals("#文章")){
                event.respond(OutConn.ShowRobotSaiwen(s[1],0,1));
                respondSign = true;
            }
            else if(s.length==3&&s[0].equals("#文章")){
                chanllgelist.put(QQnum,s[1]+"%"+s[2]);
                event.respond(OutConn.ShowRobotSaiwen(s[1],Integer.valueOf(s[2]),2));
                respondSign = true;
            }else if (s.length==3&&s[0].equals("#文章成绩")){
                String path = ComArti.getRobotArMathImgPath(s[1],Integer.valueOf(s[2]));
                if(path.equals("没有该文章成绩"))event.respond(path);
                else event.respond("[CQ:image,file="+path+"]");
                respondSign = true;
            }
            if(respondSign==true)return;
            Date date = null;
            s[s.length - 1] = RegexText.AddZero(s[s.length - 1]);
            try {
                date = Date.valueOf(s[s.length - 1]);
            }catch (Exception e){e.printStackTrace();}
            Matcher m = RegexText.isAt(s[1]);
            if (s.length == 2) {
                if (s[0].equals("#拖拉机赛文")) {
                    event.respond(ComArti.responseStr(s[1], QQnum, date, 1));
                    respondSign = true;
                } else if (s[0].equals("#拖拉机成绩")) {
//                    event.respond(ComArti.responseStr(s[1], QQnum, date, 2));
                    event.respond("[CQ:image,file="+ComArti.responseStr(s[1], QQnum, date, 2)+"]");
                    respondSign = true;
                } else if(s[0].equals("#成绩")){
                    String path;
                    String path2;
                    if(date!=null){
                        path = OutConn.ShowGroupIdMath(event.getSenderId(), initGroupList.QQGroupMap.get(event.getGroupId()), date, initGroupList.QQlist.get(event.getSenderId()));
                        path2 = "typinggroup/"+event.getGroupId() +"-"+ date + ".jpg";
                    }else if(initGroupList.QQGroupMap.values().contains(s[1])) {
                        path = OutConn.ShowGroupIdMath(event.getSenderId(), s[1], Conn.getdate(), initGroupList.QQlist.get(event.getSenderId()));
                    }else path = "无收录成绩";
                    if(path.equals("无收录成绩"))event.respond(path);
                    else event.respond("[CQ:image,file="+ path +"]");

                } else if(s[0].equals("#清零")&&QQnum==1061917196L){
                    event.respond(InConn.Deleterobotinfo(Long.valueOf(s[1])));
                }
            } else if (s.length == 3) {
                if (s[0].equals("#赛文")) {
                    event.respond(OutConn.ShowGroupSaiwen(s[1], date));
                    respondSign = true;
                } else if (s[0].equals("#成绩")) {
                    String path  = OutConn.ShowGroupIdMath(event.getSenderId(), s[1], date, initGroupList.QQlist.get(event.getSenderId()));
                    if(path.equals("无收录成绩"))event.respond(path);
                    else event.respond("[CQ:image,file="+ path +"]");
//                    event.respond(OutConn.ShowGroupSaiwenMath(s[1], date));
                    respondSign = true;
                }
            } else if (s.length == 4) {
                if (s[0].equals("#成绩")) {
                    System.out.println("#成绩");
                    Long QQ;
                    if(m.find())
                        QQ = Long.valueOf(m.group(1));
                    else
                        QQ = Long.valueOf(s[1]);
//                    event.respond(OutConn.ShowGroupIdMath(QQ, s[2], date,event.getGroupSender().getInfo().getCard()));
                    String path = OutConn.ShowGroupIdMath(QQ, s[2], date,initGroupList.QQlist.get(QQ));
                    if(path.equals("无收录成绩"))event.respond(path);
                    else event.respond("[CQ:image,file="+ path +"]");
                    respondSign = true;
                }
            }
        }catch (Exception e){
//            e.printStackTrace();
        }
    }
    private void ShowGroupList(EventMessage event){
        try {
            String message = event.getMessage();
            System.out.println("群映射操作");
            boolean sessced = false;
            if (message.equals("#群映射列表")) {
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
            } else if (message.equals("#人工智障帮助")) {
                event.respond(
                        "群映射列表 = 查询各大跟打群映射的缩写名字\n" +
                                "查询 @QQ = 查询你的赛文上屏成绩概况 \n" +
                                "-----群赛指令-----\n" +
                                "？+ 要查询的字词 = 该词小鹤的最佳编码\n" +
                                "例：？奇怪 = qg" +
                                "-----群赛指令-----\n"+
                                "赛文 群映射名字 年-月-日 = 查询某个群某一天的赛文\n" +
                                "成绩 群映射名字 = 查询你在某个群今天成绩上屏记录\n" +
                                "成绩 年-月-日 = 查询你在这个群某天的成绩上屏记录\n" +
                                "成绩 群映射名字 年-月-日 = 查询你在某个群某天成绩详情\n"+
                                "成绩 @QQ 群映射名字 年-月-日 = 查询你在某个群某天赛文成绩上屏记录\n"+
                                "历史成绩 yyyy-MM-dd = 查看本群的某日比赛成绩（图片成绩）\n" +
                                "历史成绩 群映射名字 yyyy-MM-dd = 查询某个群的某日比赛成绩（图片成绩）\n"+

                                "-----联赛指令-----\n"+
                                "#联赛 = 获取今天的联赛赛文\n"+
                                "#联赛成绩 = 查看今天的联赛成绩\n"+
                                "#统计成绩 查看统计所有群列表的日赛成绩汇总\n" +
                                "私聊机器人->#我的投稿 = 能看到你已投稿的赛文信息\n"+
                                "私聊机器人->赛文投稿（换行）标题（换行）内容 = 进行延续日期投稿赛文\n"+
                                "私聊机器人->赛文投稿（换行）yyyy-MM-dd（换行）标题（换行）内容 = 进行指定日期投稿赛文\n"+
                                "私聊机器人->修改赛文（换行）yyyy-MM-dd（换行）标题（换行）内容 = 修改指定日期已投稿的赛文\n"+

                                "-----拖拉机指令-----\n"+
                                "拖拉机 拖拉机号名 = 查询你拖拉机中账号信息\n"+
                                "拖拉机成绩 yyyy-MM-dd = 查看某年某月某日的拖拉机生稿成绩\n"+
                                "#拖拉机成绩 = 查看今天的拖拉机生稿成绩\n"+

                                "-----战场指令-----\n"+
                                "#随机战场 启动不需要跟打器的QQ私聊作对照区，Q群聊天框作跟打区的对战模式\n"+
                                "#随机混战 启动一个以个人为单位计分的跟打发文\n"+
                                "#随机团战 启动一个以队伍为单位计分的跟打发文\n"+

                                "-----交友指令-----\n"+
                                "私聊机器人->#匹配 = 随机与另一同为人工智障好友的人匹配聊天\n"+
                                "私聊机器人->#退出 = 已配对成功后，该指令用于退出聊天\n" +
                                "https://jdragon.club/s/1565351363708\n" +

                                "-----游戏指令-----\n"+
                                "1A2B = 启动以个人为单位的1A2B游戏（默认4个数）\n"+
                                "1A2B 个位数 = 启动以个人为单位的1A2B游戏（指定位数）"

                );
                respondSign = true;
            }else if(message.equals("#管理指令")){
                event.respond(
                        "#所有已通过 = 查看今日以后的所有已通过赛文粗略信息\n" +
                                "#查看已通过 = 查看已通过的某天详细赛文\n" +
                                "#删除已通过 = 删除已通过的赛文\n" +
                                "#所有投稿 = 查看所有未通过投稿粗略信息\n" +
                                "#查看投稿 id = 通过id查看未通过的详细赛文\n" +
                                "#同意 id = 通过id来通过文章\n"+
                                "#拒绝 id = 通过id来拒绝文章\n"+
                                "#修改赛文 = 修改已通过某一日的赛文\n"

                );
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
            String card = event.getHttpApi().getGroupMemberInfo(event.getGroupId(),ID).getData().getCard();
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
                        gp.addID(ID,card);
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
            String card = event.getHttpApi().getGroupMemberInfo(event.getGroupId(),ID).getData().getCard();
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
                        gp.addID(ID,card);
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
                    if (!gp.getIDlist().containsKey(ID)){
                        event.respond(at+"你未曾加入混战，无法执行启动");
                        return;
                    }
                    message1 += SortMap.SendsortValue(gp.getIDlist(),gp.getIDnamelist());
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
                        "#让速 速度 = 结算临时成绩时，将实际成绩减去让速得出本段成绩\n"+
                        "记分规则：第一名3分，第二名2分，第三名1分，其他名次无分";
                event.respond(message);
            }else if(length==2&&s[0].equals("#让速")){
                if(GroupFollowWarList.containsKey(GroupID)){
                    GroupFollowThread gp = GroupFollowWarList.get(GroupID);
                    if (!gp.getIDlist().containsKey(ID)){
                        event.respond(at+"你未曾加入混战，无法执行启动");
                        return;
                    }
                    try{

                        Double letSpeed = Double.parseDouble(s[1]);
                        if(letSpeed<1&&letSpeed>0) {
                            gp.setLetSpeed(ID, letSpeed);
                            event.respond(at + "让速设置成功");
                        }else
                            event.respond(at+"让速参数必须在0到1之间");
                    }catch (Exception e){}
                }
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
                            gp.setIDspend(ID, Grade[0]*gp.getLetSpeed(ID));
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
            String card = event.getHttpApi().getGroupMemberInfo(event.getGroupId(),ID).getData().getCard();
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
                    gp.addID(Integer.valueOf(s[1]),ID,card);
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