package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MaxLengthSession implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Optional<Long> maxLength = sessions.stream()
                .map(session -> Duration.between(session.getSleepStart(), session.getSleepEnd()).toMinutes())
                .max(Comparator.naturalOrder());
        if (maxLength.isPresent()) {
            return new SleepAnalysisResult("Максимальная продолжительность сессии в минутах: ", maxLength.get());
        }
        return new SleepAnalysisResult("Максимальная продолжительность сессии в минутах: ", "Сессий не найдено");
    }
}
