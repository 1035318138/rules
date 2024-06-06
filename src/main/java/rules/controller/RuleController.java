package rules.controller;

import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rules.rule.MyRule;
import rules.rule.RuleService;

/**
 * @author Can.Ru
 */
@RestController
@RequestMapping("/rule")
public class RuleController {


    @Autowired
    private RuleService ruleService;

    @RequestMapping("/simpleRule")
    public Boolean simpleRule() {
        // 创建规则对象
        MyRule myRule = new MyRule();

        boolean condition = ruleService.condition("demoService", "getTrue");

        // 设置规则条件
        myRule.setCondition(true); // 触发规则执行

        // 创建规则引擎
        RulesEngine rulesEngine = RulesEngineBuilder.aNewRulesEngine().build();

        // 注册规则
        rulesEngine.registerRule(myRule);

        // 执行规则
        rulesEngine.fireRules();
        return condition;
    }


}
