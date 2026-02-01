package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;

public class SleepingSession {

    private LocalDateTime sleepStart;
    private LocalDateTime sleepEnd;
    private SleepQuality quality;

    public SleepingSession(LocalDateTime sleepStart, LocalDateTime sleepEnd, SleepQuality quality) {
        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.quality = quality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getSleepEnd() {
        return sleepEnd;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return "SleepingSession{" +
                "sleepStart=" + sleepStart +
                ", sleepEnd=" + sleepEnd +
                ", quality=" + quality +
                '}';
    }
}
