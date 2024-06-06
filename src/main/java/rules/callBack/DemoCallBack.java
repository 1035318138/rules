package rules.callBack;

import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.List;

public class DemoCallBack {


    private static CallBack<Triple<Integer, String, List<?>>, Integer> doCallBack =
            r -> {
                //select * from schema.table where ... limit i*pageSize,pageSize;
                List<?> data = Arrays.asList("1","2","3");
                //easyExcel upload data...
                String upSuccessUrl = "https://www.baidu.com/123.xlsx";
                return Triple.of(r,upSuccessUrl,data);
            };

    public static void demo(){
        Triple<Integer, String, List<?>> integerStringListTriple = doCallBack.callBack(2);
        System.out.println(integerStringListTriple);
    }


    private static CallBack<String,Integer> do1 = (r) ->String.valueOf(r*2);

    public static void simpleDemo(){
        String str = do1.callBack(2);
        System.out.println(str);
    }


    public static void main(String[] args) {
        simpleDemo();
        demo();
    }

}
