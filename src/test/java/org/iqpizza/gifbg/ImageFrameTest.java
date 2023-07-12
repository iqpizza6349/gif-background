package org.iqpizza.gifbg;

import org.iqpizza.gifbg.debug.FrameInfo;
import org.iqpizza.gifbg.util.GifUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.OptionalDouble;

class ImageFrameTest {

    @Test
    void checkGifFrame() {
        List<FrameInfo> list = GifUtils.getFrameList("C:\\Users\\DGSW\\Downloads\\1674567200384－1.gif");
        System.out.println(list);
        assert list != null;
        OptionalDouble aDouble = list.stream().mapToInt(FrameInfo::delay).average();
        int delay;
        if (aDouble.isEmpty()) {
            delay = list.get(0).delay();
        }
        else {
            delay = (int) aDouble.getAsDouble();
        }

        //higher delay higher performance
        if (delay >= 100) {
            delay = 100;
        }
        System.out.println(delay);


        for (int i = 0; i < list.size(); i++) {
            try (final OutputStream stream = new FileOutputStream("D:\\jack\\02\\" + i + ".png")) {
                long start = System.currentTimeMillis();
                try (final InputStream is = new FileInputStream(list.get(i).path())) {
                    int buffer;
                    while ((buffer = is.read()) != -1) {
                        stream.write(buffer);
                    }
                }
                long stop = System.currentTimeMillis();
                System.out.printf("pic no.%d delay time: %dms%n", i, stop - start);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void blink() {
        for (int i = 0; i < 7; i++) {
            System.out.println("깜빡");
            try {
                Thread.sleep(3500);
            } catch (InterruptedException ignored) {}
        }
    }

}
