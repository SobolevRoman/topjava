package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> {
            meal.setUserId(1);
            save(meal, 1);
        });
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            logger.info("save {}", meal);
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        } else {
            // handle case: update, but not present in storage
            logger.info("update {}", meal);
            if (checkMealForUser(meal, userId)) {
                return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        logger.info("delete meal with id {}", id);
        Meal meal = repository.get(id);
        return checkMealForUser(meal, userId) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        logger.info("get meal with id {}", id);
        Meal meal = repository.get(id);
        return checkMealForUser(meal, userId) ? meal : null;
    }

    private boolean checkMealForUser(Meal meal, int userId) {
        logger.info("check meal {} to user with id {}", meal, userId);
        return meal != null && userId == meal.getUserId();
    }

    @Override
    public List<Meal> getAll(int userId) {
        logger.info("get all without filter");
        return getAllTemplate(userId, meal -> true);
    }

    private List<Meal> getAllTemplate(int userId, Predicate<Meal> predicate) {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(predicate)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

