package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalyzer implements SleepAnalysisFunction {

    private static final LocalTime END_OF_NIGHT = LocalTime.of(6, 0);
    private static final LocalTime START_NIGHT_EARLY_BIRD = LocalTime.of(22, 0);
    private static final LocalTime END_NIGHT_EARLY_BIRD = LocalTime.of(7, 0);
    private static final LocalTime START_NIGHT_OWL = LocalTime.of(23, 0);
    private static final LocalTime END_NIGHT_OWL = LocalTime.of(9, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя: ", "Сессий не найдено");
        }

        Map<Chronotype, Long> map = sessions.stream()
                .filter(this::filterGoodNights)
                .map(this::mapChronotypes)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long owlCount = map.getOrDefault(Chronotype.NIGHT_OWL, 0L);
        long earlyCount = map.getOrDefault(Chronotype.EARLY_BIRD, 0L);
        long pigeonCount = map.getOrDefault(Chronotype.PIGEON, 0L);
        Chronotype result = owlCount > earlyCount && owlCount > pigeonCount ? Chronotype.NIGHT_OWL
                : (earlyCount > owlCount && earlyCount > pigeonCount ? Chronotype.EARLY_BIRD : Chronotype.PIGEON);

        return new SleepAnalysisResult("Хронотип пользователя: ", result);
    }

    private boolean filterGoodNights(SleepingSession session) {
        LocalDateTime startTime = session.getSleepStart();
        LocalDateTime endTime = session.getSleepEnd();
        //либо имеют разные даты (пересечение с 0), либо сон начался раньше 6 утра
        return startTime.getDayOfMonth() != endTime.getDayOfMonth()
                || startTime.toLocalTime().isBefore(END_OF_NIGHT);
    }

    private Chronotype mapChronotypes(SleepingSession session) {
        LocalTime startTime = session.getSleepStart().toLocalTime();
        LocalTime endTime = session.getSleepEnd().toLocalTime();
        if ((startTime.isAfter(START_NIGHT_OWL) || startTime.isBefore(END_OF_NIGHT))
                && (endTime.isAfter(END_NIGHT_OWL))) {
            return Chronotype.NIGHT_OWL;
        }
        if (startTime.isBefore(START_NIGHT_EARLY_BIRD) && endTime.isBefore(END_NIGHT_EARLY_BIRD)) {
            return Chronotype.EARLY_BIRD;
        }
        return Chronotype.PIGEON;
    }
}
