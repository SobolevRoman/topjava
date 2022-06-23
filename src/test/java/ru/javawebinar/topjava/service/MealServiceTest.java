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


import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

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
        Meal userMeal = service.get(userMeal5.getId(), USER_ID);
        assertMatch(userMeal, userMeal5);
        Meal adminMeal = service.get(adminMeal14.getId(), ADMIN_ID);
        assertMatch(adminMeal, adminMeal14);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(userMeal7.getId(), ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.get(adminMeal12.getId(), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(userMeal4.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(userMeal4.getId(), USER_ID));
        service.delete(adminMeal15.getId(), ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(adminMeal15.getId(), ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void deletedAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(userMeal11.getId(), ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.delete(adminMeal17.getId(), USER_ID));
    }

    @Test
    public void getBetweenWithDefaultDate() {
        assertMatch(service.getBetweenInclusive(null, null, USER_ID), userMeals);
        assertMatch(service.getBetweenInclusive(null, null, ADMIN_ID), adminMeals);
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(DATE_START, DATE_END, USER_ID), userMealsWithDateInterval);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), userMeals);
        assertMatch(service.getAll(ADMIN_ID), adminMeals);
    }

    @Test
    public void getAllNotFound() {
        assertTrue(service.getAll(NOT_FOUND).isEmpty());
    }

    @Test
    public void update() {
        service.update(getUpdated(userMeal7), USER_ID);
        assertMatch(service.get(userMeal7.getId(), USER_ID), getUpdated(userMeal7));
        service.update(getUpdated(adminMeal15), ADMIN_ID);
        assertMatch(service.get(adminMeal15.getId(), ADMIN_ID), getUpdated(adminMeal15));
    }

    @Test
    public void updatedNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(userMeal11), NOT_FOUND));
    }

    @Test
    public void updatedAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(userMeal11), ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(adminMeal17), USER_ID));
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
        assertThrows(DataAccessException.class, () -> service.create(
                new Meal(userMeal6.getDateTime(), "Duplicate", 100), USER_ID));
    }
}