package rules.method;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Can.Ru
 */
@Service
public class DemoService {
    public boolean getTrue(){
        System.out.println("getTrue true");
        return true;
    }
    public boolean getFalse(){
        System.out.println("getFalse false");
        return false;
    }


    public boolean getBooleanByStr(String a) {
        System.out.println("getBooleanByStr "+!StringUtils.isEmpty(a));
        return !StringUtils.isEmpty(a);
    }

    public boolean getIfAgtB(Integer a, Integer b) {
        System.out.println("getIfAgtB "+ (a > b));
        return a > b;
    }
}
