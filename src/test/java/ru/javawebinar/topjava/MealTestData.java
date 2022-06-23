package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.*;

public class MealTestData {
    public static final int NOT_FOUND = 10;
    public static final LocalDate DATE_START = LocalDate.of(2022, 1, 30);
    public static final LocalDate DATE_END = LocalDate.of(2022, 1, 31);
    public static final List<Meal> userMeals;
    public static final List<Meal> adminMeals;
    public static final List<Meal> userMealsWithDateInterval;

    public static final Meal userMeal3 = new Meal(START_SEQ + 3, LocalDateTime.of(2022, 1, 30, 10, 0), "Завтрак", 400);
    public static final Meal userMeal4 = new Meal(START_SEQ + 4, LocalDateTime.of(2022, 1, 30, 13, 0), "Пельмени", 1000);
    public static final Meal userMeal5 = new Meal(START_SEQ + 5, LocalDateTime.of(2022, 1, 30, 20, 0), "Салатик", 500);
    public static final Meal userMeal6 = new Meal(START_SEQ + 6, LocalDateTime.of(2022, 1, 31, 0, 0), "Сало", 200);
    public static final Meal userMeal7 = new Meal(START_SEQ + 7, LocalDateTime.of(2022, 1, 31, 19, 30), "Ужин", 1000);
    public static final Meal userMeal8 = new Meal(START_SEQ + 8, LocalDateTime.of(2022, 1, 31, 13, 25), "Обед", 500);
    public static final Meal userMeal9 = new Meal(START_SEQ + 9, LocalDateTime.of(2022, 1, 31, 20, 0), "Ужин 2", 410);
    public static final Meal userMeal10 = new Meal(START_SEQ + 10, LocalDateTime.of(2022, 2, 1, 20, 0), "Ужин 010222", 510);
    public static final Meal userMeal11 = new Meal(START_SEQ + 11, LocalDateTime.of(2022, 2, 1, 10, 25), "Завтрак user", 650);

    public static final Meal adminMeal12 = new Meal(START_SEQ + 12, LocalDateTime.of(2022, 1, 30, 10, 0), "Завтрак admin", 500);
    public static final Meal adminMeal13 = new Meal(START_SEQ + 13, LocalDateTime.of(2022, 1, 30, 13, 0), "Обед", 1000);
    public static final Meal adminMeal14 = new Meal(START_SEQ + 14, LocalDateTime.of(2022, 1, 30, 20, 0), "Ужин", 500);
    public static final Meal adminMeal15 = new Meal(START_SEQ + 15, LocalDateTime.of(2022, 1, 31, 0, 0), "Еда", 100);
    public static final Meal adminMeal16 = new Meal(START_SEQ + 16, LocalDateTime.of(2022, 1, 31, 10, 0), "Завтрак", 1000);
    public static final Meal adminMeal17 = new Meal(START_SEQ + 17, LocalDateTime.of(2022, 1, 31, 20, 0), "Ужин", 410);

    static {
        userMeals = Arrays.asList(userMeal10, userMeal11, userMeal9, userMeal7, userMeal8,
                userMeal6, userMeal5, userMeal4, userMeal3);

        adminMeals = Arrays.asList(adminMeal17, adminMeal16, adminMeal15,
                adminMeal14, adminMeal13, adminMeal12);

        userMealsWithDateInterval = Arrays.asList(userMeal9, userMeal7, userMeal8,
                userMeal6, userMeal5, userMeal4, userMeal3);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static Meal getUpdated(Meal m) {
        Meal updated = new Meal(m);
        updated.setDateTime(LocalDateTime.of(2222, 12, 12, 12, 12));
        updated.setDescription("updated");
        updated.setCalories(1212);
        return updated;
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(
                2222, 12, 12, 12, 12), "newMeal", 99);
    }
}
