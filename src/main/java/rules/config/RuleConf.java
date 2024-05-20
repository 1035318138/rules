package rules.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Can.Ru
 */
@Component
public class RuleConf {


    public static String key1;


    public String getKey() {
        return key1;
    }

    @Value("${third.member.statistic.platform:JD}")
    public void setKey(String key) {
        key1 = key;
    }

}
