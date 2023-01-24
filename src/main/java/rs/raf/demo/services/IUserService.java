package rs.raf.demo.services;

import org.springframework.http.ResponseEntity;
import rs.raf.demo.dto.TokenRequestDto;
import rs.raf.demo.dto.TokenResponseDto;
import rs.raf.demo.dto.UserUpdateDto;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService<T, ID> {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

//    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    void deleteById(Long id);

    List<Permission> getUserPermissions(String email);

    User updateUser(Long id, UserUpdateDto userUpdateDto);

    Optional<User> findByEmail(String email);

}
