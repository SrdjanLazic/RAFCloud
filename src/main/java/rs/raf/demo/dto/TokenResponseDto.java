package rs.raf.demo.dto;

import rs.raf.demo.model.Permission;

import java.util.List;

public class TokenResponseDto {

    private String token;
    private List<Permission> permissions;

    public TokenResponseDto() {
    }

    public TokenResponseDto(String token, List<Permission> permissions) {
        this.token = token;
        this.permissions = permissions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "TokenResponseDto{" +
                "token='" + token + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
