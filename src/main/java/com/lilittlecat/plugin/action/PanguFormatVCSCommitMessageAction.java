package com.lilittlecat.plugin.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.ui.CommitMessage;
import com.lilittlecat.plugin.pangu.Pangu;
import org.jetbrains.annotations.NotNull;

/**
 * Pangy format commit message action.
 *
 * @author LiLittleCat
 * @see com.intellij.openapi.vcs.actions.ShowMessageHistoryAction
 * @since 2022/8/11
 */
public class PanguFormatVCSCommitMessageAction extends DumbAwareAction {

    public PanguFormatVCSCommitMessageAction() {
        setEnabledInModalContext(true);
    }

    public static final Integer ORDER = 1;

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        CommitMessage commitMessage = getCommitMessage(e);
        e.getPresentation().setVisible(project != null && commitMessage != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final CommitMessage commitMessage = getCommitMessage(e);
        if (commitMessage != null) {
            commitMessage.setCommitMessage(new Pangu().formatText(commitMessage.getComment()));
        }
    }

    private CommitMessage getCommitMessage(AnActionEvent e) {
        CommitMessageI data = VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
        if (data instanceof CommitMessage) {
            return (CommitMessage) data;
        } else {
            return null;
        }
    }
}
