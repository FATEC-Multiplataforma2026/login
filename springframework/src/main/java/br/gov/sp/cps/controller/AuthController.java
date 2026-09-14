package br.gov.sp.cps.controller;

import br.gov.sp.cps.controller.adapter.AuthControllerAdapter;
import br.gov.sp.cps.controller.adapter.UserControllerAdapter;
import br.gov.sp.cps.controller.request.LoginRequest;
import br.gov.sp.cps.controller.request.UserRequest;
import br.gov.sp.cps.controller.response.AuthClaimsResponse;
import br.gov.sp.cps.entity.Login;
import br.gov.sp.cps.entity.Token;
import br.gov.sp.cps.entity.User;
import br.gov.sp.cps.repository.UserRepository;
import br.gov.sp.cps.security.JwtSecurity;
import br.gov.sp.cps.security.TokenSecurity;
import br.gov.sp.cps.security.dto.AuthUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fatec/login")
public class AuthController {
    private static final String ACCESS_TOKEN_COOKIE = "access_token";

    private final String cookieSite;
    private final Boolean cookieSecure;
    private final JwtSecurity jwtSecurity;
    private final UserRepository repository;
    private final TokenSecurity tokenSecurity;

    public AuthController(
            JwtSecurity jwtSecurity,
            UserRepository repository,
            TokenSecurity tokenSecurity,
            @Value("${app.cookie.site}") String cookieSite,
            @Value("${app.cookie.secure}") Boolean cookieSecure) {
        this.cookieSite = cookieSite;
        this.repository = repository;
        this.jwtSecurity = jwtSecurity;
        this.cookieSecure = cookieSecure;
        this.tokenSecurity = tokenSecurity;
    }

    private ResponseCookie buildCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSite)
                .path("/")
                .maxAge(maxAgeSeconds)
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/create")
    public AuthClaimsResponse create(@RequestBody UserRequest request, HttpServletResponse response) {
        User user = repository.save(UserControllerAdapter.cast(request));
        AuthUserDetails userDetails = tokenSecurity.autenticar(new Login(user.username(), request.password()));
        Token token = tokenSecurity.gerarToken(userDetails);

        ResponseCookie cookie = buildCookie(token.value(), jwtSecurity.getExpirationSeconds());
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

        ResponseCookie cookie = buildCookie(token.value(), jwtSecurity.getExpirationSeconds());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new AuthClaimsResponse(
                user.id(),
                user.username(),
                user.roles().stream().map(Enum::name).toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/logout")
    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = buildCookie("", 0);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}