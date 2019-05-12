package ConDatebase;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conn  {
    public static Connection getConnection(String url,String user,String pass){
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            System.out.print("数据库连接成功\r");
        }catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
}
