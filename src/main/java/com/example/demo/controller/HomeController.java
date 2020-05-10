package com.example.demo.controller;

import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.entities.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    IAccountRepository accountRepository;

    @GetMapping("/signup")
    public String showSignUpForm(Account account) {
        //model.addAttribute("account", new Account());
        return "addAccount";
    }

    @GetMapping("/home")
    public String home(Model model) {

        //accountRepository.save(account);
        model.addAttribute("users", accountRepository.findAll());
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

        accountRepository.save(account);
        model.addAttribute("users", accountRepository.findAll());
        return "redirect:home";
    }

    @RequestMapping(value = "/searchByEmail", method = RequestMethod.POST)
    public @ResponseBody String searchPerson(HttpServletRequest request) {
        String email = request.getParameter("email");
        Account account = accountRepository.findByEmail(email);
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
        model.addAttribute("users", accountRepository.findAccountByEmailParamsNative(email));
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("account", account);
        return "updateAccount";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") String id, @Valid Account account,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            account.setId(id);
            return "updateAccount";
        }

        accountRepository.save(account);
        model.addAttribute("users", accountRepository.findAll());
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id, Model model) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        accountRepository.delete(account);
        model.addAttribute("users", accountRepository.findAll());
        return "redirect:/home";
    }
}
