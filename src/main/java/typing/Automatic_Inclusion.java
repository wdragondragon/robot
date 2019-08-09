package typing;

import cc.moecraft.icq.sender.IcqHttpApi;
import jdragon.club.robot;

import java.util.Calendar;

public class Automatic_Inclusion extends Thread {
    @Override
    public void run() {
        IcqHttpApi httpApi =  robot.getInstance().getAccountManager().getNonAccountSpecifiedApi();
        while(true) {
            try {

                Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.HOUR_OF_DAY)==23&&calendar.get(Calendar.MINUTE)==40) {
                    RobotGroupClient.automati_inclusion_sign = true;


                    for(Long o:RobotGroupClient.grouplist.keySet()){
                        httpApi.sendGroupMsg(o, "#成绩");
                        RobotGroupClient.grouplist.put(o,true);
                    }
                }
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
