package org.iqpizza.gifbackground.component.dialog;

import com.intellij.CommonBundle;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.MultiLineLabelUI;
import com.intellij.openapi.ui.messages.MessageDialog;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.fields.ExtendableTextField;
import org.iqpizza.gifbackground.configuration.constraint.PluginConstraint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * 배경화면으로 설정할 파일을 선택할 수 있도록 하는 대화창 컴포넌트입니다.
 * 직접적으로 작성할 수도 디렉토리를 열어 선택할 수도 있습니다.
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public class FileInputDialog extends MessageDialog {

    private final String defaultValue;
    private final Consumer<String> selectable;
    private ExtendableTextField extendableTextField;

    public FileInputDialog(String defaultValue, Consumer<String> selectable) {
        this.defaultValue = defaultValue;
        this.selectable = selectable;
        myMessage = PluginConstraint.ENTER_GIF_MESSAGE;
        init();
        show();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return extendableTextField;
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{ new AbstractAction(CommonBundle.getOkButtonText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!extendableTextField.getText().isEmpty()) {
                    selectable.accept(extendableTextField.getText());
                }
                close(OK_EXIT_CODE);
            }
        }, new AbstractAction(CommonBundle.getCancelButtonText()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                close(CANCEL_EXIT_CODE);
            }
        } };
    }

    @Override
    protected JComponent createCenterPanel() {
        return null;
    }

    @Override
    protected @Nullable JComponent createNorthPanel() {
        JPanel panel = createIconPanel();
        panel.add(createMessagePanel(), BorderLayout.CENTER);
        return panel;
    }

    @Override
    protected @NotNull JPanel createMessagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createTextComponent(), BorderLayout.NORTH);
        panel.add(createComponentWithBrowseButton(), BorderLayout.SOUTH);
        return panel;
    }

    private JLabel createTextComponent() {
        JLabel jLabel = new JLabel(myMessage);
        jLabel.setUI(new MultiLineLabelUI());
        jLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 20));
        return jLabel;
    }

    private ComponentWithBrowseButton<ExtendableTextField> createComponentWithBrowseButton() {
        ExtendableTextField field = new ExtendableTextField(defaultValue);
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!field.getText().isEmpty()) {
                        selectable.accept(field.getText());
                    }
                    close(OK_EXIT_CODE);
                }
            }
        });
        extendableTextField = field;

        ComponentWithBrowseButton<ExtendableTextField> browseButton
                = new ComponentWithBrowseButton<>(field, (e) -> {
            VirtualFile file = FileChooser.chooseFile(new FileChooserDescriptor(true, false, false,
                            false, false, false), null, null);
            if (file != null) {
                String path = file.getPath();
                if (!path.isEmpty()) {
                    selectable.accept(path);
                    close(OK_EXIT_CODE);
                }
            }
        });
        browseButton.setPreferredSize(new Dimension(500, 30));
        return browseButton;
    }
}
