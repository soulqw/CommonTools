package com.test.soultools.tool.log;

import android.util.Log;

public class TLog {

    private static final int MAX_LENGTH = 4000;
    private static final String NULL_TIPS = "Log with null object";
    private static final String PARAM = "Param";
    private static final String NULL = "null";

    private static final int V = 0x1;
    private static final int D = 0x2;
    private static final int I = 0x3;
    private static final int W = 0x4;
    private static final int E = 0x5;
    private static final int A = 0x6;

    private static final int STACK_TRACE_INDEX_5 = 5;

    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }

    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }

    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }

    public static void e(String tag, Object... objects) {
        printLog(E, tag, objects);
    }

    private static void printLog(int type, String tagStr, Object... objects) {
        String[] contents = wrapperContent(STACK_TRACE_INDEX_5, tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        printDefault(type, tag, headString + msg);
    }

    private static String[] wrapperContent(int stackTraceIndex, String tagStr, Object... objects) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement targetElement = stackTrace[stackTraceIndex];
        String suffix = getSuffix(targetElement);
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + suffix;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + suffix;
        }

        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }

        String tag = (tagStr == null ? className : tagStr);
        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        String headString = "(" + className + ":" + lineNumber + ")#" + methodName;

        return new String[]{tag, msg, headString};
    }

    private static String getSuffix(StackTraceElement targetElement) {
        if (targetElement.getFileName().contains("java")) {
            return ".java";
        } else {
            return ".kt";
        }
    }

    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else if (objects.length == 1) {
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        } else {
            return NULL;
        }
    }

    private static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int length = msg.length();
        int countOfSub = length / MAX_LENGTH;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + MAX_LENGTH);
                printSub(type, tag, sub);
                index += MAX_LENGTH;
            }
            printSub(type, tag, msg.substring(index, length));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case V:
                Log.v(tag, sub);
                break;
            case D:
                Log.d(tag, sub);
                break;
            case I:
                Log.i(tag, sub);
                break;
            case W:
                Log.w(tag, sub);
                break;
            case E:
                Log.e(tag, sub);
                break;
            case A:
                Log.wtf(tag, sub);
                break;
        }
    }

}
