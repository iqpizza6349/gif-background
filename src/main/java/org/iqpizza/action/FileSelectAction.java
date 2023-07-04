package org.iqpizza.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 이 클래스는 플러그인의 행동을 감지하여 특정 메뉴를 클릭하였을 때,
 * 동작하도록 하는 클래스입니다.
 * @since 0.1.0
 * @author iqpizza6349
 */
public class FileSelectAction extends AnAction {

    private static final String SELECT_GIF = "Select gif file";
    private static final String CLEAR_GIF = "Clear gif file";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final String menuText = e.getPresentation().getText();

        if (menuText.equals(SELECT_GIF)) {
            // message dialog 을 연다. 경로를 받으면 연다.
            // 만일 경로가 존재하다면, gif 청크 단위로 자른 task 가 실행된다.
        }
        else if (menuText.equals(CLEAR_GIF)) {
            // gif 청크 단위 task 를 stop 한다.
            // 그리고 저장되었던 경로를 삭제한다.
        }
    }
}
