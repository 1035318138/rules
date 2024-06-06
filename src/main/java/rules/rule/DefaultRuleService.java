package rules.rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

/**
 * @author Can.Ru
 */
@Service
public class DefaultRuleService implements RuleService {
    
    @Autowired
    private ApplicationContext applicationContext;

    private StandardEvaluationContext context = null;

    private static SpelExpressionParser PARSER = null;

    public DefaultRuleService(ApplicationContext applicationContext) {
        context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
    }

    static {
        PARSER = new SpelExpressionParser();
    }


    @Override
    public boolean condition(String service,String method) {


/*        DemoService beanDemo = parser.parseExpression("@demoService").getValue(context, DemoService.class);
        System.out.println("bean: " + beanDemo);*/

        // 访问bean方法
        Boolean ans = PARSER.parseExpression(String.format("@%s.%s() && @demoService.getIfAgtB(3,2) && @demoService.getFalse()", service, method)).getValue(context, Boolean.class);
        System.out.println(ans);


        return ans;
    }

    @Override
    public void action() {
        System.out.println("execute action...");
    }
}
