package typing.ConDatabase;

import typing.Tools.Createimg;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                respond = date + "拖拉机赛文-" + rs.getString("author") + "\n";
                respond += rs.getString("saiwen") + "\n";
                respond += "-----第0段-共" + rs.getString("saiwen").length() + "字";
            } else {
                respond = "没有这一天的赛文";
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
            if(!sign)respond = "无该天赛文成绩";

        }catch (Exception e){
            e.printStackTrace();
        }
        return respond;
    }
    private static String getComMathImgPath(Date date){
        String sql = "select * from saiwenchengji where date=? order by sudu DESC";
        String imgpath = "";
        boolean sign = false;
        try {
            Connection con = Conn.getConnection();
            PreparedStatement ptst = Conn.getPtmt(con,sql);
            ptst.setDate(1, date);
            ResultSet rs = ptst.executeQuery();
            List<List<List<String>>> allValue = new ArrayList<List<List<String>>>();
            List<List<String>> contentArray = new ArrayList<List<String>>();
            List<String[]> headTitles = new ArrayList<String[]>();
            List<String> titles = new ArrayList<String>();
            titles.add("拖拉机"+date+"跟打成绩");
            headTitles.add(new String[]{"序号","名字","成绩","击键","码长","退格","回改","选重","错字"});
            allValue.add(contentArray);
            while(rs.next()){
                String name = rs.getString("name");
                String sudu = String.valueOf(rs.getDouble("sudu"));
                String key = String.valueOf(rs.getDouble("key"));
                String keylenth = String.valueOf(rs.getDouble("keylength"));
                String delete = String.valueOf(rs.getInt("delete"));
                String deletetext = String.valueOf(rs.getInt("deletetext"));
                String repeat = String.valueOf(rs.getInt("repeat"));
                String mistake = String.valueOf(rs.getInt("mistake"));
                sign = true;
                contentArray.add(Arrays.asList(new String[]{name,sudu,key,keylenth,delete,deletetext,repeat,mistake}));
            }
            imgpath += Createimg.graphicsGeneration(allValue,titles,headTitles,"",9);
            if(!sign)imgpath = "无该天赛文成绩";

        }catch (Exception e){
            e.printStackTrace();
        }
        return imgpath;
    }
    public static String  responseStr(String s,Long QQnum,Date date,int model){
        if(Conn.getdate().toString().equals(s)&&model==1)
            return "[CQ:at,qq="+QQnum+"]\n"+"不能获取今日赛文";
        else if(model==1)
            return getComArti(date);
        else
            return getComMathImgPath(date);
    }
}
