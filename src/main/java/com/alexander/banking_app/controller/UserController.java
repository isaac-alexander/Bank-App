package com.alexander.banking_app.controller;

import com.alexander.banking_app.dto.UserDto;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("user", new UserDto());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // create user and account
        userService.registerUser(userDto);

        // flash success message
        redirectAttributes.addFlashAttribute("success", "account created successfully");

        // keep user on register page
        model.addAttribute("user", new UserDto());

        return "register";
    }

    // show edit page
    @GetMapping("/edit")
    public String editUser(HttpSession session, Model model) {

        // get session user
        User sessionUser = (User) session.getAttribute("loggedInUser");

        if (sessionUser == null) {
            return "redirect:/login";
        }

        // fetch from database using optional
        Optional<User> optionalUser = userRepository.findById(sessionUser.getId());

        if (optionalUser.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        User user = optionalUser.get();

        // map entity - dto
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());

        model.addAttribute("userDto", dto);

        return "edit-user";
    }

    // update user
    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("userDto") UserDto userDto,
                             HttpSession session) {

        // get session user
        User sessionUser = (User) session.getAttribute("loggedInUser");

        if (sessionUser == null) {
            return "redirect:/login";
        }

        // fetch user using optional
        Optional<User> optionalUser = userRepository.findById(sessionUser.getId());

        if (optionalUser.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        User user = optionalUser.get();

        // update
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setAddress(userDto.getAddress());

        // save updated user
        userRepository.save(user);

        // update session user
        session.setAttribute("loggedInUser", user);

        return "redirect:/dashboard";
    }
}