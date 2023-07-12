package org.iqpizza.gifbg.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;

/**
 * 오류 혹은 확인 등 간단한 메시지를 보여주는 컴포넌트를 띄워주는
 * 유틸 클래스입니다.
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public class DialogUtil {

    public static final String DEFAULT_TITLE = "Gif Background";

    public static void showInfo(String message) {
        show(() -> Messages.showInfoMessage(message, DEFAULT_TITLE));
    }

    public static void showError(String message) {
        show(() -> Messages.showErrorDialog(message, DEFAULT_TITLE));
    }

    private static void show(Runnable runnable) {
        ApplicationManager.getApplication().invokeLater(runnable);
    }
}
