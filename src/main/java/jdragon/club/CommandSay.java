package jdragon.club;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.User;

import java.util.ArrayList;

public class CommandSay implements EverywhereCommand {

    public CommandProperties properties() {
        return new CommandProperties ("username", "u", "Ãû×Ö");
    }

    public String run(EventMessage eventMessage, User user, String s, ArrayList<String> arrayList) {
        return "ÎÒ½Ðrobot";
    }


}
