package rs.raf.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.User;
import rs.raf.demo.services.ErrorMessageServiceImpl;
import rs.raf.demo.services.UserServiceImpl;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/errors")
public class ErrorMessageController {

    private final ErrorMessageServiceImpl errorMessageService;
    private final UserServiceImpl userService;

    public ErrorMessageController(ErrorMessageServiceImpl errorMessageService, UserServiceImpl userService) {
        this.errorMessageService = errorMessageService;
        this.userService = userService;
    }

    @GetMapping(value = "/get")
    public List<ErrorMessage> getAllMachines(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).get();

        return errorMessageService.findAll(user.getId());
    }

}
