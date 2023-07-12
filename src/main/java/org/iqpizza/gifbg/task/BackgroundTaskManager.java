package org.iqpizza.gifbg.task;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import org.iqpizza.gifbg.debug.FrameInfo;
import org.iqpizza.gifbg.util.BackgroundUtil;
import org.iqpizza.gifbg.util.DialogUtil;
import org.iqpizza.gifbg.util.GifUtils;
import org.iqpizza.gifbg.util.PropertiesUtil;

import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackgroundTaskManager {
    private static final Logger log = Logger.getInstance(BackgroundTaskManager.class);

    private static ScheduledExecutorService executorService = null;

    public static void start(String gifPath) {
        List<FrameInfo> list = GifUtils.getFrameList(gifPath);
        if (list == null || list.isEmpty()) {
            return;
        }

        if (executorService != null) {
            stop();
        }

        GifBackgroundTask task = new GifBackgroundTask(list);
        executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            BackgroundUtil.clearBackground();
            OptionalDouble aDouble = list.stream().mapToInt(FrameInfo::delay).average();
            int interval;
            if (aDouble.isEmpty()) {
                interval = list.get(0).delay();
            }
            else {
                interval = (int) aDouble.getAsDouble();
            }

            interval *= 50;
            PropertiesComponent prop = PropertiesComponent.getInstance();
            int delay = prop.isValueSet(IdeBackgroundUtil.EDITOR_PROP) ? interval : 0;
            executorService.scheduleAtFixedRate(task, delay, delay, TimeUnit.MILLISECONDS);
            PropertiesUtil.saveValue(PropertiesUtil.GIF_PATH, gifPath);
        } catch (RejectedExecutionException e) {
            log.warn(e);
            stop();
            DialogUtil.showError(e.getMessage());
        }
    }

    public static void restart(String gifPath) {
        stop();
        start(gifPath);
    }

    public static void stop() {
        if (executorService != null && !executorService.isTerminated()) {
            executorService.shutdownNow();
        }

        executorService = null;
    }
}
