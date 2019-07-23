package ConDatebase.royal;

import ConDatebase.Conn;
import java.sql.Date;
public class Fresh extends Thread{
    public void run(){
        Date date = Conn.getdate();
        Date date1 = Conn.getdate();
        while(true){
            try {
                sleep(1000);
            } catch (InterruptedException e) {}
            date = date1;
            date1 = Conn.getdate();
            if(!date.toString().equals(date1.toString())) {
                InConn.Freshdatetime();
            }
        }
    }
}
