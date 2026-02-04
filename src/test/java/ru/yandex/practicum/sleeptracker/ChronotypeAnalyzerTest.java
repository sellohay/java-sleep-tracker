package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.ChronotypeAnalyzer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChronotypeAnalyzerTest {

    public List<SleepingSession> sessions;
    public DateTimeFormatter formatter;

    @BeforeEach
    public void setUp() {
        formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 23:30", formatter),
                        LocalDateTime.parse("07.01.26 09:10", formatter),
                        SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.parse("08.01.26 01:20", formatter),
                        LocalDateTime.parse("08.01.26 07:30", formatter),
                        SleepQuality.BAD),
                new SleepingSession(LocalDateTime.parse("08.01.26 21:40", formatter),
                        LocalDateTime.parse("09.01.26 07:50", formatter),
                        SleepQuality.NORMAL)
        );
    }

    @Test
    public void testChronotypeOwl() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 23:30", formatter),
                        LocalDateTime.parse("07.01.26 09:01", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("08.01.26 01:01", formatter),
                        LocalDateTime.parse("09.01.26 10:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("09.01.26 22:01", formatter),
                        LocalDateTime.parse("10.01.26 06:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        Assertions.assertEquals(Chronotype.NIGHT_OWL, result.getResult());
    }

    @Test
    public void testChronotypeEarlyBird() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 21:30", formatter),
                        LocalDateTime.parse("07.01.26 06:01", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("07.01.26 19:01", formatter),
                        LocalDateTime.parse("08.01.26 03:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("08.01.26 23:30", formatter),
                        LocalDateTime.parse("09.01.26 05:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        Assertions.assertEquals(Chronotype.EARLY_BIRD, result.getResult());
    }

    @Test
    public void testChronotypePigeon() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 21:30", formatter),
                        LocalDateTime.parse("07.01.26 07:01", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("07.01.26 19:01", formatter),
                        LocalDateTime.parse("08.01.26 03:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("08.01.26 23:30", formatter),
                        LocalDateTime.parse("09.01.26 05:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        Assertions.assertEquals(Chronotype.PIGEON, result.getResult());
    }

    @Test
    public void testChronotypeEqual() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.parse("06.01.26 21:30", formatter),
                        LocalDateTime.parse("07.01.26 05:01", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("07.01.26 19:01", formatter),
                        LocalDateTime.parse("08.01.26 03:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("08.01.26 23:30", formatter),
                        LocalDateTime.parse("09.01.26 05:30", formatter),
                        SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.parse("09.01.26 23:30", formatter),
                        LocalDateTime.parse("10.01.26 07:30", formatter),
                        SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        Assertions.assertEquals(Chronotype.PIGEON, result.getResult());
    }

    @Test
    public void testChronotypeEmptyFile() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        SleepAnalysisResult result = analyzer.apply(new ArrayList<>());
        Assertions.assertEquals("Сессий не найдено", result.getResult());
    }

}
