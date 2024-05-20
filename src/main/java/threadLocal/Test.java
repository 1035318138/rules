package threadLocal;

import threadLocal.utils.ThreadLocalUtil;


/**
 * @author Can.Ru
 */
public class Test {


    private static final String A ="A";
    private static final String B ="B";
    private static final String C ="C";



    public static void excute(int i) {
        System.out.println(i+"*"+"before*"+Thread.currentThread().getName() +ThreadLocalUtil.get(A));
        System.out.println(i+"*"+"before*"+Thread.currentThread().getName() +ThreadLocalUtil.get(B));
        System.out.println(i+"*"+"before*"+Thread.currentThread().getName() +ThreadLocalUtil.get(C));

        ThreadLocalUtil.set(A,A);
        ThreadLocalUtil.set(B,B);
        ThreadLocalUtil.set(C,C);

        System.out.println("before*"+i+"*"+Thread.currentThread().getName() +ThreadLocalUtil.get(A));
        System.out.println("before*"+i+"*"+Thread.currentThread().getName() +ThreadLocalUtil.get(B));
        System.out.println("before*"+i+"*"+Thread.currentThread().getName() +ThreadLocalUtil.get(C));
        ThreadLocalUtil.getThreadLocal().clear();
    }
}
