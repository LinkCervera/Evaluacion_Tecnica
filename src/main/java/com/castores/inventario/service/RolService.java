package com.castores.inventario.service;

import com.castores.inventario.model.Rol;
import com.castores.inventario.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Optional<Rol> buscarPorNombre(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol);
    }

    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }
}
