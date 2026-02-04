package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class SessionCounter implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> s) {
        return new SleepAnalysisResult("Количество сессий за данный период: ", s.size());
    }
}
