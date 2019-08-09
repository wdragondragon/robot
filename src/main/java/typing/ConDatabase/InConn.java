package typing.ConDatabase;

import typing.Tools.initGroupList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class InConn {
    public static void AddGroupSaiwen(Long groupid,String []Com){
        try{
            String sql = "SELECT * FROM groupsaiwen where groupid=? and saiwendate=?";
            Date date = Conn.getdate();
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,groupid);
            ptmt.setDate(2,date);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){
                con.close();
                return;
            }
            else{
                sql = "insert into groupsaiwen value(?,?,?,?)";
                ptmt = Conn.getPtmt(con,sql);
                ptmt.setLong(1,groupid);
                ptmt.setDate(2,date);
                ptmt.setString(3,Com[1]);
                ptmt.setString(4,Com[0]);
                ptmt.execute();
                con.close();
                System.out.println("添加赛文:"+groupid+"日期:"+ Conn.getdate());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void addMaxComMath(Long id,Long groupid,double[] Com){
        try{
            String sql = "select * from groupsaiwenmax where id=? and groupid=? and date=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ptmt.setLong(2,groupid);
            ptmt.setDate(3,Conn.getdate());
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){
                double speed = rs.getDouble("speed");
                if(speed<Com[0]){
                    sql = "update groupsaiwenmax set speed=?,keyspeed=?,keylength=? where id=? and groupid=? and date=?";
                    ptmt = Conn.getPtmt(con,sql);
                    ptmt.setDouble(1,Com[0]);
                    ptmt.setDouble(2,Com[1]);
                    ptmt.setDouble(3,Com[2]);
                    ptmt.setLong(4,id);
                    ptmt.setLong(5,groupid);
                    ptmt.setDate(6,Conn.getdate());
                    ptmt.execute();
                    System.out.println("更改成绩"+id+" 群"+groupid);
                }
            }
            else{
                sql = "insert into groupsaiwenmax values(?,?,?,?,?,?)";
                ptmt = Conn.getPtmt(con,sql);
                ptmt.setLong(1,groupid);
                ptmt.setLong(2,id);
                ptmt.setDate(3,Conn.getdate());
                ptmt.setDouble(4,Com[0]);
                ptmt.setDouble(5,Com[1]);
                ptmt.setDouble(6,Com[2]);
                ptmt.execute();
                System.out.println("添加成绩"+id+" 群"+groupid);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static  void AddRobotHistory(Long id,Long groupid,double[] Com){
        try {
            String sql1 = "insert into robothistory values(?,?,?,?,?,default,?)";
            String sql2 = "SELECT * FROM robotclient where id=" + id;
            String sql3 = "insert into robotclient values(?,default,?,?,?,?,default)";
            String sql4 = "update robotclient set n=?,speedaver=?,keyspeedaver=?,keylengthaver=? where id=?";
            Connection con = Conn.getConnection();
            ResultSet rs = Conn.getStmtSet(con,sql2);
            PreparedStatement ptmt;
            if (rs.next()){
                ptmt = Conn.getPtmt(con,sql4);
                int n = rs.getInt("n");
                double speedaver = rs.getDouble("speedaver");
                double keyspeedaver = rs.getDouble("keyspeedaver");
                double keylengthaver = rs.getDouble("keylengthaver");
                ptmt.setInt(1,n+1);
                ptmt.setDouble(2,(speedaver*n+Com[0])/(n+1));
                ptmt.setDouble(3,(keyspeedaver*n+Com[1])/(n+1));
                ptmt.setDouble(4,(keylengthaver*n+Com[2])/(n+1));
                ptmt.setLong(5,id);
                ptmt.execute();
            } else {
                ptmt = Conn.getPtmt(con,sql3);
                ptmt.setLong(1,id);
                ptmt.setInt(2,1);
                ptmt.setDouble(3,Com[0]);
                ptmt.setDouble(4,Com[1]);
                ptmt.setDouble(5,Com[2]);
                ptmt.execute();
            }
            ptmt = Conn.getPtmt(con,sql1);
            ptmt.setLong(1,id);
            ptmt.setLong(2,groupid);
            ptmt.setDouble(3,Com[0]);
            ptmt.setDouble(4,Com[1]);
            ptmt.setDouble(5,Com[2]);
            ptmt.setDate(6,Conn.getdate());
            ptmt.execute();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void AddGroupCheatNum(int num,long id){
        String sql = "select * from robotclient where id="+id;
        try{
            Connection con = Conn.getConnection();
            ResultSet rs = Conn.getStmtSet(con,sql);
            if(rs.next()){
                sql = "update robotclient set cheatnum="
                        +(rs.getInt("cheatnum")+num)
                        +" where id="+id;
                Conn.execute(con,sql);
            }else{
                sql = "insert into robotclient values(?,default,default,default,default,default,default)";
                PreparedStatement ptmt = Conn.getPtmt(con,sql);
                ptmt.setLong(1,id);
                ptmt.execute();
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String AddAllGroupSaiwenDate(String date,String title,String saiwen,long id){
        String sql = "select * from allgroupsaiwen where saiwendate=?";
        try{
            Connection con = Conn.getConnection();
            PreparedStatement ptst = Conn.getPtmt(con,sql);
            ptst.setDate(1,Date.valueOf(date));
            ResultSet rs = ptst.executeQuery();
            if(rs.next()){
                return "该日期已有赛文";
            }else{
                sql = "insert into allgroupsaiwen values(?,?,?,?)";
                ptst = Conn.getPtmt(con,sql);
                ptst.setDate(1,Date.valueOf(date));
                ptst.setString(2,title);
                ptst.setString(3,saiwen);
                ptst.setLong(4,id);
                ptst.execute();
                con.close();
                return "发布成功";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "未知原因发布失败";
        }
    }
    public static String AddAllGroupSaiwen(String title,String saiwen,long id){
        String sql = "select * from allgroupsaiwen ORDER BY saiwendate DESC";
        try{
            Connection con = Conn.getConnection();
            ResultSet rs = Conn.getStmtSet(con,sql);
            Date date ;
            if(rs.next()){
                date = rs.getDate("saiwendate");
                if(Conn.getdate().getTime()>date.getTime())
                    date = Conn.getdate();
                else {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    calendar.add(calendar.DATE,1);
                    java.util.Date date1 =  calendar.getTime();
                    date = new Date(date1.getTime());
                }
            }
            else date = Conn.getdate();
            sql = "insert into allgroupsaiwen values(?,?,?,?)";
            PreparedStatement ptst = Conn.getPtmt(con,sql);
            ptst.setDate(1,date);
            ptst.setString(2,title);
            ptst.setString(3,saiwen);
            ptst.setLong(4,id);
            ptst.execute();
            con.close();
            return "发布成功赛文日期："+ date;
//            }
        }catch (Exception e){
            e.printStackTrace();
            return "未知原因发布失败";
        }
    }
    public static String ChangeAllGroupSaiwen(String date,String title,String saiwen,long id){
        String sql = "select * from allgroupsaiwen where saiwendate=?";
        String message = "";
        try{
            Connection con = Conn.getConnection();
            PreparedStatement ptst = Conn.getPtmt(con,sql);
            ptst.setDate(1,Date.valueOf(date));
            ResultSet rs = ptst.executeQuery();
            if(rs.next()){
                if(rs.getLong("author")==id){
                    sql = "update allgroupsaiwen set title=?,saiwen=? where saiwendate=?";
                    ptst = Conn.getPtmt(con,sql);
                    ptst.setString(1,title);
                    ptst.setString(2,saiwen);
                    ptst.setDate(3,Date.valueOf(date));
                    ptst.execute();
                    message =  "修改成功";
                }else {
                    message =  "不是你发布的赛文，你无法修改";
                }
            }else{
                message =  "该天无赛文，无法修改";
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
            message =  "未知原因发布失败";
        }
        return message;
    }
    public static String AddRobotSaiwen(String type,String title,String saiwen,long id,String nickname) {
        String sql = "select * from robotsaiwen where type=? ORDER BY aid DESC";
        try {
            Connection con = Conn.getConnection();
            PreparedStatement ptmt =  Conn.getPtmt(con,sql);
            ptmt.setString(1,type);
            ResultSet rs = ptmt.executeQuery();
            int aid = 0;
            if(rs.next())
                aid = rs.getInt("aid");
            sql = "insert into robotsaiwen values(?,?,?,?,?,?)";
            ptmt = Conn.getPtmt(con,sql);
            ptmt.setInt(1,++aid);
            ptmt.setString(2,title);
            ptmt.setString(3,saiwen);
            ptmt.setString(4,nickname);
            ptmt.setLong(5,id);
            ptmt.setString(6,type);
            ptmt.execute();
            con.close();
            return "发布成功文章编号：" + type+" "+aid;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return "未知原因发布失败";
        }
    }
    public static void addMaxAllGroupComMath(Long id,Long groupid,double[] Com,String name){
        try{
            String sql = "select * from allgroupsaiwenchengji where id=? and saiwendate=?";
            String username = initGroupList.GroupMemberCardMap.get(groupid).get(id);
            if(username.equals("")||username==null)username = name;
            Connection con = Conn.getConnection();
            Date date = Conn.getdate();
            System.out.println(date);
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ptmt.setDate(2,date);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){
                double speed = rs.getDouble("sudu");
                int num = rs.getInt("typenum");
                if(speed<Com[0]){
                    sql = "update allgroupsaiwenchengji set sudu=?,keyspeed=?,keylength=?,groupid=?,typenum=?,name=?,del=?,deletetext=?,sel=?,mistake=?,rightkeyper=? where id=? and saiwendate=?";
                    ptmt = Conn.getPtmt(con,sql);
                    ptmt.setDouble(1,Com[0]);
                    ptmt.setDouble(2,Com[1]);
                    ptmt.setDouble(3,Com[2]);
                    ptmt.setLong(4,groupid);
                    ptmt.setInt(5,num+1);
                    ptmt.setString(6,username);
                    ptmt.setInt(7,(int)Com[3]);
                    ptmt.setInt(8,(int)Com[4]);
                    ptmt.setInt(9,(int)Com[5]);
                    ptmt.setInt(10,(int)Com[6]);
                    ptmt.setDouble(11,Com[7]);
                    ptmt.setLong(12,id);
                    ptmt.setDate(13,date);
                    System.out.println("更改联赛成绩"+id+" "+groupid);
                    ptmt.execute();
                }else{
                    sql = "update allgroupsaiwenchengji set typenum=? where id=? and saiwendate=?";
                    ptmt = Conn.getPtmt(con,sql);
                    ptmt.setInt(1,num+1);
                    ptmt.setLong(2,id);
                    ptmt.setDate(3,Conn.getdate());
                    ptmt.execute();
                }
            }
            else{
                sql = "insert into allgroupsaiwenchengji values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ptmt = Conn.getPtmt(con,sql);
                ptmt.setLong(1,id);
                ptmt.setLong(2,groupid);
                ptmt.setDate(3,Conn.getdate());
                ptmt.setDouble(4,Com[0]);
                ptmt.setDouble(5,Com[1]);
                ptmt.setDouble(6,Com[2]);
                ptmt.setInt(7,1);
                ptmt.setString(8,username);
                ptmt.setInt(9,(int)Com[3]);
                ptmt.setInt(10,(int)Com[4]);
                ptmt.setInt(11,(int)Com[5]);
                ptmt.setInt(12,(int)Com[6]);
                ptmt.setDouble(13,Com[7]);
                ptmt.setDouble(14,Com[0]);
                ptmt.execute();
                System.out.println("添加联赛成绩"+id+" 群"+groupid);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String addRobotSaiwenMath(Long id,Long groupid,double[] Com,String name,String typeandaid){
        String message = "";
        String []s = typeandaid.split("%");
        String type = s[0];
        int aid = Integer.valueOf(s[1]);
        try{
            String username = initGroupList.GroupMemberCardMap.get(groupid).get(id);
            if (username.equals("") || username == null)
//              String
                      username = name;
            String sql = "select * from robotsaiwenchengji where aid=? and id=? and type=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setInt(1,aid);
            ptmt.setLong(2,id);
            ptmt.setString(3,type);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){
                double speed = rs.getDouble("sudu");
                int num = rs.getInt("typenum");
                if(speed<Com[0]) {
                    sql = "update robotsaiwenchengji set sudu=?,keyspeed=?,keylength=?,groupid=?,typenum=?,name=?,del=?,deletetext=?,sel=?,mistake=?,rightkeyper=? where id=? and aid=? and type=?";
                    ptmt = Conn.getPtmt(con, sql);
                    ptmt.setDouble(1, Com[0]);
                    ptmt.setDouble(2, Com[1]);
                    ptmt.setDouble(3, Com[2]);
                    ptmt.setLong(4, groupid);
                    ptmt.setInt(5, rs.getInt("typenum") + 1);
                    ptmt.setString(6, username);
                    ptmt.setInt(7, (int) Com[3]);
                    ptmt.setInt(8, (int) Com[4]);
                    ptmt.setInt(9, (int) Com[5]);
                    ptmt.setInt(10, (int) Com[6]);
                    ptmt.setDouble(11, Com[7]);
                    ptmt.setLong(12, id);
                    ptmt.setInt(13, aid);
                    ptmt.setString(14,type);
                    System.out.println("更改联赛成绩" + id + " " + groupid);
                    ptmt.execute();
                }else {
                    sql = "update robotsaiwenchengji set typenum=? where id=? and aid=? and type=?";
                    ptmt = Conn.getPtmt(con,sql);
                    ptmt.setInt(1,num+1);
                    ptmt.setLong(2,id);
                    ptmt.setInt(3,aid);
                    ptmt.setString(4,type);
                    ptmt.execute();
                }
            }else {
                sql = "insert into robotsaiwenchengji values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ptmt = Conn.getPtmt(con, sql);
                ptmt.setLong(1, id);
                ptmt.setLong(2, groupid);
                ptmt.setDouble(3, Com[0]);
                ptmt.setDouble(4, Com[1]);
                ptmt.setDouble(5, Com[2]);
                ptmt.setInt(6, 1);
                ptmt.setString(7, username);
                ptmt.setInt(8, (int) Com[3]);
                ptmt.setInt(9, (int) Com[4]);
                ptmt.setInt(10, (int) Com[5]);
                ptmt.setInt(11, (int) Com[6]);
                ptmt.setDouble(12, Com[7]);
                ptmt.setInt(13, aid);
                ptmt.setString(14,type);
                ptmt.execute();
            }
            con.close();
            message = aid + "编号成绩提交成绩成功";
        }catch (Exception e){
            message = aid+"编号成绩提交成绩失败，请备份成绩与管理员联系。";
            e.printStackTrace();
        }
        return message;
    }
    public static String  setName(String name,Long id){
        try{
            String sql = "update robotclient set name=? where id=?";
            String sql1 = "select * from robotclient where name=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql1);
            ptmt.setString(1,name);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){con.close();return "已存在相同用户名 Q号为："+rs.getLong("id");}
            ptmt = Conn.getPtmt(con,sql);
            ptmt.setString(1,name);
            ptmt.setLong(2,id);
            ptmt.execute();
            con.close();
            return "更改成功 账号："+id+" 名片："+name;
        }catch (Exception e){
            return "更改失败，先打一篇赛文上屏成绩";
        }
    }
}
