package typing.Tools;

import java.util.*;

public class SortMap {
    public static String SendsortKey(Map<Long,Integer> map,Map<Long,SortMap>namelist){
        String message="";
        Set set = map.keySet();
        Long[] arr = (Long[]) set.toArray();
        Arrays.sort(arr);
        for (Long key : arr) {
            System.out.println(key + ": " + map.get(key));
            message +="用户名："+ namelist.get(key) +
                    " 用户Q号：" + key + " 得分：" + map.get(key) + "\n";
        }
        return  message;
    }
    public static String SendsortValue(Map<Long,Integer> map,Map<Long,String> namelist){
        String message="";
        //value-sort
        List<Map.Entry<Long, Integer>> list = new ArrayList<Map.Entry<Long, Integer>>(map.entrySet());
        //collections.sort()
        Collections.sort(list, new Comparator<Map.Entry<Long, Integer>>() {
            public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        //for-each
        for (Map.Entry<Long, Integer> mapping : list) {
            System.out.println(mapping.getKey() + ": " + mapping.getValue());
            message +="用户名："+ namelist.get(mapping.getKey()) +" 用户Q号：" + mapping.getKey() + " 得分：" + mapping.getValue() + "\n";
        }//OutConn.insteadName(mapping.getKey())
        return message;
    }
    public static String SendsortValueFollow(Map<Long,Integer> math,Map<Long, Double> map,Map<Long,String>namelist){
        String message="";
        //value-sort
        List<Map.Entry<Long, Double>> list = new ArrayList<Map.Entry<Long, Double>>(map.entrySet());
        //collections.sort()
        Collections.sort(list, new Comparator<Map.Entry<Long, Double>>() {
            public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        //for-each
        int i = 0;
        for (Map.Entry<Long, Double> mapping : list) {
            if(i<3)
                math.put(mapping.getKey(),(math.get(mapping.getKey())+3-i));
            else
                break;
            i++;// OutConn.insteadName(mapping.getKey())
            message +="用户名："+ namelist.get(mapping.getKey())+
                    " 用户Q号：" + mapping.getKey() +
                    " 得分：" + math.get(mapping.getKey())+
                    " 该段速度："+mapping.getValue()+"\n";
        }
        return message;
    }
    public static String SendsortValueTeamOneSpeed(Map<Long,Double> map,Map<Long,String> namelist){
        String message="";
        //value-sort
        List<Map.Entry<Long, Double>> list = new ArrayList<Map.Entry<Long, Double>>(map.entrySet());
        //collections.sort()
        Collections.sort(list, new Comparator<Map.Entry<Long, Double>>() {
            public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        //for-each
        for (Map.Entry<Long, Double> mapping : list) {
            System.out.println(mapping.getKey() + ": " + mapping.getValue());
            message +="用户名："+ namelist.get(mapping.getKey()) +//OutConn.insteadName(mapping.getKey())
                    " 用户Q号：" + mapping.getKey() + " 该段速度：" + mapping.getValue() + "\n";
        }
        return message;
    }
    public static String SendsortValueTeamSpeed(Map<Integer,Double> map,Map<Integer,Integer> math){
        String message="";
        //value-sort
        List<Map.Entry<Integer,Double>> list = new ArrayList<Map.Entry<Integer,Double>>(map.entrySet());
        //collections.sort()
        Collections.sort(list, new Comparator<Map.Entry<Integer,Double>>() {
            public int compare(Map.Entry<Integer,Double> o1, Map.Entry<Integer,Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        //for-each
        int i=0;
        for (Map.Entry<Integer,Double> mapping : list) {
            System.out.println(mapping.getKey() + ": " + mapping.getValue());
            if(i==0){
                math.put(mapping.getKey(),(math.get(mapping.getKey())+1));
            }
            message +="团队号：" + mapping.getKey() + " 队员平均速度：" + mapping.getValue() + "队伍得分："+math.get(mapping.getKey())+"\n";
            i++;
        }
        return message;
    }
    public static String SendsortValueTeamMath(Map<Integer,Integer> map){
        String message="";
        //value-sort
        List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        //collections.sort()
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        //for-each
        for (Map.Entry<Integer, Integer> mapping : list) {
            System.out.println(mapping.getKey() + ": " + mapping.getValue());
            message +="队伍号：" + mapping.getKey() + " 得分：" + mapping.getValue() + "\n";
        }
        return message;
    }
}
