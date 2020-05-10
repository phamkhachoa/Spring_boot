package com.example.demo.controller;

import com.example.demo.repositories.entities.Account;
import com.example.demo.service.IAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private IAccountService iAccountService;

    @GetMapping("/signup")
    public String showSignUpForm(Account account) {
        //model.addAttribute("account", new Account());
        return "addAccount";
    }

    @GetMapping("/home")
    public String home(Model model) {

        //accountRepository.save(account);
        model.addAttribute("users", iAccountService.findAll());
        return "index";
    }

    @PostMapping(value="/addUser")
    public String addUser(@Valid Account account, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addAccount";
        }
        account.setPassword("admin");
        account.setCreatedBy("1");
        account.setStatus(1);
        //account.setId("34");

        iAccountService.save(account);
        model.addAttribute("users", iAccountService.findAll());
        return "redirect:home";
    }

    @RequestMapping(value = "/searchByEmail", method = RequestMethod.POST)
    public @ResponseBody String searchPerson(HttpServletRequest request) {
        String email = request.getParameter("email");
        Account account = iAccountService.findByEmail(email);
        ObjectMapper mapper = new ObjectMapper();
        String ajaxResponse = "";
        try {
            ajaxResponse = mapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ajaxResponse;
    }

    @PostMapping(value="/searchUser")
    public String addUser(@RequestParam String email, Model model) {
        model.addAttribute("users", iAccountService.findAccountByEmailParamsNative(email));
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        Optional<Account> account = iAccountService.findById(id);
        model.addAttribute("account", account.get());
        return "updateAccount";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") String id, @Valid Account account,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            account.setId(id);
            return "updateAccount";
        }

        iAccountService.save(account);
        model.addAttribute("users", iAccountService.findAll());
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id, Model model) {
        Optional<Account> account = iAccountService.findById(id);
        iAccountService.delete(account.get());
        model.addAttribute("users", iAccountService.findAll());
        return "redirect:/home";
    }
}
