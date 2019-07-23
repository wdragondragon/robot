package ConDatebase;

import java.sql.*;
import java.util.Calendar;

public class Conn  {
    public static Connection getConnection(){
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //mysql登录名
            String user = "root";
            //连接数据库的url，test是我自己的一个数据库啊宝宝们。
            String url = "jdbc:mysql://localhost:3306/students?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";
            //mysql登录密码
            String pass = "951753";
            con = DriverManager.getConnection(url, user, pass);
//            System.out.print("数据库连接成功\r");
        }catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
    public static PreparedStatement getPtmt(Connection con,String sql){
        PreparedStatement ptmt = null;
        try {
            ptmt = con.prepareStatement(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ptmt;
    }
    public static ResultSet getStmtSet(Connection con,String sql){
        ResultSet rs = null;
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }
    public static void execute(Connection con,String sql){
        try{
            Statement stmt = con.createStatement();
            stmt.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static  Date getdate(){
        int y,m,d;
        Calendar cal;
        cal= Calendar.getInstance();
        y=cal.get(Calendar.YEAR);
        m=cal.get(Calendar.MONTH);
        d=cal.get(Calendar.DATE);
        return new Date(y-1900,m,d);
    }
}
