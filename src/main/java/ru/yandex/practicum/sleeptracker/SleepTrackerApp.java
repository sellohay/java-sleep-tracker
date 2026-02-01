package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {

    public static final String pathToSleep = "src/main/resources/sleep_log.txt";
    public static final List<Function<List<SleepingSession>, SleepAnalysisResult>> FUNCTIONS = List.of(
            new SessionCounter(),
            new MinLengthSession(),
            new MaxLengthSession(),
            new AverageLengthSession(),
            new BadSessionCounter(),
            new SleeplessNightCounter(),
            new ChronotypeAnalyzer()
    );

    public static void main(String[] args) {

        List<SleepingSession> sessions;
        try {
            DataLoader loader = new DataLoader(Paths.get(pathToSleep));
            sessions = loader.load();
            List<SleepAnalysisResult> results = SleepTrackerApp.analyze(sessions);
            results.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

    private static List<SleepAnalysisResult> analyze(List<SleepingSession> sessions) {
        return FUNCTIONS.stream()
                .map(function -> function.apply(sessions))
                .toList();
    }
}