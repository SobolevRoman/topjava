package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName1", "email1@mail.ru", "password1", Role.ADMIN));
            adminUserController.create(new User(null, "userName2", "email2@mail.ru", "password2", Role.USER));
            adminUserController.create(new User(null, "userName3", "email3@mail.ru", "password3", Role.USER));
            System.out.println("--------------------------------------------");
            adminUserController.getAll().forEach(System.out::println);
            System.out.println("--------------------------------------------");
            adminUserController.update(new User(2, "name555", "em@gmail.com", "pass555", Role.USER), 2);
            adminUserController.getAll().forEach(System.out::println);
        }
    }
}
