package typing.Tools;

import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RGroupMemberInfo;
import typing.ConDatabase.OutConn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class initGroupList {
    public static Map<Long,String> QQlist = new HashMap<>();
    public static Map<Long,Map<Long,String>> GroupMemberCardMap= new HashMap<Long, Map<Long, String>>();
    public static Map<Long,String> QQDomap = new HashMap<>();
    public static Map<Long,String> QQGroupMap = OutConn.getGroupMap();
    public static Map<String,Long> QQGroupNameMap = new HashMap<>();
    public static ArrayList<Long> adminList = new ArrayList<>();
    public static void init(IcqHttpApi httpApi){
        HashMap<Long,Boolean> Grouplist = OutConn.getGroupList();
        for(long GroupID :Grouplist.keySet()) {
            if (!GroupMemberCardMap.containsKey(GroupID)) {        //群名片散列
                List<RGroupMemberInfo> s ;
                    s = httpApi.getGroupMemberList(GroupID).getData();
                Map<Long, String> memberlist = new HashMap<Long, String>();
                for (RGroupMemberInfo key : s) {
                    memberlist.put(key.getUserId(), key.getCard());
                    if(QQlist.get(key.getUserId())==null||QQlist.get(key.getUserId()).equals(""))
                        QQlist.put(key.getUserId(),key.getCard());
                }
                GroupMemberCardMap.put(GroupID, memberlist);
            }
        }
        //添加群名片
        for(long GroupID :Grouplist.keySet()) {
            List<RGroupMemberInfo> s = httpApi.getGroupMemberList(GroupID).getData();
            for (RGroupMemberInfo key : s) {
                if(QQlist.get(key.getUserId())==null||QQlist.get(key.getUserId()).equals(""))
                    QQlist.put(key.getUserId(),key.getNickname());
            }
        }
        //添加String,long群列表
        for(Map.Entry entry:QQGroupMap.entrySet()){
            QQGroupNameMap.put(String.valueOf(entry.getValue()),(Long)entry.getKey());
        }
        QQDomap.put(1045865146L,"酥");
        QQDomap.put(1157477506L,"空");
        QQDomap.put(2963900463L,"木");
        QQDomap.put(1816436708L,"圈");
        QQDomap.put(2297714857L,"风");
        QQDomap.put(2524098743L,"焕");
        QQDomap.put(1061917196L,"谭");
        QQDomap.put(29141847L,"白");
        QQDomap.put(710596517L,"桂");
        QQDomap.put(779855010L,"无");

        adminList.add(1061917196L);
        adminList.add(2623495687L);
    }
}
