package jdragon.club;

import ConDatebase.ComArti;
import ConDatebase.Conn;
import ConDatebase.InConn;
import ConDatebase.OutConn;
import GroupWar.GroupThread;
import Tool.RegexText;
import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.event.events.message.EventMessage;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.HashMap;
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
        if(!respondSign)
            CreateCom(event);
        
    }
    private void CarryName(EventMessage event){
        try {
//            System.out.println("��Ƭ����");
            String set[] = event.getMessage().split(" ");
            Long id = event.getSenderId();
            String message = "[CQ:at,qq=" + id + "]\n";
            if (set[0].equals("������Ƭ")) {
                message += InConn.setName(set[1], id);
                event.respond(message);
                respondSign = true;
            } else if (set[0].equals("��ѯ")) {
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
            System.out.println("�ռ�999�β���");
            if (Com[2].equals("999")) {
                String message = Com[0] + "\n" + Com[1];
                Long GroupId = rgt.getGroupID(event.toString());
                Long SendId = event.getSenderId();
                message = "Ⱥ:" + GroupId + " ��:" + SendId + " �������ĶΣ�\n" + message;
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
            if (event.getMessage().substring(0, 5).equals("��999��")) {
                Long GroupID = RegexText.getGroupID(event.toString());
                Long SendID = event.getSenderId();
                double Grade[] = RegexText.getGrade(event.getMessage());
                String message = "Ⱥ��:" + GroupID + "�û�:" + SendID + "\n�ٶ�:" + Grade[0] + " ����:" + Grade[1] + " �볤:" + Grade[2];
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
//            System.out.println("���ĺͳɼ�����");
            String s[] = message.split(" ");
            s[s.length - 1] = RegexText.AddZero(s[s.length - 1]);
            Date date = Date.valueOf(s[s.length - 1]);
            Long QQnum = event.getSenderId();
            if (s.length == 2) {
                if (s[0].equals("����")) {
                    event.respond(ComArti.responseStr(s[1], QQnum, date, 1));
                    respondSign = true;
                } else if (s[0].equals("�ɼ�")) {
                    event.respond(ComArti.responseStr(s[1], QQnum, date, 2));
                    respondSign = true;
                }
            } else if (s.length == 3) {
                if (s[0].equals("����")) {
                    event.respond(OutConn.ShowGroupSaiwen(s[1], date));
                    respondSign = true;
                } else if (s[0].equals("�ɼ�")) {
                    respondSign = true;
                }
            } else if (s.length == 4) {
                if (s[0].equals("�ɼ�")) {
                    event.respond(OutConn.ShowGroupIdMath(s[1], s[2], date));
                    respondSign = true;
                }
            }
        }catch (Exception e){}
    }
    private void ShowGroupList(EventMessage event){
        try {
            String message = event.getMessage();
//            System.out.println("Ⱥӳ�����");
            boolean sessced = false;
            if (message.equals("Ⱥӳ���б�")) {
                try {
                    String sql = "select * from groupmap";
                    Connection con = Conn.getConnection();
                    ResultSet rs = Conn.getStmtSet(con, sql);
                    message = "";
                    while (rs.next()) {
                        message += rs.getString("groupname") + "��" +
                                rs.getString("groupid") + "\n";
                        sessced = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (sessced)
                    event.respond(message.substring(0, message.length() - 1));
                respondSign = true;
            } else if (message.equals("�˹����ϰ���")) {
                event.respond(
                        "Ⱥӳ���б� = ��ѯ�������Ⱥӳ�����д����\n" +
                                "������Ƭ ��Ƭ = ��������Ƭ������Ⱥ��Ƭ��\n" +
                                "��ѯ �����Ƭ = ��ѯ������������ɼ��ſ�\n" +
                                "���� ��-��-��=��ѯ������ĳ������\n" +
                                "�ɼ� ��-��-�� = ��ѯ������ĳ�����ĳɼ�\n" +
                                "���� Ⱥӳ������ ��-��-�� = ��ѯĳ��Ⱥĳһ�������\n" +
                                "�ɼ� �����Ƭ Ⱥӳ������ ��-��-�� = ��ѯ����ĳ��Ⱥĳ�����ĳɼ�������¼"
                );
                respondSign = true;
            }
        }catch (Exception e){}
    }

    static HashMap<Long, GroupThread> GroupWarList = new HashMap<Long, GroupThread>();
    private void CreateCom(EventGroupMessage event) {
        try {
            String message = event.getMessage();
//            System.out.println("���ս������");
            String s[] = message.split(" ");
            int length = s.length;
            Long GroupID = RegexText.getGroupID(event.toString());
            Long ID = event.getSenderId();
            String at = "[CQ:at,qq="+ID+"]\n";
            if (length == 4 && s[0].equals("#���ս��")) {
                if (GroupWarList.containsKey(GroupID))
                    event.respond(at+"���ս���Ѵ���");
                else {//�ܳ� �γ� ��ʱ��
                    GroupThread gp = new GroupThread(Integer.valueOf(s[1]), Integer.valueOf(s[2]),
                            Integer.valueOf(s[3]),event,GroupID);
                    GroupWarList.put(GroupID, gp);
                    gp.start();
                    event.respond(at+"�ѿ���һ���ܳ���Ϊ" + s[1] + "��һ������Ϊ" + s[2] + "��ÿ�μ��Ϊ" + s[3] + "���ս��");
                }
            } else if (message.equals("#����ս��")) {
                if (GroupWarList.containsKey(GroupID)) {
                    GroupThread gp = GroupWarList.get(GroupID);
                    if (gp.getStartSign())
                        event.respond(at+"����ʧ�ܣ�ս���ѿ�ʼ����ȴ���һ��");
                    else if (gp.getIDlist().containsKey(ID))
                        event.respond(at+"�����ظ�����,�˳�ս��ָ�#�˳�ս��");
                    else {
                        gp.addID(ID);
                        event.respond(at+"����ɹ�");
                    }
                } else
                    event.respond(at+"��Ⱥ��δ����ս����ָ�#���ս��");
            } else if (message.equals("#�˳�ս��")) {
                if (GroupWarList.containsKey(GroupID)) {
                    GroupThread gp = GroupWarList.get(GroupID);
                    if (gp.getStartSign())
                        event.respond(at+"�˳�ʧ�ܣ�ս���ѿ�ʼ��ս�������Զ��˳�");
                    else if (!gp.getIDlist().containsKey(ID))
                        event.respond(at+"��δ������ս�����޷�ִ���˳�");
                    else {
                        gp.removeID(ID);
                        event.respond(at+"�˳��ɹ�");
                    }
                } else
                    event.respond(at+"��Ⱥ��δ����ս����ָ�#���ս��");
            }else if(message.equals("#ս������")){
                if (GroupWarList.containsKey(GroupID)) {
                    GroupThread gp = GroupWarList.get(GroupID);
                    if (gp.getStartSign())
                        event.respond(at+"ս��������");
                    else if (!gp.getIDlist().containsKey(ID))
                        event.respond(at+"��δ������ս�����޷�ִ������");
                    else {
                        gp.setStartSign(true);
                        event.respond("ս��������ս����ʼ��");
                    }
                } else
                    event.respond(at+"��Ⱥ��δ����ս����ָ�#���ս��");
            }
            else if(message.equals("#ս������")){
                if(GroupWarList.containsKey(GroupID)){
                    GroupThread gp = GroupWarList.get(GroupID);
                    gp.stop();
                    GroupWarList.remove(GroupID);
                    event.respond(at+"ս��������");
                }else
                    event.respond(at+"��Ⱥ��δ����ս����ָ�#���ս��");
            }
            else if(message.equals("#ս������")||message.equals("#���ս��")){
                message = "#���ս�� �����ܳ��� �ֶγ��� ���ʱ�� = ����һ��ս��\n"+
                        "#����ս�� = ����ս��\n"+
                        "#ս����Ա = ��ѯ�Ѽ���ս����Ⱥ��\n"+
                        "#�˳�ս�� = �˳���Ⱥս��\n"+
                        "#ս������ = ���Ѽ���ս����Ⱥ��һ�������ʱ�ֶθ���\n"+
                        "#ս������ = ����Ⱥ������ս��ɾ��";
                event.respond(message);
            }
            else if(GroupWarList.containsKey(GroupID)){
                GroupThread gp = GroupWarList.get(GroupID);
                if(message.equals(gp.message)){
                    Map<Long,Integer> idlist = gp.getIDlist();
                    int i = idlist.get(ID);
                    idlist.put(ID,i+1);
                    System.out.println("�ӷ֣�"+(i+1));
                }
                else if(message.equals("#ս����Ա")){
                    Map<Long,Integer> idlist = gp.getIDlist();
                    String number = "";
                    for(Long k:idlist.keySet()){
                        number += "�û�Q�ţ�"+k+"\n";
                    }
                    number += "��"+idlist.size()+"����Ա׼������ս��";
                    event.respond(number);
                }
            }
        }catch (Exception e){event.respond("δ֪ԭ�򣬲���ʧ��");}
    }
}