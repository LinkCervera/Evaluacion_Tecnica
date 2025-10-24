package com.castores.inventario.utils;

import com.castores.inventario.model.Usuario;
import com.castores.inventario.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    private final UsuarioService usuarioService;

    public AuthUtils(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();
        return usuarioService.buscarPorUsername(username).orElse(null);
    }
}
