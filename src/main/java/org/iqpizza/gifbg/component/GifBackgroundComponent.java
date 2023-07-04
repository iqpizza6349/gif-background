package org.iqpizza.gifbg.component;

import com.intellij.openapi.components.ProjectComponent;
import org.apache.commons.lang.StringUtils;
import org.iqpizza.gifbg.util.PropertiesUtil;

/**
 * 이 클래스는 더 이상 사용되지 않는 Project Component 인터페이스를
 * 구현하였습니다. (ProjectActivity 는 코틀린이고, 자세한 사용방법이
 * 서술되지 않아 지금 당장 사용하기에는 어려워, deprecated 된 인터페이스 사용)
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
@SuppressWarnings("deprecation")
public class GifBackgroundComponent implements ProjectComponent {

    @Override
    public void projectOpened() {
        String value = PropertiesUtil.getValue(PropertiesUtil.GIF_ENABLE);
        if (StringUtils.isEmpty(value)) {
            return;
        }

        String gifPath = PropertiesUtil.getValue(PropertiesUtil.GIF_PATH);
        if (StringUtils.isEmpty(gifPath)) {
            return;
        }

        // restart the gif background feature
    }
}
