package ConDatebase;

import Tool.RegexText;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OutConn {
    public static String ShowName(String name){
        try {
            String sql = "select * from robotclient where name=?";
            String sql1 = "select * from robotclient where id=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con, sql);
            ptmt.setString(1, name);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next())
                return getShowNameStr(rs);
            else{
                ptmt = Conn.getPtmt(con,sql1);
                ptmt.setLong(1,Long.parseLong(name));
                rs = ptmt.executeQuery();
                if(rs.next())
                    return getShowNameStr(rs);
                else
                    return "��ȡʧ�ܣ��޳ɼ��������ƣ��ȴ�һƪ���������ɼ����Կ��ɣ�����Ⱥ��Ƭ������Q�Ų�ѯŶ";
            }
        }catch (Exception e){
//            e.printStackTrace();
            return "��ȡʧ�ܣ��޳ɼ��������ƣ��ȴ�һƪ���������ɼ����Կ��ɣ�����Ⱥ��Ƭ������Q�Ų�ѯŶ";
        }
    }
    private static String getShowNameStr(ResultSet rs){
        try {
            String name = rs.getString("name");
            if(name==null)name="��δ������Ƭ�� ";
            return "�û�����"+name+" Q�ţ�"+rs.getLong("id")+
                    "\n�������ĳɼ�������"+rs.getInt("n")+
                    "\nƽ���ٶȣ�"+ RegexText.FourOutFiveIn(rs.getDouble("speedaver"))+
                    " ƽ��������"+RegexText.FourOutFiveIn(rs.getDouble("keyspeedaver"))+
                    " ƽ���볤��"+RegexText.FourOutFiveIn(rs.getDouble("keylengthaver"));
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String ShowGroupSaiwen(String groupname,Date date){
        String message = "";
        Long groupid;
        try{
            String sql = "select groupid from groupmap where groupname=?";
            Connection conn = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(conn,sql);
            ptmt.setString(1,groupname);
            ResultSet rs = ptmt.executeQuery();

            if(rs.next()) {
                groupid = rs.getLong("groupid");
                sql = "select * from groupsaiwen where groupid=? and saiwendate=?";
                ptmt = Conn.getPtmt(conn,sql);
                ptmt.setLong(1,groupid);
                ptmt.setDate(2,date);
                rs = ptmt.executeQuery();
                if(rs.next()){
                    String title = rs.getString("title");
                    String saiwen = rs.getString("saiwen");
                    message = title+"\n"+saiwen+"\n-----��1��-��"+saiwen.length()+"��";
                }
                else
                    message = groupname+"����һ��û������";
            }
            else
                message =  "û��"+groupname+"���Ⱥ";
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }

    public static String ShowGroupIdMath(String idname,String groupname,Date date) {
        boolean have = false;
        String message = date+"\n�û�����"+idname+" Ⱥ��"+groupname;
        String sql = "select id from robotclient where name=?";
        Long id,groupid;
        try{
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setString(1,idname);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next())id=rs.getLong("id");
            else return "�޸���Ƭ������ָ�������Ƭ ���֣���������Ƭ������������Ƭ���볢�Ը���һ�����Ĳ������ɼ�";

            sql = "select groupid from groupmap where groupname=?";
            ptmt = Conn.getPtmt(con,sql);
            ptmt.setString(1,groupname);
            rs = ptmt.executeQuery();
            if(rs.next())groupid=rs.getLong("groupid");
            else return "�޸�Ⱥӳ���б�����ָ�Ⱥӳ���б����鿴���Բ�ѯ��Ⱥ��";


            sql = "select * from robothistory where id=? and groupid=? and date=?";
//            sql = "select * from robothistory " +
//                    "where id=" +
//                    "(select id from robotclient where name=?)" +
//                    " and groupid=" +
//                    "(select groupid from groupmap where groupname=?)" +
//                    " and date=?;";
            ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ptmt.setLong(2,groupid);
            ptmt.setDate(3,date);
            rs = ptmt.executeQuery();
            while(rs.next()){
                message += "\n�ٶȣ�"+RegexText.FourOutFiveIn(rs.getDouble("speed"))+
                                " ������"+RegexText.FourOutFiveIn(rs.getDouble("keyspeed"))+
                                    " �볤��"+RegexText.FourOutFiveIn(rs.getDouble("keylength"));
                have = true;
            }
            if(have==false)message = idname+"��"+date+"��"+groupname+"�ĳɼ�";
        }catch (Exception e){
            e.printStackTrace();
        }
        return  message;
    }
}
