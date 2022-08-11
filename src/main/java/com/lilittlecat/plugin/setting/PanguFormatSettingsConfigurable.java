package com.lilittlecat.plugin.setting;

import com.intellij.openapi.options.SearchableConfigurable;
import com.lilittlecat.plugin.common.Constant;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.vcs.changes.ignore.lang.IgnoreCommenter;import com.intellij.openapi.project.DumbAware;

import javax.swing.*;

/**
 * @author LiLittleCat
 * @since 2022/8/11
 */
public class PanguFormatSettingsConfigurable implements SearchableConfigurable {
    private PanguFormatSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return Constant.DISPLAY_NAME;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new PanguFormatSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        PanguFormatSettingsState settings = PanguFormatSettingsState.getInstance();
        return mySettingsComponent.getPanguFormatWhenReformatCode() != settings.panguFormatWhenReformatCode;
    }

    @Override
    public void apply() {
        PanguFormatSettingsState settings = PanguFormatSettingsState.getInstance();
        settings.panguFormatWhenReformatCode = mySettingsComponent.getPanguFormatWhenReformatCode();
    }

    @Override
    public void reset() {
        PanguFormatSettingsState settings = PanguFormatSettingsState.getInstance();
        mySettingsComponent.setPanguFormatWhenReformatCode(settings.panguFormatWhenReformatCode);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

    @NotNull
    @Override
    public String getId() {
        return "com.lilittlecat.plugin.setting.PanguFormatSettingsConfigurable";
    }
}
