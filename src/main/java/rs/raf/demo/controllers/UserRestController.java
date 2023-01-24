package rs.raf.demo.controllers;

import com.google.common.hash.Hashing;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.TokenRequestDto;
import rs.raf.demo.dto.TokenResponseDto;
import rs.raf.demo.dto.UserCreateDto;
import rs.raf.demo.dto.UserUpdateDto;
import rs.raf.demo.model.User;
import rs.raf.demo.services.UserServiceImpl;
import rs.raf.demo.utils.JwtUtil;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserRestController(UserServiceImpl userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // GET ALL:
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    // LOGIN:
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(tokenRequestDto.getEmail(), tokenRequestDto.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        TokenResponseDto responseDto = new TokenResponseDto(jwtUtil.generateToken(tokenRequestDto.getEmail()), userService.getUserPermissions(tokenRequestDto.getEmail()));
        return ResponseEntity.ok(responseDto);
    }


    // CREATE USER:
    @PostMapping(
            value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestHeader("Authorization") String token, @Valid @RequestBody UserCreateDto userCreateDto){

        User user = new User();
        String encodedPassword = passwordEncoder.encode(userCreateDto.getPassword());

        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setPassword(encodedPassword);
        user.setPermissions(userCreateDto.getPermissions());
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    // DELETE USER:
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET USER BY ID:
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // UPDATE USER:
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateDto userUpdateDto) {
        System.out.println(userUpdateDto.getPermissions());
        userService.updateUser(id, userUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
