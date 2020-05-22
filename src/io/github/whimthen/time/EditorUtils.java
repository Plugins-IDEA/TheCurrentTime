package io.github.whimthen.time;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class EditorUtils {

    public static void insert(@NotNull AnActionEvent e, String pattern) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getProject();
        final Document document = editor.getDocument();

        final SelectionModel selectionModel = editor.getSelectionModel();
        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();

        WriteCommandAction.runWriteCommandAction(project, () -> {
            String currentTime = TimeUtils.getCurrentTime(pattern);
            if (start == end) {
                document.insertString(start, currentTime);
            } else {
                document.replaceString(start, end, currentTime);
            }
        });
        selectionModel.removeSelection();
    }

}
