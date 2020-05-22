package io.github.whimthen.time.impl;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import io.github.whimthen.time.TimePatternService;
import io.github.whimthen.time.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@State(name = "io.github.whimthen.the_current_time", storages = {
    @Storage(value = "io.github.whimthen.the_current_time.xml")
})
public class TimePatternServiceImpl implements TimePatternService {

    private State state;

    @NotNull
    @Override
    public State get() {
        if (Objects.isNull(this.state)) {
            this.state = new State();
        }
        return this.state;
    }

    @Override
    public void save(String pattern) {
        if (this.state.patterns.contains(pattern)) {
            return;
        }
        this.state.patterns.add(pattern);
        loadState(this.state);
    }

    @NotNull
    @Override
    public List<String> getPatterns() {
        State state = get();
        List<String> patterns = TimeUtils.getDefaultPatterns();
        if (Objects.nonNull(state.patterns) && !state.patterns.isEmpty()) {
            patterns.addAll(state.patterns);
        }
        return patterns;
    }

    @Nullable
    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
    }

}
