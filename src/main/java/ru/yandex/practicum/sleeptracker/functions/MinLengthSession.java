package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MinLengthSession implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Optional<Long> minLength = sessions.stream()
                .map(session -> Duration.between(session.getSleepStart(), session.getSleepEnd()).toMinutes())
                .min(Comparator.naturalOrder());
        if (minLength.isPresent()) {
            return new SleepAnalysisResult("Минимальная продолжительность сессии в минутах: ", minLength.get());
        }
        return new SleepAnalysisResult("Минимальная продолжительность сессии в минутах: ", "Сессий не найдено");
    }
}
