package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.*;

public class MealTestData {
    public static final LocalDate DATE_START = LocalDate.of(2022, 1, 30);
    public static final LocalDate DATE_END = LocalDate.of(2022, 1, 31);
    public static final List<Meal> userMealsTestData;
    public static final List<Meal> adminMealsTestData;
    public static final List<Meal> userMealsWithDateInterval;

    public static final Meal userMeal100003 = new Meal(START_SEQ + 3, LocalDateTime.of(2022, 1, 30, 10, 0), "Завтрак", 400);
    public static final Meal userMeal100004 = new Meal(START_SEQ + 4, LocalDateTime.of(2022, 1, 30, 13, 0), "Пельмени", 1000);
    public static final Meal userMeal100005 = new Meal(START_SEQ + 5, LocalDateTime.of(2022, 1, 30, 20, 0), "Салатик", 500);
    public static final Meal userMeal100006 = new Meal(START_SEQ + 6, LocalDateTime.of(2022, 1, 31, 0, 0), "Сало", 200);
    public static final Meal userMeal100007 = new Meal(START_SEQ + 7, LocalDateTime.of(2022, 1, 31, 19, 30), "Ужин", 1000);
    public static final Meal userMeal100008 = new Meal(START_SEQ + 8, LocalDateTime.of(2022, 1, 31, 13, 25), "Обед", 500);
    public static final Meal userMeal100009 = new Meal(START_SEQ + 9, LocalDateTime.of(2022, 1, 31, 20, 0), "Ужин 2", 410);
    public static final Meal userMeal100010 = new Meal(START_SEQ + 10, LocalDateTime.of(2022, 2, 1, 20, 0), "Ужин 010222", 510);
    public static final Meal userMeal100011 = new Meal(START_SEQ + 11, LocalDateTime.of(2022, 2, 1, 10, 25), "Завтрак user", 650);

    public static final Meal adminMeal100012 = new Meal(START_SEQ + 12, LocalDateTime.of(2022, 1, 30, 10, 0), "Завтрак admin", 500);
    public static final Meal adminMeal100013 = new Meal(START_SEQ + 13, LocalDateTime.of(2022, 1, 30, 13, 0), "Обед", 1000);
    public static final Meal adminMeal100014 = new Meal(START_SEQ + 14, LocalDateTime.of(2022, 1, 30, 20, 0), "Ужин", 500);
    public static final Meal adminMeal100015 = new Meal(START_SEQ + 15, LocalDateTime.of(2022, 1, 31, 0, 0), "Еда", 100);
    public static final Meal adminMeal100016 = new Meal(START_SEQ + 16, LocalDateTime.of(2022, 1, 31, 10, 0), "Завтрак", 1000);
    public static final Meal adminMeal100017 = new Meal(START_SEQ + 17, LocalDateTime.of(2022, 1, 31, 20, 0), "Ужин", 410);

    static {
        userMealsTestData = Stream.of(
                        userMeal100003, userMeal100004, userMeal100005, userMeal100006, userMeal100007,
                        userMeal100008, userMeal100009, userMeal100010, userMeal100011)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());

        adminMealsTestData = Stream.of(
                        adminMeal100012, adminMeal100013, adminMeal100014, adminMeal100015,
                        adminMeal100016, adminMeal100017)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());

        userMealsWithDateInterval = Stream.of(
                        userMeal100003, userMeal100004, userMeal100005, userMeal100006, userMeal100007,
                        userMeal100008, userMeal100009)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
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
        return new Meal(null, LocalDateTime.of(2222, 12, 12, 12, 12), "newMeal", 99);
    }
}
