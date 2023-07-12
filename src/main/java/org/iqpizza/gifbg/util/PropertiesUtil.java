package org.iqpizza.gifbg.util;

import com.intellij.ide.util.PropertiesComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * gif 파일 경로와 gif 파일이 설정되었는 가 여부를 확인하고
 * 새롭게 갱신을 도와주는 유틸 클래스입니다.
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public class PropertiesUtil {
    public static final String GIF_PATH = "GIF_PATH";
    public static final String GIF_ENABLE = "TRUE";

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
}
