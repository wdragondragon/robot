package ConDatebase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class ComArti {

    private static String getComArti(Date date){
        String sql = "SELECT * FROM everydaysaiwen where saiwendate=?";
        String respond = "";
        try {
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setDate(1, date);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) {
                respond = date + "����������-" + rs.getString("author") + "\n";
                respond += rs.getString("saiwen") + "\n";
                respond += "-----��0��-��" + rs.getString("saiwen").length() + "��";
            } else {
                respond = "û����һ�������";
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return respond;
    }
    private static String getComMath(Date date){
        String sql = "select * from saiwenchengji where date=? order by sudu DESC";
        String respond = "";
        boolean sign = false;
        try {
            Connection con = Conn.getConnection();
            PreparedStatement ptst = Conn.getPtmt(con,sql);
            ptst.setDate(1, date);
            ResultSet rs = ptst.executeQuery();
            while(rs.next()){
                String name = rs.getString("name");
                Double sudu = rs.getDouble("sudu");
                respond += name + " "+sudu+"\n";
                sign = true;
            }
            if(!sign)respond = "�޸������ĳɼ�";

        }catch (Exception e){
            e.printStackTrace();
        }
        return respond;
    }

    public static String  responseStr(String s,Long QQnum,Date date,int model){
        if(Conn.getdate().toString().equals(s)&&model==1)
            return "[CQ:at,qq="+QQnum+"]\n"+"���ܻ�ȡ��������";
        else if(model==1)
            return getComArti(date);
        else
            return getComMath(date);
    }
}
