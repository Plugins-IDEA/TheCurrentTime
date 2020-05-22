package io.github.whimthen.time;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurrentTimeGroup extends ActionGroup {

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        List<String> patterns = TimePatternService.getInstance().getPatterns();
        final AnAction[] actions = new AnAction[patterns.size()];
        for (int i = 0; i < patterns.size(); i++) {
            String pattern = patterns.get(i);
            String actionText = pattern;
            switch (pattern) {
                case TimeUtils.MILLISECONDS:
                    actionText = "Milliseconds";
                    break;
                case TimeUtils.SECONDS:
                    actionText = "Seconds";
                    break;
                case TimeUtils.NANO_TIME:
                    actionText = "NanoTime";
                    break;
            }
            actions[i] = new CurrentTimeAction(pattern, actionText);
        }
        return actions;
    }

}
