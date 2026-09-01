package br.gov.sp.cps.controller.adapter;

import br.gov.sp.cps.controller.request.LoginRequest;
import br.gov.sp.cps.entity.Login;

public class AuthControllerAdapter {
    private AuthControllerAdapter() {
    }

    public static Login cast(LoginRequest request) {
        return new Login(request.username(), request.password());
    }
}