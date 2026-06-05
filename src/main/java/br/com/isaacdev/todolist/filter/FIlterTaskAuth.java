package br.com.isaacdev.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.isaacdev.todolist.user.UserModel;
import br.com.isaacdev.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FIlterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        var servletPath = request.getServletPath();
        if (!servletPath.startsWith("/tasks")) {
            filterChain.doFilter(request, response);
            return;
        }

        var authorization = request.getHeader("Authorization");
        var authEncoded = authorization.substring("Basic".length()).trim();

        byte[] authDecode = Base64.getDecoder().decode(authEncoded);
        var authString = new String(authDecode);

        String credentials[] = authString.split(":", 2);
        String username = credentials[0];
        String password = credentials[1];

        UserModel user = this.userRepository.findByUsername(username);
        if (user == null) {
            response.sendError(401, "E-mail ou senha incorretos");
            return;
        }

        boolean passwordVerified = BCrypt.verifyer()
            .verify(password.toCharArray(), user.getPassword())
            .verified;

        if (!passwordVerified) {
            response.sendError(401, "E-mail ou senha incorretos");
            return;
        }

        request.setAttribute("userId", user.getId());
        filterChain.doFilter(request, response);
    }
}
