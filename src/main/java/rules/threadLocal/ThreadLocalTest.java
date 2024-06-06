package rules.threadLocal;

import rules.utils.ThreadLocalUtil;


/**
 * @author Can.Ru
 */
public class ThreadLocalTest {


    private static final String A ="A";
    private static final String B ="B";
    private static final String C ="C";



    public static void doExecute(int i) {
        System.out.println(i+"*"+"before*"+Thread.currentThread().getName() +ThreadLocalUtil.get(A));
        System.out.println(i+"*"+"before*"+Thread.currentThread().getName() +ThreadLocalUtil.get(B));
        System.out.println(i+"*"+"before*"+Thread.currentThread().getName() +ThreadLocalUtil.get(C));

        ThreadLocalUtil.set(A,A);
        ThreadLocalUtil.set(B,B);
        ThreadLocalUtil.set(C,C);

        System.out.println("before*"+i+"*"+Thread.currentThread().getName() +ThreadLocalUtil.get(A));
        System.out.println("before*"+i+"*"+Thread.currentThread().getName() +ThreadLocalUtil.get(B));
        System.out.println("before*"+i+"*"+Thread.currentThread().getName() +ThreadLocalUtil.get(C));

        //业务逻辑执行完不清理该线程上下文数据 会导致<before>块业务还保留着上个线程的值
        ThreadLocalUtil.getThreadLocal().clear();
    }
}
