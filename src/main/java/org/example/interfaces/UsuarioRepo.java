package org.example.interfaces;

import java.util.List;

public interface UsuarioRepo<T> {

    List<T> listar();

    T porId(int codigo);

    void guardar(T t);

    void eliminar(int id);

}
