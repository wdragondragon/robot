package game.OneATwoB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
    String rightstr = new String();
    Room(int n){
        List<Integer> numlist = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0;i<n;i++){
            int randomnum;
            do{
                randomnum = rand.nextInt(10);
            } while(numlist.contains(randomnum));
            numlist.add(randomnum);
            rightstr += String.valueOf(randomnum);
        }
        System.out.println(rightstr);
    }
    String judge(String answer){
        String message = new String();
        if(rightstr.equals(answer)){
            message = "恭喜正确！";
        }else {
            char[]rightchars = rightstr.toCharArray();
            char[]answerchars = answer.toCharArray();
            int A = 0;
            int B = 0;
            for(int i=0;i<rightchars.length;i++){
                int index = rightstr.indexOf(answerchars[i]);
                if(index==i){
                    A++;
                }else if(index!=-1&&index!=i){
                    B++;
                }
            }
            message = A + "A" + B + "B";
        }
        return message;
    }
    String getRightstr(){
        return rightstr;
    }
}
