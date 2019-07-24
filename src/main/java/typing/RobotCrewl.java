package typing;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RobotCrewl {

    public static String carry(String id,String what,String cookie,int i){
        String str = "";
        try {
            Document doc = getDoc(id,what,cookie);
            str = regex(doc,i);
//            sendmessage(str);

        }catch (Exception e){
            System.out.println("连接错误");
            e.printStackTrace();
        }
        return str;
    }

    public static Document getDoc(String id,String what,String cookie) throws IOException{
        Map<String, String> map = new HashMap<String, String>();
        map.put("account",id);
        map.put("inputObject","all");
        map.put("Submit",what);
        Connection resconn = Jsoup.connect("http://10.10.12.54/accounttodatTrjnObject.action");
        Connection.Response res = resconn       //http://10.10.12.54/loginstudent.action
                .data(map)
                .header("Cookie",cookie )
                .method(Connection.Method.POST)
                .execute();
        Document doc = res.parse();
        return doc;
    }
    public static String regex(Document doc,int select){
        String regex = ">(.*?)</td>";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(doc.toString());
        int i = 1;
        String str = "";
        if(select==1)
            while (m.find()) {
                i++;
                if (i > 13 && i < 22) str += m.group(1) + " ";
                if(i>=22)break;
            }
        else if(select==2){
            while (m.find()) {
                i++;
                if (i > 13) {
                    str += m.group(1) + " ";
                    if ((i - 13) % 10 == 0) str += "\n";
                }
            }
        }
        System.out.println(str);
        return str;
    }
    public static void sendmessage(String str) throws IOException {
        String message = URLEncoder.encode(str);
        String URL = "http://jdragon.club:5700/send_private_msg?message=" + message + "&user_id=1061917196";
        System.out.println(URL);
        java.net.URL url = new URL(URL);
        URLConnection urlcon = url.openConnection();
        InputStreamReader in = new InputStreamReader(urlcon.getInputStream(), "utf-8");
        BufferedReader bufferRead = new BufferedReader(in);
        System.out.println(bufferRead.readLine());
    }
    public static int recordnum(int num) throws InterruptedException{
        num++;
        System.out.println("次数:" + num);
        Thread.sleep(60 * 1000);
        return num;
    }
}