package rules;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;

@Rule(name = "My Rule", description = "Description of my rule")
public class MyRule {

    private boolean condition;

    @Condition
    public boolean when() {
        // 定义您的条件
        return condition;
    }

    @Action
    public void then() throws Exception {
        // 规则匹配时执行的操作
        System.out.println("Rule executed!");
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }
}

