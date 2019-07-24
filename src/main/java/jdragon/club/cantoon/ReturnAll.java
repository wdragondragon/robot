package jdragon.club.cantoon;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.User;
import typing.RobotCrewl;

import java.util.ArrayList;

public class ReturnAll implements EverywhereCommand {

    public CommandProperties properties() {
        return new CommandProperties ("alltoday", "a", "当日消费记录");
    }

    public String run(EventMessage eventMessage, User user, String s, ArrayList<String> arrayList) {
        return RobotCrewl.carry("73010","+%C8%B7+%B6%A8+","JSESSIONID=A25C631FBCAD2EC6E801746CAA036EF1",2);
    }
}
