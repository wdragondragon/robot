package typing.ConDatabase;

import typing.Tools.RegexText;
import typing.Tools.initGroupList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class OutConn {
    public static String insteadName(Long id){
        try{
            String sql = "select name from robotclient where id=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ResultSet rs = ptmt.executeQuery();
            con.close();
            if(rs.next()){
                return rs.getString("name");
            }
            else{
                return "未设名";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "获取错误";
        }
    }
    public static String ShowName(String name){
        try {
            String sql = "select * from robotclient where name=?";
            String sql1 = "select * from robotclient where id=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con, sql);
            ptmt.setString(1, name);
            ResultSet rs = ptmt.executeQuery();
            con.close();
            if(rs.next())
                return getShowNameStr(rs);
            else{
                ptmt = Conn.getPtmt(con,sql1);
                ptmt.setLong(1,Long.parseLong(name));
                rs = ptmt.executeQuery();
                if(rs.next())
                    return getShowNameStr(rs);
                else
                    return "获取失败，无收录成绩";
            }
        }catch (Exception e){
//            e.printStackTrace();
            return "获取失败，无收录成绩";
        }
    }
    private static String getShowNameStr(ResultSet rs){
        try {
            String name = rs.getString("name");
            if(name==null)name="（未设置名片） ";
            return "用户名："+ name +" Q号："+rs.getLong("id")+
                    "\n上屏赛文成绩次数："+rs.getInt("n")+
                    "\n平均速度："+ RegexText.FourOutFiveIn(rs.getDouble("speedaver"))+
                    " 平均击键："+RegexText.FourOutFiveIn(rs.getDouble("keyspeedaver"))+
                    " 平均码长："+RegexText.FourOutFiveIn(rs.getDouble("keylengthaver"));
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
                    message = title+"\n"+saiwen+"\n-----第1段-共"+saiwen.length()+"字";
                }
                else
                    message = groupname+"在这一天没有赛文";
            }
            else
                message =  "没有"+groupname+"这个群";
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }
    public static String ShowGroupSaiwenMath(String groupname,Date date){
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
                sql = "select * from groupsaiwenmax where groupid=? and date=? order by speed DESC";
                ptmt = Conn.getPtmt(conn,sql);
                System.out.println(groupid+":"+date);
                ptmt.setLong(1,groupid);
                ptmt.setDate(2,date);
                rs = ptmt.executeQuery();
                int i = 0;
                while(rs.next()){
                    i++;
                    String name = initGroupList.GroupMemberCardMap.get(groupid).get(rs.getLong("id"));
                    if(name.equals(""))
                        name = ""+rs.getLong("id");
                    message += i+ " "+rs.getDouble("speed")+" "+
                            rs.getDouble("keyspeed")+" "+
                            rs.getDouble("keylength")+"：" +name + "\n";
                    System.out.println(i);
                }
                if(i==0)
                    message = groupname+"在这一天没有赛文成绩";
            }
            else
                message =  "没有"+groupname+"这个群";
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }
    public static String ShowGroupIdMath(Long id,String groupname,Date date,String idname) {
        boolean have = false;
        String message = date+"\n用户名："+idname+" 群："+groupname;
//        String sql = "select id from robotclient where name=?";
        Long groupid;
        try{
            Connection con = Conn.getConnection();
            String sql = "select groupid from groupmap where groupname=?";
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setString(1,groupname);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next())groupid=rs.getLong("groupid");
            else{ con.close();return "无该群映射列表，发送指令（群映射列表）来查看可以查询的群名";}
            sql = "select * from robothistory where id=? and groupid=? and date=?";
            ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ptmt.setLong(2,groupid);
            ptmt.setDate(3,date);
            rs = ptmt.executeQuery();
            while(rs.next()){
                message += "\n速度："+RegexText.FourOutFiveIn(rs.getDouble("speed"))+
                                " 击键："+RegexText.FourOutFiveIn(rs.getDouble("keyspeed"))+
                                " 码长："+RegexText.FourOutFiveIn(rs.getDouble("keylength"));
                have = true;
            }
            if(have==false)
                message = "无收录成绩";
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  message;
    }
    public static HashMap<Long,Boolean> getGroupList(){
        HashMap<Long,Boolean> grouplist = new HashMap();
        String sql  = "select * from groupmap";
        try{
            ResultSet rs = Conn.getStmtSet(Conn.getConnection(),sql);
            while(rs.next()){
                long groupid = rs.getLong("groupid");
                grouplist.put(groupid,false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return grouplist;
    }
    public static String tljinfo(){
        String message = "";
        String sql = "select * from history";
        String sql1 = "select * from client";
        int wordnum = 0;
        double time = 0;
        int num = 0;
        int datenum = 0;
        int n = 0;
        int people = 0;
        int year;
        int month;
        int day;
        int hour;
        int minte;
        int second;
        try{
            Connection con = Conn.getConnection();
            ResultSet rs = Conn.getStmtSet(con,sql);
            while(rs.next()){
                wordnum += rs.getInt("number");
                time += rs.getDouble("time");
                n++;
            }
            rs = Conn.getStmtSet(con,sql1);
            while(rs.next()){
                num += rs.getInt("num");
                datenum += rs.getInt("datenum");
                people++;
            }
            year = (int)time/(60*60*24*30*12);
            month = ((int)time/(60*60*24*30))%12;
            day = ((int)time/(60*60*24))%30;
            hour = ((int)time/(60*60))%60;
            minte = ((int)time/60)%60;
            second = ((int)time)%60;
            message = "拖拉机详情：\n注册有"+people+"个账号" +
                    "\n跟打器跟打总字数：" + num +
                    "\n跟打器今日跟打总字数：" + datenum +
                    "\n全体平均速度：" + String.format("%.2f",(wordnum/time)*60) +
                    "\n全体共跟打次数：" + n +
                    "\n全体在跟打上累计的有效时间：" + String.format("%.2f",time) + "秒"+
                    "\n换算时间："+year+"年"+month+"月"+day+"天"+hour+"时"+minte+"分"+second+"秒";
            con.close();
        }catch (Exception e){e.printStackTrace();}
        return message;
    }
}
