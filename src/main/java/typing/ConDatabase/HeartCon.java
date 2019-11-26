package typing.ConDatabase;

import java.sql.Connection;
import java.sql.SQLException;

public class HeartCon extends Thread {
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(60*60*1000);
                Connection con = Conn.getConnection();
                Conn.execute(con,"select 1");
                System.out.println("自连");
                con.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
