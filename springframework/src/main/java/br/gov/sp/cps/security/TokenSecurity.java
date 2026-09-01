package br.gov.sp.cps.security;

import br.gov.sp.cps.entity.Login;
import br.gov.sp.cps.entity.Token;
import br.gov.sp.cps.security.dto.AuthUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        return new Token(jwtSecurity.generateToken(autenticar(login)));
    }

    public AuthUserDetails autenticar(Login login) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        login.username(),
                        login.password());
        authenticationManager.authenticate(authToken);
        return (AuthUserDetails) userDetailsService
                .loadUserByUsername(login.username());
    }

    public Token gerarToken(AuthUserDetails userDetails) {
        return new Token(jwtSecurity.generateToken(userDetails));
    }
}