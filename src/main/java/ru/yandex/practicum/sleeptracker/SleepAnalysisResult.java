package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {

    private final String resultName;
    private final Object result;

    public SleepAnalysisResult(String resultName, Object result) {
        this.resultName = resultName;
        this.result = result;
    }

    public String getResultName() {
        return resultName;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return resultName + ' ' + result;
    }
}
