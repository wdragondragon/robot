package typing.Tools;

import cc.moecraft.icq.event.events.message.EventGroupMessage;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RGroup;
import cc.moecraft.icq.sender.returndata.returnpojo.get.RGroupMemberInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class initGroupList {
    public static Map<Long,Map<Long,String>> GroupMemberCardMap= new HashMap<Long, Map<Long, String>>();
    public static void init(EventGroupMessage event){
        List<RGroup> Grouplist= event.getHttpApi().getGroupList().getData();
        for(RGroup k:Grouplist) {
            Long GroupID = k.getGroupId();
            if (!GroupMemberCardMap.containsKey(GroupID)) {        //群名片散列
//                System.out.println(GroupId);
                List<RGroupMemberInfo> s = event.getHttpApi().getGroupMemberList(GroupID).getData();
                Map<Long, String> memberlist = new HashMap<Long, String>();
                for (RGroupMemberInfo key : s) {
                    memberlist.put(key.getUserId(), key.getCard());
                }
                GroupMemberCardMap.put(GroupID, memberlist);
            }
        }
    }
}
