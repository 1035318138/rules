package threadLocal.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import rules.method.DemoService;

import javax.annotation.PostConstruct;

/**
 * @author Can.Ru
 */
@Component
public class AesConfig implements InitializingBean {

    @Autowired
    private AesConfig aesConfig;

    static AesConfig demoService;

    @PostConstruct
    public void init() {
        secret1 = secret;
    }
    public  static String secret1;
    @Value("${standard.aes.secret:}")
    public  String secret;

    @Value("${standard.aes.iv:}")
    public  String iv;




    @Override
    public String toString() {
        return "AesConfig{" +
                "secret='" + secret + '\'' +
                ", iv='" + iv + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("a");

    }
}


