package org.iqpizza.gifbackground.component;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.iqpizza.gifbackground.configuration.constant.PluginConstant;
import org.iqpizza.gifbackground.media.GifPlayer;
import org.iqpizza.gifbackground.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class BackgroundComponent implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        JFrame frame = (JFrame) KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .getActiveWindow();
        String currentPath = PropertiesUtil.getValue(PluginConstant.GIF_PATH);
        if (currentPath != null && !currentPath.isEmpty()) {
            GifPlayer.INSTANCE.injectPainter(frame);
        }
        return this;
    }
}
