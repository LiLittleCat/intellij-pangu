package com.lilittlecat.plugin.listener;

import com.intellij.codeInsight.actions.ReformatCodeAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.AnActionListener;
import com.lilittlecat.plugin.setting.PanguFormatSettingsState;
import org.jetbrains.annotations.NotNull;

/**
 * Reformat code action listener.
 *
 * @author LiLittleCat
 * @since 2022/8/12
 */
public class PanguFormatActionListener implements AnActionListener {

    @Override
    public void afterActionPerformed(@NotNull AnAction action, @NotNull DataContext dataContext, @NotNull AnActionEvent event) {
        if (action.getClass().equals(ReformatCodeAction.class) && PanguFormatSettingsState.getInstance().isPanguFormatWhenReformatCode) {
            // pangu format in reformat code action
            ActionManager.getInstance().getAction("Pangu.Format.Editor").actionPerformed(event);
        }
    }
}