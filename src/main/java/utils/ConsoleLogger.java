package utils;

public class ConsoleLogger {
    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String GRAY = "\u001B[90m";

    // General log methods
    public static void logInfo(String message) {
        System.out.println(BLUE + message + RESET);
    }

    public static void logSuccess(String message) {
        System.out.println(GREEN + message + RESET);
    }

    public static void logWarning(String message) {
        System.out.println(YELLOW + message + RESET);
    }

    public static void logError(String message) {
        System.out.println(RED + message + RESET);
    }

    public static void logPlain(String message) {
        System.out.println(GRAY + message + RESET);
    }

    // Section title formatter
    public static void logSection(String title) {
        System.out.println(CYAN + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🔍 " + title);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
    }

    // Bullet-style logging
    public static void logBullet(String label, String value, boolean success) {
        String color = success ? GREEN : RED;
        String icon = success ? "✅" : "❌";
        System.out.println(color + icon + " " + label + ": " + value + RESET);
    }
}
