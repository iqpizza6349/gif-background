package org.iqpizza.gifbackground.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.iqpizza.gifbackground.configuration.constant.PluginConstant;
import org.iqpizza.gifbackground.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;

/**
 * .gif 확장자로 지정된 배경화면이 움직이고 있다면, 더 이상 움직이지 않도록 하고 삭제합니다.
 *
 * @version 0.1.0
 * @author iqpizza6349
 */
public class ResetBackgroundAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PropertiesUtil.saveValue(PluginConstant.GIF_PATH, null);
    }
}
