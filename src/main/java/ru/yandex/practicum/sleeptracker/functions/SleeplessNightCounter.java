package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final int HOUR_NEW_DAY = 12;

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        //количество ночей всего:
        LocalDateTime firstDayTime = sessions.getFirst().getSleepStart();
        LocalDateTime lastDayTime = sessions.getLast().getSleepEnd();
        long nightsOverall = Period.between(firstDayTime.toLocalDate(), lastDayTime.toLocalDate()).getDays();
        if (firstDayTime.getHour() < HOUR_NEW_DAY) {
            nightsOverall += 1;
        }
        //сколько не бессонных ночей
        long nightsGood = sessions.stream()
                .filter(session -> {
                    LocalDateTime startTime = session.getSleepStart();
                    LocalDateTime endTime = session.getSleepEnd();
                    //либо имеют разные даты (пересечение с 0), либо сон начался раньше 6 утра
                    return startTime.getDayOfMonth() != endTime.getDayOfMonth()
                        || (startTime.toLocalTime().isBefore(LocalTime.of(6, 0))
                            || startTime.toLocalTime().equals(LocalTime.of(6, 0)));
                })
                .count();

        long result = nightsOverall - nightsGood;

        return new SleepAnalysisResult("Количество бессонных ночей: ", result);
    }
}
