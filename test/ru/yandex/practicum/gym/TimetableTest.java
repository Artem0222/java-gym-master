package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAsList(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(singleTrainingSession, mondaySessions.get(0));

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDayAsList(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAsList(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDayAsList(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDayAsList(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessionsAt13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, sessionsAt13.size());

        List<TrainingSession> sessionsAt14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(sessionsAt14.isEmpty());
    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Фитнес", Age.ADULT, 90);

        // У тренера 1 - 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group1, coach1,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 0)));

        // У тренера 2 - 1 тренировка
        timetable.addNewTrainingSession(new TrainingSession(group2, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(18, 0)));

        List<Timetable.CoachTrainingInfo> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());
        assertEquals(coach1, result.get(0).getCoach());
        assertEquals(3, result.get(0).getTraininCount());
        assertEquals(coach2, result.get(1).getCoach());
        assertEquals(1, result.get(1).getTraininCount());
    }

    @Test
    void testMultipleSessionsSameTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Алексей", "Викторович");
        Group group1 = new Group("Бокс", Age.ADULT, 60);
        Group group2 = new Group("Карате", Age.CHILD, 45);

        TrainingSession session1 = new TrainingSession(group1, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));
        TrainingSession session2 = new TrainingSession(group2, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(19, 0));
        assertEquals(2, sessions.size());
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        for (DayOfWeek day : DayOfWeek.values()) {
            List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAsList(day);
            assertTrue(sessions.isEmpty());
        }

        List<Timetable.CoachTrainingInfo> coaches = timetable.getCountByCoaches();
        assertTrue(coaches.isEmpty());
    }
}