package br.com.fatec.fretec.controller;

import br.com.fatec.fretec.controller.adapter.UserControllerAdapter;
import br.com.fatec.fretec.controller.request.UserRequest;
import br.com.fatec.fretec.controller.response.AuthResponse;
import br.com.fatec.fretec.entity.Token;
import br.com.fatec.fretec.security.TokenSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fatec/login")
public class AuthController {
    private final TokenSecurity tokenSecurity;

    public AuthController(TokenSecurity tokenSecurity) {
        this.tokenSecurity = tokenSecurity;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/auth")
    public AuthResponse login(@RequestBody UserRequest request) {
        Token token = tokenSecurity.gerarToken(UserControllerAdapter.cast(request));
        return new AuthResponse(token.value());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1/auth/forget/{username}")
    public String forgetPassword(@PathVariable("username") String username) {
        return "Olá " + username + " enviamos sua senha para o seu email";
    }
}