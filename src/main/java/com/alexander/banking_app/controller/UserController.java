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
import org.springframework.security.core.Authentication;


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
    // only showing changed methods

    @GetMapping("/edit")
    public String editUser(Authentication authentication, Model model) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        User user = optionalUser.get();

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());

        model.addAttribute("userDto", dto);

        return "edit-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userDto") UserDto userDto,
                             Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        User user = optionalUser.get();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setAddress(userDto.getAddress());

        userRepository.save(user);

        return "redirect:/dashboard";
    }

}