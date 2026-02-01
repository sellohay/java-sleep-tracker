package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        Map<Chronotype, Long> map = sessions.stream()
                .filter(session -> {
                    LocalDateTime startTime = session.getSleepStart();
                    LocalDateTime endTime = session.getSleepEnd();
                    //либо имеют разные даты (пересечение с 0), либо сон начался раньше 6 утра
                    return startTime.getDayOfMonth() != endTime.getDayOfMonth()
                            || startTime.toLocalTime().isBefore(LocalTime.of(6, 0));
                })
                .map(session -> {
                    LocalTime startTime = session.getSleepStart().toLocalTime();
                    LocalTime endTime = session.getSleepEnd().toLocalTime();
                    if ((startTime.isAfter(LocalTime.of(23, 0)) || startTime.isBefore(LocalTime.of(6, 0)))
                    && (endTime.isAfter(LocalTime.of(9, 0)))) {
                        return Chronotype.NIGHT_OWL;
                    }
                    if (startTime.isBefore(LocalTime.of(22, 0)) && endTime.isBefore(LocalTime.of(7, 0))) {
                        return Chronotype.EARLY_BIRD;
                    }
                    return Chronotype.PIGEON;
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long owlCount = map.getOrDefault(Chronotype.NIGHT_OWL, 0L);
        long earlyCount = map.getOrDefault(Chronotype.EARLY_BIRD, 0L);
        long pigeonCount = map.getOrDefault(Chronotype.PIGEON, 0L);
        Chronotype result = owlCount > earlyCount && owlCount > pigeonCount ? Chronotype.NIGHT_OWL
                : (earlyCount > owlCount && earlyCount > pigeonCount ? Chronotype.EARLY_BIRD : Chronotype.PIGEON);

        return new SleepAnalysisResult("Хронотип пользователя: ", result);
    }
}
