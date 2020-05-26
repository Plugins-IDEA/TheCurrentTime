package io.github.whimthen.time.impl;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import io.github.whimthen.time.TimePatternService;
import io.github.whimthen.time.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@State(name = "io.github.whimthen.theCurrentTime", storages = {
    @Storage(value = "io.github.whimthen.theCurrentTime.xml")
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
        if (Objects.nonNull(state.patterns) && !state.patterns.isEmpty()) {
            return state.patterns;
        }
        return new ArrayList<>();
    }

    @Override
    public void delete(String pattern) {
        if (!this.state.patterns.contains(pattern)) {
            return;
        }
        this.state.patterns.remove(pattern);
        loadState(this.state);
    }

    @Override
    public void init() {
        State state = get();
        if (!state.isLoaded) {
            TimeUtils.getDefaultPatterns().forEach(this::save);
            state.isLoaded = true;
            loadState(state);
        }
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
