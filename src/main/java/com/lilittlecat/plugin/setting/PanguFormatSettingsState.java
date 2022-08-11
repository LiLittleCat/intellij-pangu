package com.lilittlecat.plugin.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * @author LiLittleCat
 * @since 2022/8/11
 */
@State(
        name = "com.lilittlecat.plugin.setting.PanguFormatSettingsState",
        storages = @Storage("PanguFormatSettingsPlugin.xml")
)
public class PanguFormatSettingsState implements PersistentStateComponent<PanguFormatSettingsState> {
    public boolean panguFormatWhenReformatCode = true;

    @Nullable
    @Override
    public PanguFormatSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(PanguFormatSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static PanguFormatSettingsState getInstance() {
        return ApplicationManager.getApplication().getComponent(PanguFormatSettingsState.class);
    }

}
