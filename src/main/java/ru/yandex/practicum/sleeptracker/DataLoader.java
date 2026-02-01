package ru.yandex.practicum.sleeptracker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoader {

    private final Path path;
    private final DateTimeFormatter formatter;

    public DataLoader(Path path) {
        this.path = path;
        this.formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    }

    public List<SleepingSession> load() throws IOException {

        if (!Files.exists(path)) {
            throw new FileNotFoundException("Ошибка: файл сессий не найден.");
        }

        List<SleepingSession> sessions;
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            sessions = lines
                    .map(line -> line.split(";"))
                    .map(line -> {
                        LocalDateTime sleepStart = LocalDateTime.parse(line[0], formatter);
                        LocalDateTime sleepEnd = LocalDateTime.parse(line[1], formatter);
                        SleepQuality quality = SleepQuality.valueOf(line[2].toUpperCase());
                        return new SleepingSession(sleepStart, sleepEnd, quality);
                    } )
                    .collect(Collectors.toList());
        }

        return sessions;
    }
}
