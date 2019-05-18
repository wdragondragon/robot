package ConDatebase;

import java.sql.*;
import java.util.Calendar;

public class Conn  {
    private static String url="jdbc:mysql://localhost:3306/students?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";//�������ݿ��url��test�����Լ���һ�����ݿⰡ�����ǡ�
    private static String user="root";//mysql��¼��
    private static String pass="951753";//mysql��¼����
    public static Connection getConnection(){
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
//            System.out.print("���ݿ����ӳɹ�\r");
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
