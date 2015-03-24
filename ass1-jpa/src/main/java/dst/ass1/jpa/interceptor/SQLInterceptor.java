package dst.ass1.jpa.interceptor;

import org.hibernate.EmptyInterceptor;

import java.util.concurrent.atomic.AtomicInteger;

public class SQLInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 3894614218727237142L;

    private static AtomicInteger counter = new AtomicInteger(0);
    private static boolean verbose;

    public String onPrepareStatement(String sql) {

        String sqlLowerCase = sql.toLowerCase();

        if (sqlLowerCase.startsWith("select") &&
                (sqlLowerCase.contains("from moderator") || sqlLowerCase.contains("from virtualschool"))) {
            counter.incrementAndGet();
        }

        return sql;
    }

    public static void resetCounter() {
            counter.set(0);
    }


    public static int getSelectCount() {
        return counter.intValue();
    }

    public static void setVerbose(boolean verbose) {
        SQLInterceptor.verbose = verbose;
    }
}
