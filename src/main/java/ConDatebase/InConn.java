package ConDatebase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            String sql = "select * from groupsaiwenMax where id=? and groupid=? and date=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ptmt.setLong(2,groupid);
            ptmt.setDate(3,Conn.getdate());
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){
                double speed = rs.getDouble("speed");
                if(speed<Com[0]){
                    sql = "update groupsaiwenMax set speed=?,keyspeed=?,keylength=? where id=? and groupid=? and date=?";
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
                sql = "insert into groupsaiwenMax values(?,?,?,?,?,?)";
                ptmt = Conn.getPtmt(con,sql);
                ptmt.setLong(1,id);
                ptmt.setLong(2,groupid);
                ptmt.setDate(3,Conn.getdate());
                ptmt.setDouble(4,Com[0]);
                ptmt.setDouble(5,Com[1]);
                ptmt.setDouble(6,Com[2]);
                ptmt.execute();
                System.out.println("添加成绩"+id+" 群"+groupid);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static  void AddRobotHistory(Long id,Long groupid,double[] Com){
        try {
            String sql1 = "insert into robothistory values(?,?,?,?,?,default,?)";
            String sql2 = "SELECT * FROM robotclient where id=" + id;
            String sql3 = "insert into robotclient value(?,default,?,?,?,?)";
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
            } else{
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
    public static String  setName(String name,Long id){
        try{
            String sql = "update robotclient set name=? where id=?";
            String sql1 = "select * from robotclient where name=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql1);
            ptmt.setString(1,name);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next())return "已存在相同用户名 Q号为："+rs.getLong("id");
            ptmt = Conn.getPtmt(con,sql);
            ptmt.setString(1,name);
            ptmt.setLong(2,id);
            return "更改成功 账号："+id+" 名片："+name;
        }catch (Exception e){
            return "更改失败，先打一篇赛文上屏成绩";
        }
    }
}
