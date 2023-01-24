package rs.raf.demo.dto;

import rs.raf.demo.model.Permission;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class UserCreateDto {

    @NotNull
    @NotBlank(message = "First name is mandatory.")
    private String firstName;
    @NotNull
    @NotBlank(message = "Last name is mandatory.")
    private String lastName;
    @NotNull
    @NotBlank(message = "Email is mandatory.")
    private String email;
    @NotNull
    @NotBlank(message = "Password is mandatory.")
    private String password;
    private Set<Permission> permissions;

    public UserCreateDto(String firstName, String lastName, String email, String password, Set<Permission> permissions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.permissions = permissions;
    }

    public UserCreateDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "UserCreateDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
