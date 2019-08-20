package game.OneATwoB;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class O2TCarry extends IcqListener {
    HashMap<Long,Room> roomHashMap = new HashMap<>();

    @EventHandler
    public void Carry(EventMessage event){
        String message = event.getMessage();
        Long id = event.getSenderId();
        String at = "[CQ:at,qq=" + id + "]\n";
        String s[] = message.split(" ");
        if(message.equals("1A2B")){
            if(roomHashMap.containsKey(id)){
                event.respond("你已启动过1A2B");
            }else {
                roomHashMap.put(id,new Room(4));
                event.respond("1A2B开始！");
            }
        }else if(roomHashMap.containsKey(id)){
            Room room = roomHashMap.get(id);
            if(room.getRightstr().length()==message.length()){
                Set<Character> set = new HashSet<>();
                char[] chars = message.toCharArray();
                for(char c:chars)
                    set.add(c);
                if(set.size()!=message.length()){
                    event.respond(at+"不可有重复数字");
                }else {
                    message = room.judge(message);
                    if (message.equals("恭喜正确！"))
                        roomHashMap.remove(id);
                    event.respond(at+message);
                }
            }
        }else if(s.length==2&&s[0].equals("1A2B")){
            if(roomHashMap.containsKey(id)){
                event.respond("你已启动过1A2B");
            }else {
                int level = Integer.valueOf(s[1]);
                if(level<4||level>9)event.respond("难度在4到9之间");
                else {
                    roomHashMap.put(id, new Room(level));
                    event.respond("1A2B开始！");
                }
            }
        }
    }
}
