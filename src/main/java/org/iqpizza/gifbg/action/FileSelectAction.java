package org.iqpizza.gifbg.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsSafe;
import org.apache.commons.lang.StringUtils;
import org.iqpizza.gifbg.task.BackgroundTaskManager;
import org.iqpizza.gifbg.util.BackgroundUtil;
import org.iqpizza.gifbg.util.DialogUtil;
import org.iqpizza.gifbg.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 이 클래스는 플러그인의 행동을 감지하여 특정 메뉴를 클릭하였을 때,
 * 동작하도록 하는 클래스입니다.
 * @since 0.1.0
 * @author iqpizza6349
 */
public class FileSelectAction extends AnAction {
    private static final String SELECT_GIF = "Select Gif File";
    private static final String CLEAR_GIF = "Clear Gif File";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final String menuText = e.getPresentation().getText();

        if (menuText.equals(SELECT_GIF)) {
            // message dialog 을 연다. 경로를 받으면 연다.
            // 만일 경로가 존재하다면, gif 청크 단위로 자른 task 가 실행된다.
            String gifPath = PropertiesUtil.getValue(PropertiesUtil.GIF_PATH);
            //noinspection UnstableApiUsage
            gifPath = Messages.showInputDialog("Input gif file path", DialogUtil.DEFAULT_TITLE,
                    Messages.getInformationIcon(), gifPath, new InputValidator() {
                        @Override
                        public boolean checkInput(@NlsSafe String inputString) {
                            if (inputString.trim().length() == 0) {
                                return false;
                            }
                            return inputString.endsWith(".gif") || inputString.endsWith(".GIF");
                        }

                        @Override
                        public boolean canClose(@NlsSafe String inputString) {
                            return true;
                        }
                    });
            if (StringUtils.isEmpty(gifPath)) {
                return;
            }

            BackgroundTaskManager.restart(gifPath.trim());
            PropertiesUtil.saveValue(PropertiesUtil.GIF_ENABLE, "TRUE");
        }
        else if (menuText.equals(CLEAR_GIF)) {
            // gif 청크 단위 task 를 stop 한다.
            // 그리고 저장되었던 경로를 삭제한다.
            BackgroundTaskManager.stop();
            BackgroundUtil.clearBackground();
            PropertiesUtil.removeKey(PropertiesUtil.GIF_ENABLE);
        }
    }
}
