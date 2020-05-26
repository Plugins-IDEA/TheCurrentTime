package io.github.whimthen.time;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CurrentTimeGroup extends ActionGroup {

    private static final List<AnAction> actions = new ArrayList<>();

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        if (actions.isEmpty()) {
            TimePatternService.getInstance().getPatterns().forEach(pattern -> actions.add(new CurrentTimeAction(pattern)));
        }
        return actions.toArray(new AnAction[0]);
    }

}
