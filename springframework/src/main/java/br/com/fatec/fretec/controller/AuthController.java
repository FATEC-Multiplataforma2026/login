package br.com.fatec.fretec.controller;

import br.com.fatec.fretec.controller.adapter.AuthControllerAdapter;
import br.com.fatec.fretec.controller.adapter.UserControllerAdapter;
import br.com.fatec.fretec.controller.request.LoginRequest;
import br.com.fatec.fretec.controller.request.UserRequest;
import br.com.fatec.fretec.controller.response.AuthClaimsResponse;
import br.com.fatec.fretec.entity.Login;
import br.com.fatec.fretec.entity.Token;
import br.com.fatec.fretec.entity.User;
import br.com.fatec.fretec.repository.UserRepository;
import br.com.fatec.fretec.security.JwtSecurity;
import br.com.fatec.fretec.security.TokenSecurity;
import br.com.fatec.fretec.security.dto.AuthUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fatec/login")
public class AuthController {
    private static final String ACCESS_TOKEN_COOKIE = "access_token";

    private final UserRepository repository;
    private final JwtSecurity jwtSecurity;
    private final TokenSecurity tokenSecurity;

    public AuthController(
            UserRepository repository,
            JwtSecurity jwtSecurity,
            TokenSecurity tokenSecurity) {
        this.repository = repository;
        this.jwtSecurity = jwtSecurity;
        this.tokenSecurity = tokenSecurity;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/create")
    public AuthClaimsResponse create(@RequestBody UserRequest request, HttpServletResponse response) {
        User user = repository.save(UserControllerAdapter.cast(request));
        AuthUserDetails userDetails = tokenSecurity.autenticar(new Login(user.username(), request.password()));
        Token token = tokenSecurity.gerarToken(userDetails);

        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, token.value())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtSecurity.getExpirationSeconds())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new AuthClaimsResponse(
                user.id(),
                user.username(),
                user.roles().stream().map(Enum::name).toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/auth")
    public AuthClaimsResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthUserDetails userDetails = tokenSecurity.autenticar(AuthControllerAdapter.cast(request));
        Token token = tokenSecurity.gerarToken(userDetails);
        User user = userDetails.user();
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, token.value())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtSecurity.getExpirationSeconds())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new AuthClaimsResponse(
                user.id(),
                user.username(),
                user.roles().stream().map(Enum::name).toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/logout")
    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}