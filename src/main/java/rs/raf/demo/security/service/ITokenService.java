package rs.raf.demo.security.service;

import io.jsonwebtoken.Claims;

public interface ITokenService {

    String generate(Claims claims);

    Claims parseToken(String jwt);
}