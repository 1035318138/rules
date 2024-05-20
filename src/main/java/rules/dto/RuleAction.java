package rules.dto;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Can.Ru
 */
@Data
public class RuleAction {


    private String actionCode;
    private List<Action> action;

    @Data
    static class  Action {
        private String service;
        private Integer sortId;
        private String operator;
        private Map<String, Object> params;
        private List<Action> actions;
    }

    public static void main(String[] args) {
        RuleAction ruleAction = new RuleAction();

        Action action1 = new Action();
        action1.setService("A");
        action1.setOperator("and");

        Action action2 = new Action();
        //action2.setService("B");
        action2.setOperator("and");
        Action action3 = new Action();
        //action3.setService("C");
        action3.setOperator("and");
        Action action4 = new Action();
        action4.setService("D");
        List<Action> actions = new ArrayList<>();
        actions.add(action1);
        List<Action> actions1 = new ArrayList<>();
        Action action5 = new Action();
        action5.setService("E");
        action5.setOperator("or");
        Action action6 = new Action();
        action6.setService("F");
        List<Action> actions2 = new ArrayList<>();
        actions2.add(action5);
        actions2.add(action6);
        action3.setActions(actions2);
        actions1.add(action3);
        actions1.add(action4);
        action2.setActions(actions1);
        actions.add(action2);

        Action action7 = new Action();
        action7.setService("G");
        action7.setOperator("or");

        Action action8 = new Action();
        action8.setService("K");

        actions.add(action7);
        actions.add(action8);



        ruleAction.setAction(actions);

        StringBuilder act = new StringBuilder();
        act.append("(");
        ruleAction.getAction().stream().forEach(r -> {
            if(!StringUtils.isEmpty(r.getService())){
                act.append(" ").append(r.getService()).append(" ");
            }
            if (!CollectionUtils.isEmpty(r.getActions())) {
                joinString(act,r.getActions());
            }
            act.append(r.getOperator() == null ? "" : r.getOperator());
        });
        act.append(")");
        System.out.println(act.toString());//( A and (  (  E or F  ) and D  ) and G or K )
    }

    public static void joinString(StringBuilder act, List<Action> actList) {
        act.append(" ( ");
        actList.stream().forEach(ra -> {
            if(!StringUtils.isEmpty(ra.getService())){
                act.append(" ").append(ra.getService()).append(" ");
            }
            if (!CollectionUtils.isEmpty(ra.getActions())) {
                joinString(act, ra.getActions());
            }
            act.append(ra.getOperator() == null ? "" : ra.getOperator());
        });
        act.append(" ) ");
    }


}
