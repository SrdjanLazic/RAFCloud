package rs.raf.demo.services;

import com.google.common.hash.Hashing;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.raf.demo.dto.TokenRequestDto;
import rs.raf.demo.dto.TokenResponseDto;
import rs.raf.demo.dto.UserUpdateDto;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.security.service.TokenServiceImpl;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService<User, Long> {

    private final UserRepository userRepository;
    private final TokenServiceImpl tokenService;

    public UserServiceImpl(UserRepository userRepository, TokenServiceImpl tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

//    @Override
//    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
//
//        String encodedhash = Hashing.sha256()
//                .hashString(tokenRequestDto.getPassword(), StandardCharsets.UTF_8)
//                .toString();
//
//        Optional<User> user = userRepository
//                .findUserByEmailAndPassword(tokenRequestDto.getEmail(), encodedhash);
//
//        if(!user.isPresent()){
//            return null;
//        } else {
//            //Create token payload
//            Claims claims = Jwts.claims();
//            claims.put("id", user.get().getId());
//            claims.put("permissions", user.get().getPermissions());
//
//            //Generate token
//            String token = tokenService.generate(claims);
//            return new TokenResponseDto(token);
//        }
//
//    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Permission> getUserPermissions(String email) {
        List<Permission> permissions = new ArrayList<>(userRepository.findUserByEmail(email).get().getPermissions());
        return permissions;
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ExpressionException(String.format("User with id: %d not found.", id)));
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setPermissions(userUpdateDto.getPermissions());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
