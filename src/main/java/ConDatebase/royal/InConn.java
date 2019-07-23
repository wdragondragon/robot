package ConDatebase.royal;

import ConDatebase.Conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class InConn {
    public static boolean addTribe(Long id,String name,String message,int orlevel,int ownlevel){
        try{
            delete(id);
            String sql = "insert into royal value(?,?,?,default,?,?,default)";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ptmt.setString(2,name);
            ptmt.setString(3,message);
            ptmt.setInt(4,orlevel);
            ptmt.setInt(5,ownlevel);
            ptmt.execute();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static String waitallow(){
        String message = "等待允许挂招部落\n";
        try{
            String sql = "select * from royal where allow=0";
            Connection con = Conn.getConnection();
            ResultSet rs = Conn.getStmtSet(con,sql);
            while(rs.next()){
                message += "Q号：+"+rs.getLong("id")+
                        "\n部落名："+rs.getString("name")+"\n";
            }
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }
    public static boolean changeallow(Long id,int i){
        try{
            String sql = "update royal set allow=? where id=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setInt(1,i);
            ptmt.setLong(2,id);
            ptmt.execute();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isAllow(Long id){
        try{
            String sql = "select allow from royal where id=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()) {
                if (rs.getInt("allow") == 1)
                    return true;
                else
                    return false;
            }else
                return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static String getAllmessage(){
        String message = "";
        try{
            String sql = "select * from royal where allow=1 order by ownlevel asc";
            Connection con = Conn.getConnection();
            ResultSet rs = Conn.getStmtSet(con,sql);
            while(rs.next()){
                message += "部落名字：" + rs.getString("name") +
                        " 要求杯数：" + String.valueOf(rs.getInt("orlevel")) +
                        " 部落总杯：" + String.valueOf(rs.getInt("ownlevel")) +"\n";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }
    public static String getMessage(Long id){
        String message = "";
        int time;
        try{
            if(isAllow(id)) {
                String sql = "select * from royal where id=?";
                Connection con = Conn.getConnection();
                PreparedStatement ptmt = Conn.getPtmt(con, sql);
                ptmt.setLong(1, id);
                ResultSet rs = ptmt.executeQuery();
                if (rs.next()) {
                    time = rs.getInt("time");
                    if(time>0) {
                        message = "部落名字：" + rs.getString("name") +
                                "\n招人信息：" + rs.getString("message") +
                                "\n要求杯数：" + String.valueOf(rs.getInt("orlevel")) +
                                "\n部落总杯：" + String.valueOf(rs.getInt("ownlevel")) +
                                "\n今日剩余发送次数：" + String.valueOf(time);
                        updatetime(con,id,time);
                    }
                    else
                        message = "当日发送次数不足";
                }
            }else{
                message = "未通过群主考核，无法发送招人信息";
            }
        }catch (Exception e){
            e.printStackTrace();
            message = "处理出错";
        }
        return message;
    }
    public static boolean delete(Long id){
        try{
            String sql = "delete from royal where id=?";
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setLong(1,id);
            ptmt.execute();
            return true;
        }catch (Exception e){return false;}
    }
    public static void updatetime(Connection con,Long id,int time){
        try {
            String sql = "update royal set time=? where id=?";
            PreparedStatement ptmt = Conn.getPtmt(con, sql);
            ptmt.setInt(1, time - 1);
            ptmt.setLong(2, id);
            ptmt.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void Freshdatetime(){
        try{
            String sql = "update royal set time=5";
            Connection con = Conn.getConnection();
            Conn.execute(con,sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
