package io.github.whimthen.time.actions;

import com.intellij.codeInspection.ui.ListTable;
import com.intellij.codeInspection.ui.ListWrappingTableModel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.whimthen.time.CurrentTimeTooWindow;
import org.jetbrains.annotations.NotNull;

public class AddPatternAction extends AnAction {

    public static final String ID = "TheCurrentTime.AddPatternAction";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ListTable listTable = CurrentTimeTooWindow.toolWindowPanel.getListTable();
        ListWrappingTableModel tableModel = listTable.getModel();
        tableModel.addRow();
        listTable.editCellAt(tableModel.getRowCount() - 1, 0);
    }

}
