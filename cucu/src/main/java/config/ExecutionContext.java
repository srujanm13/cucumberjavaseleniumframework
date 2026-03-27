package config;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContext {
    private static final ThreadLocal<Map<String, String>> CONTEXT =
            ThreadLocal.withInitial(HashMap::new);

    private ExecutionContext() {
    }

    public static void set(String key, String value) {
        if (value != null && !value.isBlank()) {
            CONTEXT.get().put(key, value.trim());
        }
    }

    public static String get(String key) {
        return CONTEXT.get().get(key);
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
