import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Can.Ru
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("rules")
public class RulesApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RulesApplication.class).run(args);
    }
}
