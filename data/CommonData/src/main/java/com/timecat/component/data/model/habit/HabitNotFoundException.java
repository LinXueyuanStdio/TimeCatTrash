package com.timecat.component.data.model.habit;

public class HabitNotFoundException extends RuntimeException {
    public HabitNotFoundException() {
        super();
    }

    public HabitNotFoundException(String message) {
        super(message);
    }

    public HabitNotFoundException(Throwable cause) {
        super(cause);
    }

    public HabitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
