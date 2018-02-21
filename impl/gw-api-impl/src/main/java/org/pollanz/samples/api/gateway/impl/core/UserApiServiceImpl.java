package org.pollanz.samples.api.gateway.impl.core;

import com.codahale.metrics.annotation.Timed;
import org.pollanz.samples.api.gateway.core.UserApi;
import org.pollanz.samples.api.gateway.core.pojo.JWTToken;
import org.pollanz.samples.api.gateway.core.pojo.LoginVM;
import org.pollanz.samples.api.gateway.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.core.Response;

public class UserApiServiceImpl implements UserApi {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Timed
    @Override
    public Response authenticate(LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        return Response.ok(new JWTToken(jwt)).build();
    }

}
