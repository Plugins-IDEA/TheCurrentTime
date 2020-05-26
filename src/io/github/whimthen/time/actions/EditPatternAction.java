package io.github.whimthen.time.actions;

import com.intellij.codeInspection.ui.ListTable;
import com.intellij.codeInspection.ui.ListWrappingTableModel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.whimthen.time.CurrentTimeTooWindow;
import org.jetbrains.annotations.NotNull;

public class EditPatternAction extends AnAction {

    public static final String ID = "TheCurrentTime.EditPatternAction";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ListTable listTable = CurrentTimeTooWindow.toolWindowPanel.getListTable();
        int selectedRow = listTable.getSelectedRow();
        if (selectedRow != -1) {
            listTable.editCellAt(selectedRow, 0);
        }
    }

}
