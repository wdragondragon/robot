package ConDatebase;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventMessage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OneUserNum extends IcqListener {
    public static String url="jdbc:mysql://localhost:3306/students?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";//连接数据库的url，test是我自己的一个数据库啊宝宝们。
    public  static String user="root";//mysql登录名
    public  static String pass="951753";//mysql登录密码
    String getComArti(String username,Long QQnum){
        String sql = "SELECT * FROM client where username=?";
        String respond = "[CQ:at,qq="+QQnum+"]\n";
        try {
            Connection con = Conn.getConnection(url, user, pass);
            PreparedStatement ptmt = con.prepareStatement(sql);
            ptmt.setString(1, username);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) {
                int all = rs.getInt("num");
                int right = rs.getInt("rightnum");
                int mis = rs.getInt("misnum");
                int datenum = rs.getInt("datenum");
                double aver = rs.getInt("aver");
                String online = rs.getInt("online")==1?"在线":"不在线";
                respond += username+" 总:"+all+" 对:"+right+" 错:"+mis+" 今日:"+datenum+"\n平均成绩:"+aver+" 在线状态:"+online;
            } else {
                respond += "该用户不存在";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return respond;
    }

    @EventHandler
    public void onPMEvent(EventMessage event){
        String message = event.getMessage();
        String s[] = message.split(" ");
        Long QQnum = event.getSenderId();
        if(s.length==2&&s[0].equals("查")){
            event.respond(getComArti(s[1],QQnum));
        }
    }
}
