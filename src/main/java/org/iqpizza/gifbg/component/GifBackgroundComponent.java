package org.iqpizza.gifbg.component;

import com.intellij.openapi.components.ProjectComponent;
import org.apache.commons.lang.StringUtils;
import org.iqpizza.gifbg.util.PropertyUtil;

@SuppressWarnings("deprecation")
public class GifBackgroundComponent implements ProjectComponent {

    @Override
    public void projectOpened() {
        String value = PropertyUtil.getValue(PropertyUtil.GIF_ENABLE);
        if (StringUtils.isEmpty(value)) {
            return;
        }

        String gifPath = PropertyUtil.getValue(PropertyUtil.GIF_PATH);
        if (StringUtils.isEmpty(gifPath)) {
            return;
        }

        // restart it
    }
}
