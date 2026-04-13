package br.com.fatec.fretec.controller.adapter;

import br.com.fatec.fretec.controller.request.LoginRequest;
import br.com.fatec.fretec.entity.Login;

public class AuthControllerAdapter {
    private AuthControllerAdapter() {
    }

    public static Login cast(LoginRequest request) {
        return new Login(request.username(), request.password());
    }
}