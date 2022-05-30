package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println();
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println();
        System.out.println(filteredByRecursive(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSum = new HashMap<>();
        meals.forEach(meal ->
                caloriesSum.merge(
                        meal.getDate(), meal.getCalories(), Integer::sum));

        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(meal -> {
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                result.add(createUserMealWithExcess(meal, caloriesSum, caloriesPerDay));
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSum = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createUserMealWithExcess(meal, caloriesSum, caloriesPerDay))
                .collect(Collectors.toList());
    }

    /**
     * Implementation of the method for Optional 2, using recursive
     */
    public static List<UserMealWithExcess> filteredByRecursive(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSum = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        return buildByRecursive(caloriesSum, result, meals, startTime, endTime, caloriesPerDay, 0);
    }

    private static List<UserMealWithExcess> buildByRecursive(
            Map<LocalDate, Integer> caloriesSum, List<UserMealWithExcess> result,
            List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay, int count) {
        if (count == meals.size()) {
            return result;
        }
        UserMeal meal = meals.get(count);
        caloriesSum.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        buildByRecursive(caloriesSum, result, meals, startTime, endTime, caloriesPerDay, ++count);

        if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
            result.add(createUserMealWithExcess(meal, caloriesSum, caloriesPerDay));
        }
        return result;
    }

    private static UserMealWithExcess createUserMealWithExcess(UserMeal meal, Map<LocalDate, Integer> caloriesSum, int caloriesPerDay) {
        return new UserMealWithExcess(
                meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
                caloriesSum.get(meal.getDate()) > caloriesPerDay);
    }
}
