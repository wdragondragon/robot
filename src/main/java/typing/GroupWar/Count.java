package typing.GroupWar;

public class Count extends Thread {
    public int time = 0;
    boolean runSign = false;
    int timenum;
    public Count(int timenum){
        this.timenum=timenum;
    }
    public void run(){

        while (true) {
            if (time > timenum) {
                time = 0;
            }
            try {
                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(time);
            if(runSign)
                time++;
        }

    }
    public void setRunSign(boolean sign){
        runSign = sign;
    }
    public int getTime(){
        return time;
    }
    public void setTime(int time){
        this.time = time;
    }
}
