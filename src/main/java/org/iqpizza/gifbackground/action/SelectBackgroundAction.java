package org.iqpizza.gifbackground.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.iqpizza.gifbackground.configuration.constraint.PluginConstraint;
import org.iqpizza.gifbackground.component.dialog.FileInputDialog;
import org.iqpizza.gifbackground.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.intellij.openapi.actionSystem.ActionUpdateThread.EDT;

/**
 * 배경화면으로 설정할 파일을 대화창을 통해 받아와서 해당 파일을
 * 배경화면으로 지정하고, 움직일 수 있는 gif 파일의 경우 움직이도록 합니다.
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public class SelectBackgroundAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        JFrame frame = (JFrame) KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .getActiveWindow();
        String currentPath = PropertiesUtil.getValue(PluginConstraint.GIF_PATH);
        new FileInputDialog(currentPath, text -> {
            if (!text.isEmpty()) {
                PropertiesUtil.saveValue(PluginConstraint.GIF_PATH, text);
                //TODO: initialize media player and start it.
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // TODO: MediaPlayer.isStopped
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return EDT;
    }
}
