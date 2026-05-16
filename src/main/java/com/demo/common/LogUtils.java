package com.demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 統一日誌工具。
 *
 * 設計目的：將 slf4j 的依賴集中在這一個類別，
 * 若日後 slf4j 需要替換為其他日誌元件（如 log4j2、java.util.logging），
 * 只需修改此檔案，其他所有程式碼不需要異動。
 *
 * 規範：
 * - 禁止在其他類別直接使用 System.out.println()
 * - 禁止在其他類別直接宣告或使用 slf4j Logger / LoggerFactory
 * - 一律呼叫 LogUtils.info() / warn() / error() / debug()
 */
public final class LogUtils {

    private LogUtils() {}

    private static Logger logger() {
        String callerClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames
                        .filter(f -> !f.getDeclaringClass().equals(LogUtils.class))
                        .findFirst()
                        .map(f -> f.getDeclaringClass().getName())
                        .orElse(LogUtils.class.getName()));
        return LoggerFactory.getLogger(callerClass);
    }

    public static void info(String message, Object... args) {
        logger().info(message, args);
    }

    public static void warn(String message, Object... args) {
        logger().warn(message, args);
    }

    public static void error(String message, Object... args) {
        logger().error(message, args);
    }

    public static void debug(String message, Object... args) {
        logger().debug(message, args);
    }
}
