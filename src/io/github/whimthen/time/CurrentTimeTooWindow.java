package io.github.whimthen.time;

import com.intellij.codeInspection.ui.ListTable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class CurrentTimeTooWindow implements ToolWindowFactory {

    public static ToolWindowPanel toolWindowPanel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        TimePatternService.getInstance().init();
        ContentFactory factory = ContentFactory.SERVICE.getInstance();
        toolWindowPanel = ToolWindowPanel.getInstance(project);
        Content content = factory.createContent(toolWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    @NotNull
    public ListTable getListTable() {
        return toolWindowPanel.getListTable();
    }

}
