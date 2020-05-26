package io.github.whimthen.time.actions;

import com.intellij.codeInspection.ui.ListTable;
import com.intellij.codeInspection.ui.ListWrappingTableModel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.whimthen.time.CurrentTimeTooWindow;
import io.github.whimthen.time.TimePatternService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeletePatternAction extends AnAction {

    public static final String ID = "TheCurrentTime.DeletePatternAction";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ListTable listTable = CurrentTimeTooWindow.toolWindowPanel.getListTable();
        ListWrappingTableModel tableModel = listTable.getModel();
        int[] selectedRows = listTable.getSelectedRows();
        if (selectedRows != null && selectedRows.length > 0 && listTable.getRowCount() > 0) {
            int index = 0;
            int maxSelectedRow = 0;
            int deletedCount = 0;
            do {
                int selectedRow = selectedRows[index];
                if (selectedRow > maxSelectedRow)
                    maxSelectedRow = selectedRow;
                Object value = listTable.getValueAt(Math.max(selectedRow - deletedCount, 0), 0);
                if (Objects.nonNull(value) && StringUtils.isNotBlank(value.toString())) {
                    TimePatternService.getInstance().delete(value.toString());
                }
                tableModel.removeRow(selectedRow - deletedCount);
                index++;
                deletedCount++;
            } while (index < selectedRows.length);
            listTable.changeSelection(Math.max(maxSelectedRow - deletedCount, 0), 0, false, false);
        }
    }

}
