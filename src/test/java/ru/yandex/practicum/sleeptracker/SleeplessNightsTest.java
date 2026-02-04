package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.SleeplessNightCounter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SleeplessNightsTest {

    public DateTimeFormatter formatter;

    @BeforeEach
    public void setUp() {
        formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    }

    @Test
    public void testSleeplessNightsCounterAllCovered() {
        SleeplessNightCounter counter = new SleeplessNightCounter();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 23:30", formatter),
                        LocalDateTime.parse("07.01.26 05:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("08.01.26 00:30", formatter),
                        LocalDateTime.parse("08.01.26 07:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("09.01.26 01:30", formatter),
                        LocalDateTime.parse("09.01.26 05:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = counter.apply(sessions);
        Assertions.assertEquals(0L, result.getResult());
    }

    @Test
    public void testSleeplessNightsCounterFirstNightSleepless() {
        SleeplessNightCounter counter = new SleeplessNightCounter();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 17:30", formatter),
                        LocalDateTime.parse("06.01.26 18:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("08.01.26 00:30", formatter),
                        LocalDateTime.parse("08.01.26 07:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = counter.apply(sessions);
        Assertions.assertEquals(1L, result.getResult());
    }

    @Test
    public void testSleeplessNightsCounterEdges() { //считаем что границы входят
        SleeplessNightCounter counter = new SleeplessNightCounter();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 17:30", formatter),
                        LocalDateTime.parse("07.01.26 00:00", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("08.01.26 06:00", formatter),
                        LocalDateTime.parse("08.01.26 12:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = counter.apply(sessions);
        Assertions.assertEquals(0L, result.getResult());
    }

    @Test
    public void testSleeplessNightCounterMultipleSleeplessInARow() {
        SleeplessNightCounter counter = new SleeplessNightCounter();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 17:30", formatter),
                        LocalDateTime.parse("06.01.26 23:59", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("10.01.26 06:01", formatter),
                        LocalDateTime.parse("10.01.26 23:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = counter.apply(sessions);
        Assertions.assertEquals(4L, result.getResult());
    }

    @Test
    public void testSleeplessNightsEmptyFile() {
        SleeplessNightCounter counter = new SleeplessNightCounter();
        SleepAnalysisResult result = counter.apply(new ArrayList<>());
        Assertions.assertEquals("Сессий не найдено", result.getResult());
    }

    @Test
    public void testSleeplessNightsDifferentMonths() {
        SleeplessNightCounter counter = new SleeplessNightCounter();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("31.01.26 17:30", formatter),
                        LocalDateTime.parse("31.01.26 18:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("31.01.26 23:30", formatter),
                        LocalDateTime.parse("01.02.26 07:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = counter.apply(sessions);
        Assertions.assertEquals(0L, result.getResult());

        sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("30.01.26 22:00", formatter),
                        LocalDateTime.parse("31.01.26 08:00", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("01.02.26 07:00", formatter),
                        LocalDateTime.parse("01.02.26 11:00", formatter),
                        SleepQuality.NORMAL)
        );
        result = counter.apply(sessions);
        Assertions.assertEquals(1L, result.getResult());
    }

    @Test
    public void testSleeplessNightsFirstAfterMidnight() {
        SleeplessNightCounter counter = new SleeplessNightCounter();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("30.01.26 01:00", formatter),
                        LocalDateTime.parse("30.01.26 08:00", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("30.01.26 23:00", formatter),
                        LocalDateTime.parse("31.01.26 08:00", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = counter.apply(sessions);
        Assertions.assertEquals(0L, result.getResult());

        sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("30.01.26 08:00", formatter),
                        LocalDateTime.parse("30.01.26 11:00", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("31.01.26 01:00", formatter),
                        LocalDateTime.parse("31.01.26 08:00", formatter),
                        SleepQuality.NORMAL)
        );
        result = counter.apply(sessions);
        Assertions.assertEquals(1L, result.getResult());
    }

}
