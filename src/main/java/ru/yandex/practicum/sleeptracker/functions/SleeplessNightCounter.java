package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;

public class SleeplessNightCounter implements SleepAnalysisFunction {

    private static final int HOUR_NEW_DAY = 12;
    private static final LocalTime END_OF_NIGHT = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей: ", "Сессий не найдено");
        }

        //количество ночей всего:
        LocalDateTime firstDayTime = sessions.getFirst().getSleepStart();
        LocalDateTime lastDayTime = sessions.getLast().getSleepEnd();
        long nightsOverall = Period.between(firstDayTime.toLocalDate(), lastDayTime.toLocalDate()).getDays();
        if (firstDayTime.getHour() < HOUR_NEW_DAY) {
            nightsOverall += 1;
        }
        //сколько не бессонных ночей
        long nightsGood = sessions.stream()
                .filter(this::filterGoodNights)
                .count();
        long result = nightsOverall - nightsGood;

        return new SleepAnalysisResult("Количество бессонных ночей: ", result);
    }

    private boolean filterGoodNights(SleepingSession session) {
        LocalDateTime startTime = session.getSleepStart();
        LocalDateTime endTime = session.getSleepEnd();
        //либо имеют разные даты (пересечение с 0), либо сон начался раньше 6 утра
        return startTime.getDayOfMonth() != endTime.getDayOfMonth()
                || (startTime.toLocalTime().isBefore(END_OF_NIGHT)
                || startTime.toLocalTime().equals(END_OF_NIGHT));
    }
}
