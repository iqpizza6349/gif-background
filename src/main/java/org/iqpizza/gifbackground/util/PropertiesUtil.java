package org.iqpizza.gifbackground.util;

import com.intellij.ide.util.PropertiesComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 배경화면의 설정들을 인 메모리상에서 관리하는 유틸 클래스입니다.
 */
public class PropertiesUtil {

    public static void removeKey(@NotNull String key) {
        saveValue(key, null);
    }

    public static void saveValue(@NotNull String key, String value) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(key, value);
    }

    @Nullable
    public static String getValue(@NotNull String key) {
        return getValue(key, null);
    }

    public static String getValue(@NotNull String key, String defaultValue) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String value = prop.getValue(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static int getValueByInt(@NotNull String key, String defaultValue) {
        return Integer.parseInt(getValue(key, defaultValue));
    }

    public static long getValueByLong(@NotNull String key, String defaultValue) {
        return Long.parseLong(getValue(key, defaultValue));
    }

    public static float getValueByFloat(@NotNull String key, String defaultValue) {
        return Float.parseFloat(getValue(key, defaultValue));
    }
}
