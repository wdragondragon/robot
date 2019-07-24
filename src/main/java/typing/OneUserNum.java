package typing;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventMessage;
import typing.ConDatabase.Conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OneUserNum extends IcqListener {
    String getComArti(String username,Long QQnum){
        String sql = "SELECT a.*,(SELECT COUNT(DISTINCT aver) FROM client AS b WHERE a.aver<b.aver)+1 as rank FROM client as a where a.username=?";
        String respond = "[CQ:at,qq="+QQnum+"]\n";
        try {
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setString(1, username);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) {
                int all = rs.getInt("num");
                int right = rs.getInt("rightnum");
                int mis = rs.getInt("misnum");
                int datenum = rs.getInt("datenum");
                int rank = rs.getInt("rank");
                double aver = rs.getInt("aver");
                String online = rs.getInt("online")==1?"在线":"不在线";
                int n = rs.getInt("n");
                respond += "用户名："+username+"\n字数情况：总："+all+" 对："+right+" 错："+mis+" 今日："+datenum+
                        "\n赛文情况：累计跟打"+n+"次 平均成绩："+aver+" 排名："+rank+
                        "\n在线状态："+online;
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
        if(s.length==2&&s[0].equals("拖拉机")){
            event.respond(getComArti(s[1],QQnum));
        }
    }
}
