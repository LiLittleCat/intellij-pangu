package com.lilittlecat.plugin.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.PsiFile;
import ws.vinta.pangu.Pangu;

import java.text.MessageFormat;

/**
 * @author LiLittleCat
 * @since 2022/8/6
 */
public class PanguFormatAction extends AnAction {
    private static final String GROUP_ID = "Pangu Format";

    private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup(GROUP_ID, NotificationDisplayType.BALLOON, true);

    private static final Pangu PANGU = new Pangu();

    @Override
    public void actionPerformed(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        PsiFile file = CommonDataKeys.PSI_FILE.getData(dataContext);
        if (editor == null || file == null) {
            return;
        }
        Document document = editor.getDocument();
        if (document.isWritable()) {
            SelectionModel selectionModel = editor.getSelectionModel();
            String selectedText = selectionModel.getSelectedText();
            if (selectedText == null) {
                // no selection, pangu all content of current file
                String text = document.getText();
                String formattedText = PANGU.spacingText(text);
                CommandProcessor.getInstance().executeCommand(file.getProject(), () -> {
                    WriteAction.run(() -> {
                        document.setText(formattedText);
                    });
                }, GROUP_ID, null);
            } else if (!selectedText.isBlank()) {
                // selection, pangu selected text
                int selectionStart = selectionModel.getSelectionStart();
                int selectionEnd = selectionModel.getSelectionEnd();
                String formattedText = PANGU.spacingText(selectedText);
                CommandProcessor.getInstance().executeCommand(file.getProject(), () -> {
                    WriteAction.run(() -> {
                        document.replaceString(selectionStart, selectionEnd, formattedText);
                    });
                }, GROUP_ID, null);
            }
            notification(MessageFormat.format("{0}: '{1}' success.", GROUP_ID, file.getName()), MessageType.INFO, e.getProject());
        } else {
            // notification: cannot edit current file
            notification(MessageFormat.format("{0} fail: '{1}' is not writable.", GROUP_ID, file.getName()), MessageType.WARNING, e.getProject());
        }

//        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
//        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
//        String name = e.getRequiredData(CommonDataKeys.PSI_FILE).getViewProvider().getVirtualFile().getName();
    }

    /**
     * notification
     *
     * @param message     message to show
     * @param messageType message type
     * @param project     current project
     */
    private static void notification(String message, MessageType messageType, Project project) {
        Notification notification = NOTIFICATION_GROUP.createNotification(message, messageType);
        Notifications.Bus.notify(notification, project);
    }
}
