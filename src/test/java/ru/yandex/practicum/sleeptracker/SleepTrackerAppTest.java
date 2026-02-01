package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SleepTrackerAppTest {

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
    public void testDataLoader() throws IOException {
        Path file = Paths.get("src/main/resources/sleep_log.txt");
        DataLoader dataLoader = new DataLoader(file);
        List<SleepingSession> sessions = dataLoader.load();
        Assertions.assertNotNull(sessions);
        assertFalse(sessions.isEmpty());
    }

    @Test
    public void testLoaderFileNotFound() {
        Path file = Paths.get("src/main/resources/sleep.txt");
        DataLoader dataLoader = new DataLoader(file);
        assertThrows(FileNotFoundException.class, dataLoader::load);
    }

    @Test
    public void testSessionCounter() {
        SessionCounter counter = new SessionCounter();
        SleepAnalysisResult result = counter.apply(sessions);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, (Integer) result.getResult());
    }

    @Test
    public void testSessionCounterWith0() {
        SessionCounter counter = new SessionCounter();
        SleepAnalysisResult result = counter.apply(new ArrayList<>());
        Assertions.assertEquals(0, (Integer) result.getResult());
    }

    @Test
    public void testMaxLengthSession() {
        MaxLengthSession sessionGetter = new MaxLengthSession();
        SleepAnalysisResult result = sessionGetter.apply(sessions);
        Assertions.assertEquals(610L, result.getResult());
    }

    @Test
    public void testMaxLengthSessionWith0() {
        MaxLengthSession sessionGetter = new MaxLengthSession();
        SleepAnalysisResult result = sessionGetter.apply(new ArrayList<>());
        Assertions.assertEquals("Сессий не найдено", result.getResult());
    }

    @Test
    public void testMinLengthSession() {
        MinLengthSession sessionGetter = new MinLengthSession();
        SleepAnalysisResult result = sessionGetter.apply(sessions);
        Assertions.assertEquals(370L, result.getResult());
    }

    @Test
    public void testMinLengthSessionWith0() {
        MinLengthSession sessionGetter = new MinLengthSession();
        SleepAnalysisResult result = sessionGetter.apply(new ArrayList<>());
        Assertions.assertEquals("Сессий не найдено", result.getResult());
    }

    @Test
    public void testAverageLengthSession() {
        AverageLengthSession sessionGetter = new AverageLengthSession();
        SleepAnalysisResult result = sessionGetter.apply(sessions);
        Assertions.assertEquals(520L, result.getResult());
    }

    @Test
    public void testAverageLengthSessionWith0() {
        AverageLengthSession sessionGetter = new AverageLengthSession();
        SleepAnalysisResult result = sessionGetter.apply(new ArrayList<>());
        Assertions.assertEquals("Сессий не найдено", result.getResult());
    }

    @Test
    public void testBadSessionCounter() {
        BadSessionCounter counter = new BadSessionCounter();
        SleepAnalysisResult result = counter.apply(sessions);
        Assertions.assertEquals(1L, result.getResult());
    }

    @Test
    public void testBadSessionCounterWith0() {
        BadSessionCounter counter = new BadSessionCounter();
        SleepAnalysisResult result = counter.apply(new ArrayList<>());
        Assertions.assertEquals(0L, result.getResult());
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


}