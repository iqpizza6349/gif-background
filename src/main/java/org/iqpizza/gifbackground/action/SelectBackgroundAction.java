package org.iqpizza.gifbackground.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.iqpizza.gifbackground.configuration.constant.PluginConstant;
import org.iqpizza.gifbackground.component.dialog.FileInputDialog;
import org.iqpizza.gifbackground.media.GifPlayer;
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
        String currentPath = PropertiesUtil.getValue(PluginConstant.GIF_PATH);
        new FileInputDialog(currentPath, text -> {
            if (!text.isEmpty()) {
                PropertiesUtil.saveValue(PluginConstant.GIF_PATH, text);
                GifPlayer.INSTANCE.injectPainter(frame);
            }
        });
    }
}
