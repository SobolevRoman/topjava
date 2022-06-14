package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger logger = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        logger.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        logger.info("update {}", meal);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public void delete(int id) {
        logger.info("delete meal by id - {}", id);
        service.delete(id, authUserId());
    }

    public Meal get(int id) {
        logger.info("get meal by id - {}", id);
        return service.get(id, authUserId());
    }

    public List<MealTo> getAll() {
        logger.info("get all meals without filtering");
        return MealsUtil.getTos(
                service.getAll(authUserId()),
                authUserCaloriesPerDay()
        );
    }

    public List<MealTo> getAllWithFilter(LocalDate startDate, LocalTime startTime, LocalTime endTime, LocalDate endDate) {
        logger.info("get all meals with filter");
        if (startDate == null) {
            startDate = LocalDate.MIN;
        }
        if (endDate == null) {
            endDate = LocalDate.MAX;
        }
        if (startTime == null) {
            startTime = LocalTime.MIN;
        }
        if (endTime == null) {
            endTime = LocalTime.MAX;
        }
        return MealsUtil.getFilteredTos(
                service.getAllWithFilter(authUserId(), startDate, endDate),
                authUserCaloriesPerDay(),
                startTime,
                endTime
        );
    }
}