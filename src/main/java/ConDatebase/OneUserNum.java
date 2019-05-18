package ConDatebase;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventMessage;

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
                String online = rs.getInt("online")==1?"����":"������";
                int n = rs.getInt("n");
                respond += "�û�����"+username+"\n����������ܣ�"+all+" �ԣ�"+right+" ��"+mis+" ���գ�"+datenum+
                        "\n����������ۼƸ���"+n+"�� ƽ���ɼ���"+aver+" ������"+rank+
                        "\n����״̬��"+online;
            } else {
                respond += "���û�������";
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
        if(s.length==2&&s[0].equals("������")){
            event.respond(getComArti(s[1],QQnum));
        }
    }
}
