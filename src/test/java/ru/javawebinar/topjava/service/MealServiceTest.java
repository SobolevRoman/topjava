package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring-app-jdbc.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal mealUser = service.get(100005, USER_ID);
        assertMatch(mealUser, userMeal100005);
        Meal mealAdmin = service.get(100014, ADMIN_ID);
        assertMatch(mealAdmin, adminMeal100014);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(100004, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(100004, USER_ID));
        service.delete(100015, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(100015, ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> mealsUserMaxInterval = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(mealsUserMaxInterval, userMealsTestData);
        List<Meal> mealsAdminMaxInterval = service.getBetweenInclusive(null, null, ADMIN_ID);
        assertMatch(mealsAdminMaxInterval, adminMealsTestData);
        assertMatch(service.getBetweenInclusive(DATE_START, DATE_END, USER_ID), userMealsWithDateInterval);
    }

    @Test
    public void getAll() {
        List<Meal> userMeals = service.getAll(USER_ID);
        assertMatch(userMeals, userMealsTestData);
        List<Meal> adminMeals = service.getAll(ADMIN_ID);
        assertMatch(adminMeals, adminMealsTestData);
    }

    @Test
    public void getAllNotFound() {
        assertThrows(NotFoundException.class, () -> service.getAll(NOT_FOUND));
    }

    @Test
    public void update() {
        Meal updatedForUser = getUpdated(userMeal100007);
        service.update(updatedForUser, USER_ID);
        assertMatch(service.get(100007, USER_ID), updatedForUser);
        Meal updatedForAdmin = getUpdated(adminMeal100015);
        service.update(updatedForAdmin, ADMIN_ID);
        assertMatch(service.get(100015, ADMIN_ID), updatedForAdmin);
    }

    @Test
    public void updatedNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(userMeal100011), NOT_FOUND));
    }

    @Test
    public void create() {
        Meal createdForUser = service.create(getNew(), USER_ID);
        Integer newIdForUser = createdForUser.getId();
        Meal newMealForUser = getNew();
        newMealForUser.setId(newIdForUser);
        assertMatch(createdForUser, newMealForUser);
        assertMatch(service.get(newIdForUser, USER_ID), newMealForUser);

        Meal createdForAdmin = service.create(getNew(), ADMIN_ID);
        Integer newIdForAdmin = createdForAdmin.getId();
        Meal newMealForAdmin = getNew();
        newMealForAdmin.setId(newIdForAdmin);
        assertMatch(createdForAdmin, newMealForAdmin);
        assertMatch(service.get(newIdForAdmin, ADMIN_ID), newMealForAdmin);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(
                        new Meal(LocalDateTime.of(2022, 1, 31, 19, 30), "Duplicate", 100),
                        USER_ID));
    }
}