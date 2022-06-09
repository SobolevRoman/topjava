package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealDao;
import ru.javawebinar.topjava.repository.RamMealDao;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class MealServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2_000;
    private MealDao mealDao;

    @Override
    public void init() throws ServletException {
        mealDao = new RamMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action") == null ? "default" : req.getParameter("action");
        switch (action) {
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                mealDao.delete(id);
                resp.sendRedirect("meals");
                logger.debug("redirect to meals");
                break;
            case "edit":
                Meal meal;
                String id_String = req.getParameter("id");
                if (id_String == null) {
                    meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0);
                } else {
                    int parseId = Integer.parseInt(id_String);
                    meal = mealDao.getById(parseId);
                }
                req.setAttribute("meal", meal);
                req.getRequestDispatcher("/edit.jsp").forward(req, resp);
                logger.debug("forward to edit.jsp");
                break;
            default:
                List<MealTo> mealToList = MealsUtil.filteredByStreams(
                        mealDao.getAll(),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        CALORIES_PER_DAY);
                req.setAttribute("meals", mealToList);
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
                logger.debug("forward to meals.jsp");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        Meal meal = new Meal(
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));
        meal.setId(!id.isEmpty() ? Integer.parseInt(id) : 0);
        mealDao.save(meal);

        resp.sendRedirect("meals");
        logger.debug("redirect to meals");
    }
}
