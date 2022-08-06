package com.lilittlecat.plugin.action;

import com.intellij.lang.Language;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.rename.inplace.InplaceRefactoring;
import org.jetbrains.annotations.NotNull;
import ws.vinta.pangu.Pangu;

import java.util.Objects;

/**
 * @author LiLittleCat
 * @since 2022/8/6
 */
public class PanguFormatAction extends AnAction {

    private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("Pangu Format", NotificationDisplayType.BALLOON, true);

    private static final Pangu PANGU = new Pangu();

    @Override
    public void actionPerformed(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        Language[] languages = LangDataKeys.CONTEXT_LANGUAGES.getData(dataContext);
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
                document.setText(formattedText);
            } else if (!selectedText.isBlank()) {
                // selection, pangu selected text
                int selectionStart = selectionModel.getSelectionStart();
                int selectionEnd = selectionModel.getSelectionEnd();
                String formattedText = PANGU.spacingText(selectedText);
                CommandProcessor.getInstance().executeCommand(file.getProject(), () -> {
                    WriteAction.run(() -> {
                        document.replaceString(selectionStart, selectionEnd, formattedText);
                    });
                }, "Pangu Format", null);
            }
            Notification notification = NOTIFICATION_GROUP.createNotification("Pangu Format:  '" + file.getName() + "' success.", MessageType.INFO);
            Notifications.Bus.notify(notification, e.getProject());
        } else {
            // notification: cannot edit current file
            Notification notification = NOTIFICATION_GROUP.createNotification("Pangu Format fail: '" + file.getName() + "' is not writable.", MessageType.WARNING);
            Notifications.Bus.notify(notification, e.getProject());
        }

//        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
//        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
//        String name = e.getRequiredData(CommonDataKeys.PSI_FILE).getViewProvider().getVirtualFile().getName();
    }
}
