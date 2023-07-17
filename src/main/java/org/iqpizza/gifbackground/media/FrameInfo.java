package org.iqpizza.gifbackground.media;

import java.awt.image.BufferedImage;

public record FrameInfo(int delay, BufferedImage image, String url) {
}
