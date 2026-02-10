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

        // Получаем TreeMap и проверяем
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySchedule =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        // Считаем общее количество тренировок
        int totalSessions = 0;
        for (List<TrainingSession> sessions : mondaySchedule.values()) {
            totalSessions += sessions.size();
        }
        assertEquals(1, totalSessions);

        // Проверяем конкретную тренировку
        List<TrainingSession> sessionsAt13 = mondaySchedule.get(new TimeOfDay(13, 0));
        assertNotNull(sessionsAt13);
        assertEquals(1, sessionsAt13.size());
        assertEquals(singleTrainingSession, sessionsAt13.get(0));

        // Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySchedule =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySchedule.isEmpty());
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

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySchedule =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        int mondayTotal = 0;
        for (List<TrainingSession> sessions : mondaySchedule.values()) {
            mondayTotal += sessions.size();
        }
        assertEquals(1, mondayTotal);

        // Проверить, что за четверг вернулось два занятия
        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySchedule =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        // Проверяем, что есть занятия в 13:00 и 20:00
        assertTrue(thursdaySchedule.containsKey(new TimeOfDay(13, 0)));
        assertTrue(thursdaySchedule.containsKey(new TimeOfDay(20, 0)));

        // Проверяем порядок: ключи должны быть отсортированы
        List<TimeOfDay> times = new ArrayList<>(thursdaySchedule.keySet());
        assertEquals(new TimeOfDay(13, 0), times.get(0));  // сначала 13:00
        assertEquals(new TimeOfDay(20, 0), times.get(1));  // потом 20:00

        // Проверить, что за вторник не вернулось занятий
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySchedule =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySchedule.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> sessionsAt13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, sessionsAt13.size());
        assertEquals(singleTrainingSession, sessionsAt13.get(0));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> sessionsAt14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(sessionsAt14.isEmpty());
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 60);

        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        // Проверяем, что в одно время может быть несколько тренировок
        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(session1));
        assertTrue(sessions.contains(session2));
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Алексей", "Викторович");
        Group group = new Group("Фитнес", Age.ADULT, 90);

        // Добавляем 3 тренировки одного тренера
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));

        List<Timetable.CoachTrainingCount> counts = timetable.getCountByCoaches();
        assertEquals(1, counts.size());
        assertEquals(3, counts.get(0).getCount());
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Попов", "Дмитрий", "Сергеевич");
        Coach coach2 = new Coach("Кузнецова", "Ольга", "Ивановна");

        Group group1 = new Group("Стретчинг", Age.ADULT, 60);
        Group group2 = new Group("Аэробика", Age.ADULT, 60);

        // У тренера 1 - 4 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach1,
                DayOfWeek.TUESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group1, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach1,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 0)));

        // У тренера 2 - 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group1, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach2,
                DayOfWeek.SATURDAY, new TimeOfDay(11, 0)));

        List<Timetable.CoachTrainingCount> counts = timetable.getCountByCoaches();

        // Проверяем, что тренеры отсортированы по убыванию количества тренировок
        assertEquals(2, counts.size());
        assertEquals(4, counts.get(0).getCount());  // coach1 должен быть первым
        assertEquals(2, counts.get(1).getCount());  // coach2 должен быть вторым
    }

    @Test
    void testAddTrainingSessionToAllDays() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Смирнов", "Андрей", "Павлович");
        Group group = new Group("Кроссфит", Age.ADULT, 90);

        // Добавляем тренировки на все дни недели
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.addNewTrainingSession(
                    new TrainingSession(group, coach, day, new TimeOfDay(19, 0))
            );
        }

        // Проверяем, что на каждый день есть одна тренировка
        for (DayOfWeek day : DayOfWeek.values()) {
            TreeMap<TimeOfDay, List<TrainingSession>> schedule =
                    timetable.getTrainingSessionsForDay(day);
            int totalSessions = 0;
            for (List<TrainingSession> sessions : schedule.values()) {
                totalSessions += sessions.size();
            }
            assertEquals(1, totalSessions);
        }
    }
}