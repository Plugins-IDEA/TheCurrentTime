package io.github.whimthen.time;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface TimePatternService extends PersistentStateComponent<TimePatternService.State> {

    static TimePatternService getInstance() {
        return ServiceManager.getService(TimePatternService.class);
    }

    class State {
        public List<String> patterns = new ArrayList<>();
        public boolean isLoaded;
    }

    @NotNull
    State get();

    void save(String pattern);

    @NotNull
    List<String> getPatterns();

    void delete(String pattern);

    void init();

}
