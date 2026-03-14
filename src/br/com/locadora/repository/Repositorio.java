package br.com.locadora.repository;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T> {
    void salvar(int id, T entidade);

    Optional<T> buscarPorId(int id);

    boolean existePorId(int id);

    List<T> listarTodos();
}
