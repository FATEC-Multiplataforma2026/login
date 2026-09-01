package br.gov.sp.cps.controller.adapter;

import br.gov.sp.cps.controller.request.UserRequest;
import br.gov.sp.cps.entity.User;
import br.gov.sp.cps.entity.enumerable.UserRole;

import java.util.List;
import java.util.UUID;

public class UserControllerAdapter {
    private UserControllerAdapter() {
    }

    public static User cast(UserRequest request) {
        return new User(
                UUID.randomUUID().toString(),
                request.username(),
                request.password(),
                request.email(),
                request.cep(),
                List.of(UserRole.USER));
    }
}