package org.iqpizza.gifbackground.media;

import com.intellij.openapi.ui.AbstractPainter;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.Painter;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.iqpizza.gifbackground.configuration.constant.PluginConstant;
import org.iqpizza.gifbackground.util.GifStorage;
import org.iqpizza.gifbackground.util.PropertiesUtil;
import org.iqpizza.gifbackground.util.ReflectionUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class GifPlayer {
    public static final int STATE_STOPPED = 0;
    public static final int STATE_PLAYING = 1;
    private volatile int state;
    public static final GifPlayer INSTANCE = new GifPlayer();
    private final List<FrameInfo> images = new LinkedList<>();
    private final ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(4);
    private float canvasAlpha = .1F;
    private AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, canvasAlpha);
    private boolean initialized = false;
    private RepaintManager repaintManager;
    private Runnable repaintRunnable;
    private JRootPane rootPane;
    private VolatileImage frameImage;
    private Graphics2D frameImageGraphics;
    private long grabInterval = 0L;
    private int frame = 0;

    public void changeCanvasAlpha(float value) {
        if (canvasAlpha != value) {
            if (value > 1f) {
                value = 1f;
            }
            else if (value < 0F) {
                value = 0F;
            }
            canvasAlpha = value;
            alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, canvasAlpha);
        }
    }

    public boolean isStopped() {
        return state == STATE_STOPPED;
    }

    public boolean isPlaying() {
        return state == STATE_PLAYING;
    }

    public void stop() {
        initialized = false;
        changeState(STATE_STOPPED);
    }

    private void changeState(int newState) {
        this.state = newState;
    }

    private final AbstractPainter painter = new AbstractPainter() {
        @Override
        public boolean needsRepaint() {
            return isPlaying();
        }

        @Override
        public void executePaint(Component component, Graphics2D g) {
            final Composite oldComposite = g.getComposite();
            g.setComposite(alphaComposite);
            draw2Graphics(frameImage, g, paneSize);
            g.setComposite(oldComposite);
        }
    };

    @SuppressWarnings("BusyWait")
    private final Runnable imageProcessTask = () -> {
        try {
            while (initialized) {
                final long maxInterval = grabInterval * 2;
                FrameInfo info;
                if (isPlaying()) {
                    info = images.get(frame++);
                    _notify();
                    long occurredTime = measureTimeMillis(() -> draw(info.image()));
                    long interval = grabInterval - occurredTime;
                    if (interval > 0) {
                        _wait2(maxInterval);
                    }
                } else {
                    info = null;
                }

                if (frame >= images.size()) {
                    frame = 0;
                }

                Thread.sleep((info == null) ? 8 : info.delay());
            }
        } catch (Throwable t) {
            t.printStackTrace();
            showErrorDialog(t);
        }
        images.clear();
    };

    private final Runnable repaintTask = () -> {
        try {
            while (initialized) {
                if (isPlaying()) {
                    long occurredTime = measureTimeMillis(() -> {
                        try {
                            EventQueue.invokeAndWait(this::updateFrameImmediately);
                        } catch (Throwable t) {
                            throw new RuntimeException(t);
                        }
                    });
                    long interval = grabInterval - occurredTime;
                    if (interval > 0) {
                        _wait2(interval);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            showErrorDialog(t);
        }
    };

    private boolean repaintBarrier = false;
    private void updateFrameImmediately() {
        repaintManager.addDirtyRegion(rootPane, 0, 0, rootPane.getWidth(), rootPane.getHeight());
        repaintBarrier = false;
        repaintRunnable.run();
        repaintBarrier = true;
    }

    private void showErrorDialog(Throwable t) {
        showErrorDialog(t, "");
    }

    @SuppressWarnings("SameParameterValue")
    private void showErrorDialog(Throwable t, String message) {
        EventQueue.invokeLater(() -> Messages.showErrorDialog(ExceptionUtils.getStackTrace(t), message));
    }

    private final Runnable gifDecodeTask = () -> {
        List<FrameInfo> frames = GifStorage.getFrameList(PropertiesUtil.getValue(PluginConstant.GIF_PATH));
        if (frames == null || frames.isEmpty()) {
            return;
        }
        images.clear();
        images.addAll(frames);
        changeState(STATE_PLAYING);
        _notify2();
    };

    public void injectPainter(JFrame frame) {
        JRootPane pane = frame.getRootPane();
        if (!initialized) {
            addPainter(pane);
            replaceRepaintManager();
            rootPane = pane;
        }

        grabInterval = (1000 / 8);
        _notify();
        _notify2();
        lastRootPaneWidth = 0;
        lastRootPaneHeight = 0;
        Thread gifDecodeThread = new Thread(gifDecodeTask);
        gifDecodeThread.start();
        threadPool.execute(imageProcessTask);
        threadPool.execute(repaintTask);
    }

    private void addPainter(JRootPane rootPane) {
        IdeGlassPaneImpl glassPane = (IdeGlassPaneImpl) rootPane.getGlassPane();
        Object helper = getPaintersHelper(glassPane);
        ReflectionUtil.invokeVoid(helper.getClass(), helper, "addPainter",
                List.of(new Pair<>(Painter.class, painter), new Pair<>(Component.class, glassPane)));
        ReflectionUtil.get(Set.class, helper.getClass(), helper, "painters");
    }

    private Object getPaintersHelper(IdeGlassPaneImpl glassPane) {
        return ReflectionUtil.invoke(Object.class, IdeGlassPaneImpl.class, glassPane,
                "getNamedPainters$intellij_platform_ide_impl",
                List.of(new Pair<>(String.class, "idea.background.editor")));
    }
    
    private void replaceRepaintManager() {
        Object appContext = ReflectionUtil.invoke(Object.class, "sun.awt.AppContext",
                null, "getAppContext", List.of());
        this.repaintManager = new MyRepaintManager();
        ReflectionUtil.invokeVoid(appContext.getClass(), appContext, "put",
                List.of(new Pair<>(Object.class, RepaintManager.class), new Pair<>(Object.class, repaintManager)));
        repaintRunnable = ReflectionUtil.get(Runnable.class, RepaintManager.class, repaintManager, "processingRunnable");
        initialized = true;
    }

    private int lastRootPaneWidth = 0;
    private int lastRootPaneHeight = 0;
    private int paneWidth = 0;
    private int paneHeight = 0;
    private final Rectangle imageSize = new Rectangle();
    private final Rectangle paneSize = new Rectangle();

    private void updateCanvasSize(BufferedImage image) {
        final int imageWidth = image.getWidth(null);
        final int imageHeight = image.getHeight(null);
        imageSize.setBounds(0, 0, imageWidth, imageHeight);
        if (rootPane.getWidth() != lastRootPaneWidth
                || rootPane.getHeight() != lastRootPaneHeight) {
            if (rootPane.getWidth() < rootPane.getHeight()) {
                final float imageAspectRatio = (float) imageHeight / (float) imageWidth;
                paneWidth = rootPane.getWidth();
                paneHeight = (int) (((float) (paneWidth)) * imageAspectRatio);
            }
            else {
                final float imageAspectRatio = (float) imageWidth / (float) imageHeight;
                paneHeight = rootPane.getHeight();
                paneWidth = (int) ((float) (paneHeight) * imageAspectRatio);
            }
            lastRootPaneWidth = rootPane.getWidth();
            lastRootPaneHeight = rootPane.getHeight();
        }
        paneSize.setBounds(rootPane.getWidth() / 2 - paneWidth / 2, rootPane.getHeight() / 2 - paneHeight / 2, paneWidth, paneHeight);
    }

    private void draw(BufferedImage image) {
        updateCanvasSize(image);
        if (frameImage == null) {
            initializeFrameImage();
        }
        if (frameImageGraphics != null) {
            draw2Graphics(image, frameImageGraphics, imageSize);
        }

        frameImage.flush();
        image.flush();
    }

    private void draw2Graphics(Image image, Graphics g, Rectangle srcBounds) {
        if (image == null) {
            return;
        }

        final int sx = round(srcBounds.x);
        final int sy = round(srcBounds.y);
        final int sw = (srcBounds.width >= 0) ? round(srcBounds.width) : round(image.getWidth(null)) - sx;
        final int sh = (srcBounds.height >= 0) ? round(srcBounds.height) : round(image.getHeight(null)) - sy;
        g.drawImage(image, paneSize.x, paneSize.y, paneSize.x + paneSize.width,
                paneSize.y + paneSize.height, sx, sy, sx + sw, sy + sh, null
        );
        final GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage volatileImage = createVolatileImage(config);
        //apply
        volatileImage.validate(null);
        volatileImage.setAccelerationPriority(1F);
        g.drawImage(volatileImage, 0, 0, paneSize.x, paneSize.y + paneSize.height, null);
        g.drawImage(volatileImage, paneSize.x + paneSize.width, 0, paneSize.x, paneSize.y + paneSize.height, null);

    }

    private int round(int value) {
        return Math.round((float) value);
    }

    private void initializeFrameImage() {
        final GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        frameImage = createVolatileImage(config);
        //apply
        frameImage.validate(null);
        frameImage.setAccelerationPriority(1F);
        frameImageGraphics = frameImage.createGraphics();
        //apply
        frameImageGraphics.setComposite(AlphaComposite.Src);
        frameImageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        frameImageGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        frameImageGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        frameImageGraphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        frameImageGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        frameImageGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    }

    private VolatileImage createVolatileImage(GraphicsConfiguration config) {
        try {
            return config.createCompatibleVolatileImage(rootPane.getWidth(),
                    rootPane.getHeight(), new ImageCapabilities(true), Transparency.TRANSLUCENT);
        } catch (Exception e) {
            return config.createCompatibleVolatileImage(rootPane.getWidth(),
                    rootPane.getHeight(), Transparency.TRANSLUCENT);
        }
    }

    private long measureTimeMillis(TaskConsumer tc) {
        final long start = System.currentTimeMillis();
        tc.block();
        return System.currentTimeMillis() - start;
    }

    private void runCatchingSilently(TaskConsumer tc) {
        try {
            tc.block();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private final Object LOCK = new Object();
    private final Object LOCK2 = new Object();

    private void _wait2(long timeout) {
        runCatchingSilently(() -> {
            if (timeout > 0L) {
                synchronized (LOCK2) {
                    try {
                        LOCK2.wait(timeout);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void _notify() {
        runCatchingSilently(() -> {
            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        });
    }

    private void _notify2() {
        runCatchingSilently(() -> {
            synchronized (LOCK2) {
                LOCK2.notifyAll();
            }
        });
    }

    private static class MyRepaintManager extends RepaintManager {
        @Override
        public void paintDirtyRegions() {
            if (!INSTANCE.repaintBarrier || INSTANCE.isStopped()) {
                super.paintDirtyRegions();
            }
        }

        @Override
        public synchronized String toString() {
            return "MyRepaintManager" + super.toString();
        }
    }

    @FunctionalInterface
    private interface TaskConsumer {
        void block();
    }
}
