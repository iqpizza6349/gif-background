package org.iqpizza.gifbg.util;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;

/**
 * editor 의 배경화면 초기화와 설정을 하는 유틸 클래스입니다.
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public class BackgroundUtil {

    public static void clearBackground() {
        setBackground(null);
    }

    public static void setBackground(String path) {
        final PropertiesComponent props = PropertiesComponent.getInstance();
        props.setValue(IdeBackgroundUtil.EDITOR_PROP, path);
    }
}
