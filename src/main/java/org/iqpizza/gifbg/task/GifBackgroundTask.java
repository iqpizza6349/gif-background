package org.iqpizza.gifbg.task;

import com.intellij.openapi.application.ApplicationManager;
import org.iqpizza.gifbg.debug.FrameInfo;
import org.iqpizza.gifbg.util.BackgroundUtil;

import java.util.List;

public class GifBackgroundTask implements Runnable {
    private int index;
    private final List<FrameInfo> frameInfos;

    public GifBackgroundTask(List<FrameInfo> frameInfos) {
        this.index = 0;
        this.frameInfos = frameInfos;
    }

    @Override
    public void run() {
        index++;
        if (index >= frameInfos.size()) {
            index = 0;
        }

        ApplicationManager.getApplication().invokeLater(() ->
                BackgroundUtil.setBackground(frameInfos.get(index).path()));
    }
}
