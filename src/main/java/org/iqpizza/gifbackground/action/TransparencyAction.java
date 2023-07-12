package org.iqpizza.gifbackground.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.iqpizza.gifbackground.component.dialog.TransparencyDialog;
import org.iqpizza.gifbackground.configuration.constraint.PluginConstraint;
import org.iqpizza.gifbackground.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.actionSystem.ActionUpdateThread.EDT;

/**
 * 설정된 배경화면의 불투명도를 조절할 수 있는 컴포넌트를 보여줍니다.
 * 설정된 불투명도 값으로 배경화면의 불투명도를 조절하도록 합니다.
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public class TransparencyAction extends AnAction {

    private float currentTransparency = PropertiesUtil.getValueByFloat(PluginConstraint.TRANSPARENCY, "0F");

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new TransparencyDialog(currentTransparency, value -> {
            currentTransparency = value;
            updateTransparency();
        });
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        updateTransparency();
    }

    private void updateTransparency() {
        // TODO: update to current transparency
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return EDT;
    }
}
