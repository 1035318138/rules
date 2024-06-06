package rules.rule;

import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;

public class RuleTest {

    public static void main(String[] args) {
        // 创建规则对象
        MyRule myRule = new MyRule();

        boolean condition = true;//ruleService.condition("demoService", "getTrue");

        // 设置规则条件
        myRule.setCondition(true); // 触发规则执行

        // 创建规则引擎
        RulesEngine rulesEngine = RulesEngineBuilder.aNewRulesEngine().build();

        // 注册规则
        rulesEngine.registerRule(myRule);

        // 执行规则
        rulesEngine.fireRules();
    }
}

