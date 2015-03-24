package dst.ass1.jpa.interceptor;

import org.hibernate.EmptyInterceptor;

public class SQLInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 3894614218727237142L;

    private static int counter;
    private static boolean verbose;

    public String onPrepareStatement(String sql) {

        String sqlLowerCase = sql.toLowerCase();

        if (sqlLowerCase.startsWith("select") &&
                (sqlLowerCase.contains("moderator") || sqlLowerCase.contains("virtualschool"))) {
            counter++;
        }

        return sql;
    }

    public static void resetCounter() {
        counter = 0;
    }


    public static int getSelectCount() {
        return counter;
    }

    public static void setVerbose(boolean verbose) {
        SQLInterceptor.verbose = verbose;
    }

}
