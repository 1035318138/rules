package rules.rule;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;

/**
 * @author Can.Ru
 */
public interface RuleService {

    @Condition
    public boolean condition(String service,String methond);

    @Action
    public void action();
}
