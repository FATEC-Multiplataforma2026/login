package br.com.fatec.fretec.security;

import br.com.fatec.fretec.entity.Login;
import br.com.fatec.fretec.entity.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class TokenSecurity {
    private final JwtSecurity jwtSecurity;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public TokenSecurity(
            JwtSecurity jwtSecurity,
            UserDetailsService userDetailsService,
            AuthenticationManager authenticationManager) {
        this.jwtSecurity = jwtSecurity;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    public Token gerarToken(Login login) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                login.username(), login.password());
        authenticationManager.authenticate(authToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(login.username());
        return new Token(jwtSecurity.generateToken(userDetails));
    }

}
