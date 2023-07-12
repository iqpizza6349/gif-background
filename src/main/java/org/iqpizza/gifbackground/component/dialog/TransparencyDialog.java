package org.iqpizza.gifbackground.component.dialog;

import com.intellij.CommonBundle;
import com.intellij.openapi.ui.MultiLineLabelUI;
import com.intellij.openapi.ui.messages.MessageDialog;
import org.iqpizza.gifbackground.configuration.constraint.PluginConstraint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

/**
 * 배경화면의 불투명도를 선택할 수 있는 슬라이더형 컴포넌트입니다.
 * 최소 0에서 100까지 중 슬라이더를 움직여서 불투명도값을 지정할 수 있습니다.
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public class TransparencyDialog extends MessageDialog {

    private JSlider slider;
    private JLabel label;
    private final float defaultValue;
    private final Consumer<Float> changeable;

    public TransparencyDialog(float defaultValue, Consumer<Float> onValueChangeable) {
        myMessage = getMessage((int) (defaultValue * 100));
        this.defaultValue = defaultValue;
        this.changeable = onValueChangeable;
        init();
        show();
    }

    private String getMessage(int transparency) {
        return String.format(PluginConstraint.TRANSPARENCY_MESSAGE, transparency);
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return slider;
    }

    private JSlider createSliderComponent() {
        JSlider jSlider = new JSlider(0, 0, 100, 0);
        jSlider.setValue((int) (defaultValue * 100));
        jSlider.addChangeListener(e -> {
            label.setText(getMessage(jSlider.getValue()));
            changeable.accept(jSlider.getValue() / 100F);
        });
        jSlider.registerKeyboardAction(
                (e) -> close(OK_EXIT_CODE),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                WHEN_IN_FOCUSED_WINDOW
        );
        jSlider.setPreferredSize(new Dimension(300, 30));
        slider = jSlider;
        return jSlider;
    }

    private JLabel createTextComponent() {
        JLabel jLabel = new JLabel(myMessage);
        jLabel.setUI(new MultiLineLabelUI());
        jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 20));
        label = jLabel;
        return jLabel;
    }

    @Override
    protected @NotNull JPanel createMessagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createTextComponent(), BorderLayout.NORTH);
        panel.add(createSliderComponent(), BorderLayout.SOUTH);
        return panel;
    }

    @Override
    protected @Nullable JComponent createNorthPanel() {
        JPanel panel = createIconPanel();
        panel.add(createMessagePanel(), BorderLayout.CENTER);
        return panel;
    }

    @Override
    protected JComponent createCenterPanel() {
        return null;
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{ new AbstractAction(CommonBundle.getOkButtonText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                close(OK_EXIT_CODE);
            }
        } };
    }
}
