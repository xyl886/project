package com.love.product.util;

import java.util.ArrayList;
import java.util.List;

public class ExceptionDataUtil {

        private static final ThreadLocal<List<Exception>> exceptionThreadLocal = new ThreadLocal<>();

        public static void addException(Exception exception) {
            List<Exception> exceptions = exceptionThreadLocal.get();
            if (exceptions == null) {
                exceptions = new ArrayList<>();
                exceptionThreadLocal.set(exceptions);
            }
            exceptions.add(exception);
        }

        public static List<Exception> getExceptions() {
            return exceptionThreadLocal.get();
        }

        public static void clearExceptions() {
            exceptionThreadLocal.remove();
        }

}
