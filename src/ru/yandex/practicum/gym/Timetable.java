package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>>/* как это хранить??? */ timetable;

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
    }

    public /* непонятно, что возвращать */List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchelude = timetable.get(dayOfWeek);
        List<TrainingSession> allSessions = new ArrayList<>();

        for (TimeOfDay time : daySchelude.navigableKeySet()) {
            allSessions.addAll(daySchelude.get(time));
        }
        return allSessions;
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
    }

    public /* непонятно, что возвращать */List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchelude = timetable.get(dayOfWeek);
        return daySchelude.getOrDefault(timeOfDay, new ArrayList<>());
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
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

    private static class CoachTrainingCount {
        Coach coach;
        int count;

        CoachTrainingCount(Coach coach, int count) {
            this.coach = coach;
            this.count = count;
        }
    }
    public List<CoachTrainingCount> getCountByCoaches() {
        Map<String, Integer> coachCountMap = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            TreeMap<TimeOfDay, List<TrainingSession>> daySchelude = timetable.get(day);
            for (List<TrainingSession> sessions : daySchelude.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    String coachKey =  coach.getSurname() + " " + coach.getName() + " " + coach.getMiddleName();
                    coachCountMap.put(coachKey, coachCountMap.getOrDefault(coachKey, 0) + 1);
                }
            }
        }
        List<CoachTrainingCount> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : coachCountMap.entrySet()) {
            String[] nameParts = entry.getKey().split(" ");
            Coach coach = new Coach(nameParts[0], nameParts[1], nameParts[2] );
            result.add(new CoachTrainingCount(coach, entry.getValue()));
        }
        result.sort((c1, c2) -> Integer.compare(c2.count, c1.count));

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
