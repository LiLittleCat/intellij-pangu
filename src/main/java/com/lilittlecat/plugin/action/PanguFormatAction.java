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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                // todo use file to convert?
                CommandProcessor.getInstance().executeCommand(file.getProject(), () ->
                        WriteAction.run(() -> document.setText(formatText(text))), GROUP_ID, null);
            } else if (!selectedText.isBlank()) {
                // selection, pangu selected text
                int selectionStart = selectionModel.getSelectionStart();
                int selectionEnd = selectionModel.getSelectionEnd();
                // todo how to do with selection?
                CommandProcessor.getInstance().executeCommand(file.getProject(), () ->
                        WriteAction.run(() ->
                                document.replaceString(selectionStart, selectionEnd, formatText(selectedText))), GROUP_ID, null);
            }
            notification(MessageFormat.format("{0}: \"{1}\" success.", GROUP_ID, file.getName()), MessageType.INFO, e.getProject());
        } else {
            // notification: cannot edit current file
            notification(MessageFormat.format("{0} fail: \"{1}\" is not writable.", GROUP_ID, file.getName()), MessageType.WARNING, e.getProject());
        }
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

    /**
     * split text by line break
     *
     * @param text text to split
     * @return list of text
     */
    private List<String> splitText(String text) {
        return new ArrayList<>(Arrays.asList(text.split("\n")));
    }

    /**
     * join text by line break
     *
     * @param text text to join
     * @return joined text
     */
    private String joinText(List<String> text) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (String s : text) {
            stringBuilder.append(s).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * format text
     *
     * @param text text to format
     * @return formatted text
     */
    private String formatText(String text) {
        List<String> formattedLines = new ArrayList<>();
        for (String s : splitText(text)) {
            formattedLines.add(PANGU.spacingText(s));
        }
        return PANGU.spacingText(joinText(formattedLines));
    }
}
