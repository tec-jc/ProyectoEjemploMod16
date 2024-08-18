package org.esfe.servicios.interfaces;

import org.esfe.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioService {
    Page<Usuario> obtenerTodosPaginados(Pageable pageable);

    List<Usuario> obtenerTodos();

    Usuario obtenerPorId(Integer id);

    Usuario crearOEditar(Usuario usuario);

    void eliminarPorId(Integer id);
}
