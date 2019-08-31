package typing.Tools;

import cc.moecraft.icq.sender.IcqHttpApi;
import cc.moecraft.icq.sender.returndata.ReturnListData;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RGroupMemberInfo;
import typing.ConDatabase.OutConn;

import java.util.*;

public class initGroupList {
    public static Map<Long,String> QQlist = new HashMap<>();
    public static Map<Long,Map<Long,String>> GroupMemberCardMap= new HashMap<Long, Map<Long, String>>();
//    public static List<Long> QQDo = Arrays.asList(new Long[]{1045865146L,1157477506L,2963900463L,1816436708L,2297714857L,2524098743L});
    public static Map<Long,String> QQDomap = new HashMap<>();
    public static void init(IcqHttpApi httpApi){
//        List<RGroup> Grouplist= event.getHttpApi().getGroupList().getData();

        HashMap<Long,Boolean> Grouplist = OutConn.getGroupList();
        for(long GroupID :Grouplist.keySet()) {
            if (!GroupMemberCardMap.containsKey(GroupID)) {        //群名片散列
//                IcqHttpApi icqHttpApi = event.getHttpApi();
                ReturnListData<RGroupMemberInfo> returnData = httpApi.getGroupMemberList(GroupID);
//                System.out.println(returnData.getData());
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
        for(long GroupID :Grouplist.keySet()) {
            List<RGroupMemberInfo> s = httpApi.getGroupMemberList(GroupID).getData();
            for (RGroupMemberInfo key : s) {
                if(QQlist.get(key.getUserId())==null||QQlist.get(key.getUserId()).equals(""))
                    QQlist.get(key.getNickname());
            }
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
    }
}
