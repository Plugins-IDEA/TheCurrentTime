package io.github.whimthen.time;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import io.github.whimthen.time.actions.AddPatternAction;
import io.github.whimthen.time.actions.DeletePatternAction;
import io.github.whimthen.time.actions.EditPatternAction;

public class Actions {

    public static AddPatternAction getAddAction() {
        return (AddPatternAction) ActionManager.getInstance().getAction(AddPatternAction.ID);
    }

    public static DeletePatternAction getDeleteAction() {
        return (DeletePatternAction) ActionManager.getInstance().getAction(DeletePatternAction.ID);
    }

    public static EditPatternAction getEditAction() {
        return (EditPatternAction) ActionManager.getInstance().getAction(EditPatternAction.ID);
    }

}
