package com.timecat.component.data.model.habit;

/**
 * Interface implemented by factories that provide concrete implementations of
 * the core model classes.
 */
public interface ModelFactory {
    CheckmarkList buildCheckmarkList(Habit habit);

    default Habit buildHabit() {
        return new Habit(this);
    }
    default Habit buildHabit(Habit.HabitData data) {
        return new Habit(this, data);
    }

    HabitList buildHabitList();

    RepetitionList buildRepetitionList(Habit habit);

    ScoreList buildScoreList(Habit habit);

    StreakList buildStreakList(Habit habit);
}
