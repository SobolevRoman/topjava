package ru.javawebinar.topjava.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {
    private static final String viewName = "profile";

    @GetMapping
    public String profile() {
        return viewName;
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return viewName;
        } else {
            super.update(userTo, SecurityUtil.authUserId());
            SecurityUtil.get().setTo(userTo);
            status.setComplete();
            return "redirect:/meals";
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return viewName;
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return viewName;
        } else {
            super.create(userTo);
            status.setComplete();
            return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
        }
    }
}