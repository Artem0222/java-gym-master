package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    private HashMap<Coach, Integer> coachesCounter;

    public Timetable() {
        timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>(TIME_COMPARATOR));
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchelude = timetable.get(day);

        List<TrainingSession> sessionsAtTime = daySchelude.getOrDefault(time, new ArrayList<>());
        sessionsAtTime.add(trainingSession);
        daySchelude.put(time, sessionsAtTime);

        Coach currentCoach = trainingSession.getCoach();
        coachesCounter.put(currentCoach, coachesCounter.getOrDefault(currentCoach,0) + 1);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> traininForDay = timetable.get(dayOfWeek);
        if (traininForDay == null) {
            return new TreeMap<>(TIME_COMPARATOR);
        }

        return traininForDay;
        }



    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchelude = timetable.get(dayOfWeek);
        return daySchelude.getOrDefault(timeOfDay, new ArrayList<>());
    }

    private static final Comparator<TimeOfDay> TIME_COMPARATOR = new Comparator<TimeOfDay>() {
        @Override
        public int compare(TimeOfDay t1, TimeOfDay t2) {
            if (t1.getHours() != t2.getHours()) {
                return Integer.compare(t1.getHours(), t2.getHours());
            }
            return Integer.compare(t1.getMinutes(), t2.getMinutes());
        }
    };

    public List<CoachTrainingCount> getCountByCoaches() {
        List<CoachTrainingCount> result = new ArrayList<>();

        for (Map.Entry<Coach, Integer> entry : coachesCounter.entrySet()) {
            result.add(new CoachTrainingCount(entry.getKey(), entry.getValue()));
        }
        result.sort((c1, c2) -> Integer.compare(c2.getCount(), c1.getCount()));
        return result;
    }

    public static class CoachTrainingCount {
        private Coach coach;
        private int count;

        public CoachTrainingCount(Coach coach, int count) {
            this.coach = coach;
            this.count = count;
        }

        public Coach getCoach() {
            return coach;
        }

        public int getCount() {
            return count;
        }

        @Override
        public String toString() {
            return coach.getSurname() + " " + coach.getName() + " - " + count + " тренировок";
        }
    }
}
