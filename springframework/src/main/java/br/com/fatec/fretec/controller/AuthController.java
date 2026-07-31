package br.com.fatec.fretec.controller;

import br.com.fatec.fretec.controller.adapter.AuthControllerAdapter;
import br.com.fatec.fretec.controller.request.LoginRequest;
import br.com.fatec.fretec.controller.response.AuthClaimsResponse;
import br.com.fatec.fretec.controller.response.AuthResponse;
import br.com.fatec.fretec.entity.Token;
import br.com.fatec.fretec.entity.User;
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

    private final TokenSecurity tokenSecurity;
    private final JwtSecurity jwtSecurity;

    public AuthController(TokenSecurity tokenSecurity, JwtSecurity jwtSecurity) {
        this.tokenSecurity = tokenSecurity;
        this.jwtSecurity = jwtSecurity;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/auth")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Token token = tokenSecurity.gerarToken(AuthControllerAdapter.cast(request));
        return new AuthResponse(token.value());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/auth/cookie")
    public AuthClaimsResponse loginCookie(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthUserDetails userDetails = tokenSecurity.autenticar(AuthControllerAdapter.cast(request));
        Token token = tokenSecurity.gerarToken(userDetails);
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, token.value())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtSecurity.getExpirationSeconds())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        User user = userDetails.user();
        return new AuthClaimsResponse(
                user.id(),
                user.username(),
                user.roles().stream().map(Enum::name).toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/auth/logout")
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1/auth/forget/{username}")
    public String forgetPassword(@PathVariable("username") String username) {
        return "Olá " + username + " enviamos sua senha para o seu email";
    }
}