package Tool;
import java.util.*;

public class SortMap {
    public static String SendsortKey(Map<Long,Integer> map){
        String message="";
        Set set = map.keySet();
        Long[] arr = (Long[]) set.toArray();
        Arrays.sort(arr);
        for (Long key : arr) {
            System.out.println(key + ": " + map.get(key));
            message +="用户Q号：" + key + " 得分：" + map.get(key) + "\n";
        }
        return  message;
    }
    public static String SendsortValue(Map<Long,Integer> map){
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
            message +="用户Q号：" + mapping.getKey() + " 得分：" + mapping.getValue() + "\n";
        }
        return message;
    }
}
