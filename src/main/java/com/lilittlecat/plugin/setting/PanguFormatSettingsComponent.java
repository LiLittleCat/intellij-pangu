package com.lilittlecat.plugin.setting;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

/**+
 * @author LiLittleCat
 * @since 2022/8/11
 */
public class PanguFormatSettingsComponent {
    private final JPanel myMainPanel;
    private final JBCheckBox panguFormatWhenReformatCode = new JBCheckBox("Pangu Format when Reformat Code and Reformat Commit Message");

    public PanguFormatSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(panguFormatWhenReformatCode, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return panguFormatWhenReformatCode;
    }

    public boolean getPanguFormatWhenReformatCode() {
        return panguFormatWhenReformatCode.isSelected();
    }

    public void setPanguFormatWhenReformatCode(boolean newStatus) {
        panguFormatWhenReformatCode.setSelected(newStatus);
    }
}
