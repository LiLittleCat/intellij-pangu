package com.lilittlecat.plugin.action;

import com.intellij.codeInsight.actions.ReformatCodeAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Pangu Format Action Promoter
 * todo, click Code > Reformat Code will not work.
 *
 * @author LiLittleCat
 * @since 2022/8/11
 */
public class PanguFormatInReformatAction extends ReformatCodeAction {

    private final PanguFormatEditorAction panguFormatEditorAction;

    public PanguFormatInReformatAction() {
        super();
        ActionManager actionManager = ActionManager.getInstance();
        panguFormatEditorAction = (PanguFormatEditorAction) actionManager.getAction("Pangu.Format.Editor");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        super.actionPerformed(event);
        panguFormatEditorAction.actionPerformed(event);
    }
}
