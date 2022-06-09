package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RamMealDao implements MealDao {
    private static final Logger logger = LoggerFactory.getLogger(RamMealDao.class);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    public RamMealDao() {
        Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ).forEach(this::add);
    }

    @Override
    public void delete(int id) {
        logger.debug("delete meal by id {}", id);
        meals.remove(id);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == 0) {
            add(meal);
            logger.debug("add new meal");
        } else {
            if (!meals.containsKey(meal.getId())){
                return null;
            }
            meals.merge(meal.getId(), meal, (oldVal, newVal) -> newVal);
            logger.debug("update meal");
        }
        return meal;
    }

    private void add(Meal meal) {
        meal.setId(counter.incrementAndGet());
        meals.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        logger.debug("get list mealTo");
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(int id) {
        logger.debug("get meal by id {}", id);
        return meals.get(id);
    }
}
