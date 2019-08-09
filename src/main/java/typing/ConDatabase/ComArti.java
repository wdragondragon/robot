package typing.ConDatabase;

import typing.Tools.Createimg;
import typing.Tools.initGroupList;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

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
            HashMap<Integer,List<Double>> rankmap = new HashMap();
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
            List<Double> keylist = new ArrayList<>();
            List<Double> keylenthlist = new ArrayList<>();
            while(rs.next()){
                double key1 = rs.getDouble("key");
                double keylenth1 = rs.getDouble("keylength");
                String name = rs.getString("name");
                String sudu = String.valueOf(rs.getDouble("sudu"));
                String key = String.valueOf(key1);
                String keylenth = String.valueOf(keylenth1);
                String delete = String.valueOf(rs.getInt("delete"));
                String deletetext = String.valueOf(rs.getInt("deletetext"));
                String repeat = String.valueOf(rs.getInt("repeat"));
                String mistake = String.valueOf(rs.getInt("mistake"));
                sign = true;
                keylist.add(key1);
                keylenthlist.add(keylenth1);
                contentArray.add(Arrays.asList(new String[]{name,sudu,key,keylenth,delete,deletetext,repeat,mistake}));
            }
            Collections.sort(keylist,Collections.reverseOrder());
            Collections.sort(keylenthlist);
            rankmap.put(3,keylist);
            rankmap.put(4,keylenthlist);
            imgpath += Createimg.graphicsGeneration(allValue,titles,headTitles,"",headTitles.get(0).length,rankmap);
            if(!sign)imgpath = "无该天赛文成绩";

        }catch (Exception e){
            e.printStackTrace();
        }
        return imgpath;
    }
    private static String getComAllGroupMathImgPath(Date date){
        String path = "";
        String sql = "select * from groupsaiwenmax where date=?";
        HashMap<Long,String> groupMap1 = OutConn.getGroupMap();
        List<Long> grouplist = new ArrayList(groupMap1.keySet());
        try{
            HashMap<Long,HashMap<Long,Double>> UserMathMap = new HashMap<>();
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setDate(1,date);
            ResultSet rs = ptmt.executeQuery();
            while(rs.next()){
                long id = rs.getLong("id");
                long groupid = rs.getLong("groupid");
                double math = rs.getDouble("speed");
                if(UserMathMap.containsKey(id)){
                    Double all = 0.0;
                    HashMap<Long,Double> groupmathMap = UserMathMap.get(id);
                    groupmathMap.put(groupid,math);
                    for(Long idtemp:grouplist)
                        all += groupmathMap.get(idtemp);
                    groupmathMap.put(1l,all/(grouplist.size()));
                }else {
                    HashMap<Long,Double> groupmathMap = new HashMap<>();
                    for(Long groupidtemp:grouplist){
                        groupmathMap.put(groupidtemp,0.0);
                    }
                    groupmathMap.put(groupid,math);
                    groupmathMap.put(1l,math/grouplist.size());
                    UserMathMap.put(id,groupmathMap);
                }
            }
            List<Map.Entry<Long,HashMap<Long,Double>>> list = new ArrayList<>(UserMathMap.entrySet());
            Collections.sort(list, (o1, o2) -> o2.getValue().get(1l).compareTo(o1.getValue().get(1l)));
            HashMap<Integer,List<Double>> rankmap = new HashMap();
            List<List<List<String>>> allValue = new ArrayList<List<List<String>>>();
            List<List<String>> contentArray = new ArrayList<List<String>>();
            List<String[]> headTitles = new ArrayList<String[]>();
            List<String> titles = new ArrayList<String>();
            titles.add("群列表"+date+"跟打成绩");
            String [] headtitle = new String[groupMap1.size()+3];
            headtitle[0] = "序号";
            headtitle[1] = "名字";
            headtitle[2] = "平均";
            int num = 3;
            for(long groupidtemp:grouplist){
                headtitle[num] = groupMap1.get(groupidtemp);
                num++;
            }
            headTitles.add(headtitle);
            for(Map.Entry<Long, HashMap<Long, Double>> mapping:list){
                long idtemp = mapping.getKey();
                HashMap<Long,Double> mathmaptemp = mapping.getValue();
                List<String> value = new ArrayList();
                value.add(String.valueOf(initGroupList.QQlist.get(idtemp)));
                value.add(String.format("%.2f",mathmaptemp.get(1l)));
                for(long gid:grouplist){
                    value.add(String.format("%.2f",mathmaptemp.get(gid)));
                }
                contentArray.add(value);
            }
            allValue.add(contentArray);
            path += Createimg.graphicsGeneration(allValue,titles,headTitles,"",headTitles.get(0).length,rankmap);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return path;
        }
    }
    private static String getComUnionGroupMathImgPath(Date date){
        String path = "";
        String sql = "select * from allgroupsaiwenchengji where saiwendate=? order by sudu DESC";
        String getaversql = "select speedaver from robotclient where id=?";
        boolean sign = false;
        try {
            HashMap<Integer,List<Double>> rankmap = new HashMap();
            Connection con = Conn.getConnection();
            PreparedStatement ptst = Conn.getPtmt(con,sql);
            PreparedStatement avgrptmt = Conn.getPtmt(con,getaversql);
            ptst.setDate(1, date);
            ResultSet rs = ptst.executeQuery();
            ResultSet averrs;
            List<List<List<String>>> allValue = new ArrayList<List<List<String>>>();
            List<List<String>> contentArray = new ArrayList<List<String>>();
            List<String[]> headTitles = new ArrayList<String[]>();
            List<String> titles = new ArrayList<String>();
            titles.add("联赛"+date+"成绩");
            headTitles.add(new String[]{"序号","名字","来自","成绩","段位","首打","击键","码长","退格","回改","选重","错字","键准","重打"});
            allValue.add(contentArray);
            int n = 0;
            List<Double> keylist = new ArrayList<>();
            List<Double> keylenthlist = new ArrayList<>();
            List<Double> firstplaylist = new ArrayList<>();
            List<Double> ranklist = new ArrayList<>();
            while(rs.next()){
                long groupid = rs.getLong("groupid");
                long id = rs.getLong("id");
                avgrptmt.setLong(1,id);
                averrs = avgrptmt.executeQuery();
                sql = "select * from groupmap where groupid="+groupid;
                ResultSet rs2 = Conn.getStmtSet(con,sql);
                String groupname = "";
                Double speedaver = -1.0;
                if(averrs.next()) {
                    speedaver = averrs.getDouble("speedaver");
                    ranklist.add(Double.parseDouble(String.format("%.2f",speedaver)));
                }
                if(rs2.next())groupname = rs2.getString("groupname");
                String name = rs.getString("name");
                Double key1 = rs.getDouble("keyspeed");
                Double keylenth1 = rs.getDouble("keylength");
                Double firstsudu1 = rs.getDouble("firstplay");
                String sudu = String.valueOf(rs.getDouble("sudu"));
                String key = String.valueOf(key1);
                String firstsudu = String.valueOf(firstsudu1);
                if(key.equals("-1.0"))
                    key = "无";
                else if(keylenth1<4)
                    keylist.add(key1);
                String keylenth = String.valueOf(keylenth1);
                if(keylenth.equals("-1.0"))
                    keylenth = "无";
                else if(key1>4)
                    keylenthlist.add(keylenth1);
                String typenum = String.valueOf(rs.getInt("typenum"));
                String delete = String.valueOf(rs.getInt("del"));
                if (delete.equals("-1"))
                    delete = "无";
                String deletext = String.valueOf(rs.getInt("deletetext"));
                if(deletext.equals("-1"))
                    deletext = "无";
                String select = String.valueOf(rs.getInt("sel"));
                if(select.equals("-1"))
                    select = "无";
                String mistake = String.valueOf(rs.getInt("mistake"));
                if(mistake.equals("-1"))
                    mistake = "无";
                String rightkeyper = String.valueOf(rs.getDouble("rightkeyper"));
                if(rightkeyper.equals("-1.0"))
                    rightkeyper = "无";
                else
                    rightkeyper += "%";
                String straver = String.valueOf(speedaver);
                if(straver.equals("-1")){
                    straver = "无";
                }else {
                    straver = String.format("%.2f",speedaver);
                }
                firstplaylist.add(firstsudu1);
                sign = true;
                contentArray.add(Arrays.asList(new String[]{name,groupname,sudu,straver,firstsudu,key,keylenth,delete,deletext,select,mistake,rightkeyper,typenum}));
            }
            Collections.sort(keylist,Collections.reverseOrder());
            Collections.sort(keylenthlist);
            Collections.sort(firstplaylist,Collections.reverseOrder());
            Collections.sort(ranklist,Collections.reverseOrder());
            System.out.println(ranklist);
            rankmap.put(4,ranklist);
            rankmap.put(5,firstplaylist);
            rankmap.put(6,keylist);
            rankmap.put(7,keylenthlist);
            path += Createimg.graphicsGeneration(allValue,titles,headTitles,null,headTitles.get(0).length,rankmap);
            if(!sign)path = "无该天赛文成绩";

        }catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }
    public static String getRobotArMathImgPath(String type ,int aid){
        String path = "";
        String sql = "select * from robotsaiwen where aid=? and type=?";
        boolean sign = false;
        try {
            Connection con = Conn.getConnection();
            PreparedStatement ptmt = Conn.getPtmt(con,sql);
            ptmt.setInt(1,aid);
            ptmt.setString(2,type);
            ResultSet rs = ptmt.executeQuery();
            String title;
            if(rs.next())title = rs.getString("title");
            else return "没有该文章成绩";
            sql = "select * from robotsaiwenchengji where aid=? and type=? order by sudu DESC";
            ptmt = Conn.getPtmt(con,sql);
            ptmt.setInt(1,aid);
            ptmt.setString(2,type);
            rs = ptmt.executeQuery();
            List<List<List<String>>> allValue = new ArrayList<List<List<String>>>();
            List<List<String>> contentArray = new ArrayList<List<String>>();
            List<String[]> headTitles = new ArrayList<String[]>();
            List<String> titles = new ArrayList<String>();
            titles.add(title);
            headTitles.add(new String[]{"序号","名字","来自","成绩","击键","码长","退格","回改","选重","错字","键准","重打"});
            allValue.add(contentArray);
            int n = 0;
            List<Double> keylist = new ArrayList<>();
            List<Double> keylenthlist = new ArrayList<>();
            while(rs.next()){
                long groupid = rs.getLong("groupid");
                sql = "select * from groupmap where groupid="+groupid;
                ResultSet rs2 = Conn.getStmtSet(con,sql);
                String groupname = "";
                if(rs2.next())groupname = rs2.getString("groupname");
                String name = rs.getString("name");
                Double key1 = rs.getDouble("keyspeed");
                Double keylenth1 = rs.getDouble("keylength");
                String sudu = String.valueOf(rs.getDouble("sudu"));
                String key = String.valueOf(key1);
                if(key.equals("-1.0"))
                    key = "无";
                else if(keylenth1<4)
                    keylist.add(key1);
                String keylenth = String.valueOf(keylenth1);
                if(keylenth.equals("-1.0"))
                    keylenth = "无";
                else if(key1>4)
                    keylenthlist.add(keylenth1);
                String typenum = String.valueOf(rs.getInt("typenum"));
                String delete = String.valueOf(rs.getInt("del"));
                if (delete.equals("-1"))
                    delete = "无";
                String deletext = String.valueOf(rs.getInt("deletetext"));
                if(deletext.equals("-1"))
                    deletext = "无";
                String select = String.valueOf(rs.getInt("sel"));
                if(select.equals("-1"))
                    select = "无";
                String mistake = String.valueOf(rs.getInt("mistake"));
                if(mistake.equals("-1"))
                    mistake = "无";
                String rightkeyper = String.valueOf(rs.getDouble("rightkeyper"));
                if(rightkeyper.equals("-1.0"))
                    rightkeyper = "无";
                else
                    rightkeyper += "%";
                sign = true;
                contentArray.add(Arrays.asList(new String[]{name,groupname,sudu,key,keylenth,delete,deletext,select,mistake,rightkeyper,typenum}));
            }
            Collections.sort(keylist,Collections.reverseOrder());
            Collections.sort(keylenthlist);
            HashMap<Integer, List<Double>> rankmap = new HashMap<>();
            rankmap.put(4,keylist);
            rankmap.put(5,keylenthlist);
            path += Createimg.graphicsGeneration(allValue,titles,headTitles,null,headTitles.get(0).length,rankmap);
            if(!sign)path = "无该文章成绩";
        }catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }
    public static String  responseStr(String s,Long QQnum,Date date,int model){
        if(Conn.getdate().toString().equals(s)&&model==1)
            return "[CQ:at,qq="+QQnum+"]\n"+"不能获取今日赛文";
        else if(model==1)
            return getComArti(date);
        else if(model==2)
            return getComMathImgPath(date);
        else if(model==3)
            return getComUnionGroupMathImgPath(date);
        else
            return getComAllGroupMathImgPath(date);
    }
}
