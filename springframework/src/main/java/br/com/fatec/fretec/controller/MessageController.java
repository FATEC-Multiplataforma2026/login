package br.com.fatec.fretec.controller;

import br.com.fatec.fretec.controller.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fatec/login")
public class MessageController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1/message")
    public MessageResponse getMessage() {
        return new MessageResponse("Olá, bem-vindo ao nosso sistema!");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1/message/admin")
    public MessageResponse getMessageAdmin() {
        return new MessageResponse("Olá, bem-vindo ao nosso sistema você é um admin!");
    }

}