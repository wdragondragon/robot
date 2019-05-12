package ConDatebase;


import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventMessage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class ComArti extends IcqListener {
    private static String url="jdbc:mysql://localhost:3306/students?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";//连接数据库的url，test是我自己的一个数据库啊宝宝们。
    private static String user="root";//mysql登录名
    private static String pass="951753";//mysql登录密码
    private String getComArti(Date date){
        String sql = "SELECT * FROM everydaysaiwen where saiwendate=?";
        String respond = "";
        try {
            Connection con = Conn.getConnection(url, user, pass);
            PreparedStatement ptmt = con.prepareStatement(sql);
            ptmt.setDate(1, date);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) {
                respond = date + "拖拉机赛文-" + rs.getString("author") + "\n";
                respond += rs.getString("saiwen") + "\n";
                respond += "-----第0段-共" + rs.getString("saiwen").length() + "字";
            } else {
                respond = "没有这一天的赛文";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return respond;
    }
    private String AddZero(String str){
        String time[] = str.split("-");
        if(time[1].length()==1){time[1]="0"+time[1];}
        if(time[2].length()==1){time[2]="0"+time[2];}
        return  time[0]+"-"+time[1]+"-"+time[2];

    }
    private Date getdate(){
        int y,m,d;
        Calendar cal;
        cal= Calendar.getInstance();
        y=cal.get(Calendar.YEAR);
        m=cal.get(Calendar.MONTH);
        d=cal.get(Calendar.DATE);
        return new Date(y-1900,m,d);
    }

    @EventHandler
    public void onPMEvent(EventMessage event){
        String message = event.getMessage();
        String s[] = message.split(" ");
        Long QQnum = event.getSenderId();

        s[1] = AddZero(s[1]);
        if(s.length==2&&s[0].equals("赛文")){
            if(getdate().toString().equals(s[1])){
                event.respond("[CQ:at,qq="+QQnum+"]\n"+"不能获取今日赛文");
                return;
            }
            Date date = Date.valueOf(s[1]);

            event.respond(getComArti(date));
        }
    }
}
