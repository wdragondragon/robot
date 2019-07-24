package EntoZh;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Translate extends IcqListener {
    private static final String APP_ID = "20190523000301152";
    private static final String SECURITY_KEY = "Vk2241HU_WvI9R7EVuGX";
    TransApi api = new TransApi(APP_ID, SECURITY_KEY);
    @EventHandler
    public void Carry(EventMessage event) {
        String message = event.getMessage();
        String s[] = message.split("\r\n");
        if(message.equals("语言指令"))
            event.respond(
                    "zh\t中文\n" +
                    "en\t英语\n" +
                    "yue\t粤语\n" +
                    "wyw\t文言文\n" +
                    "jp\t日语\n" +
                    "kor\t韩语\n" +
                    "fra\t法语\n" +
                    "spa\t西班牙语\n" +
                    "th\t泰语\n" +
                    "ara\t阿拉伯语\n" +
                    "ru\t俄语\n" +
                    "pt\t葡萄牙语\n" +
                    "de\t德语\n" +
                    "it\t意大利语\n" +
                    "el\t希腊语\n" +
                    "nl\t荷兰语\n" +
                    "pl\t波兰语\n" +
                    "bul\t保加利亚语\n" +
                    "est\t爱沙尼亚语\n" +
                    "dan\t丹麦语\n" +
                    "fin\t芬兰语\n" +
                    "cs\t捷克语\n" +
                    "rom\t罗马尼亚语\n" +
                    "slo\t斯洛文尼亚语\n" +
                    "swe\t瑞典语\n" +
                    "hu\t匈牙利语\n" +
                    "cht\t繁体中文\n" +
                    "vie\t越南语");
        if(s.length==2){
            String str = "";
            if(s[0].equals("中转英"))
                str = api.getTransResult(s[1], "auto", "en");
            else if(s[0].equals("英转中"))
                str = api.getTransResult(s[1], "auto", "zh");
            else if(s[0].equals("中转粤"))
                str = api.getTransResult(s[1], "auto", "yue");
            else {
                String t[] = s[0].split(" ");
                if(t.length==3&&t[0].equals("翻译")){
                    str = api.getTransResult(s[1], t[1], t[2]);
                }
            }
            System.out.println(str);
            JsonObject jsonObj = (JsonObject)new JsonParser().parse(str);//解析json字段
            String res = jsonObj.get("trans_result").toString();//获取json字段中的 result字段，因为result字段本身即是一个json数组字段，所以要进一步解析
            JsonArray js = new JsonParser().parse(res).getAsJsonArray();//解析json数组字段
            jsonObj = (JsonObject)js.get(0);//result数组中只有一个元素，所以直接取第一个元素
            str = jsonObj.get("dst").getAsString();
            System.out.println(str);
            event.respond(str);
        }
    }
}