package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;

public class AverageLengthSession implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        OptionalDouble average = sessions.stream()
                .mapToDouble(session -> Duration.between(session.getSleepStart(), session.getSleepEnd()).toMinutes())
                .average();
        if (average.isPresent()) {
            return new SleepAnalysisResult("Средняя продолжительность сессии в минутах: ", Math.round(average.getAsDouble()));
        }
        return new SleepAnalysisResult("Средняя продолжительность сессии в минутах: ", "Сессий не найдено");
    }
}
