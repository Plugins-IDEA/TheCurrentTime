package io.github.whimthen.time;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CurrentTimeAction extends AnAction {

    private String pattern;

    public CurrentTimeAction() {
        super();
    }

    public CurrentTimeAction(String pattern) {
        super(pattern);
        this.pattern = pattern;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        EditorUtils.insert(event, this.pattern);
    }

}
