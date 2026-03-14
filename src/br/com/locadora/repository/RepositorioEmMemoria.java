package br.com.locadora.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioEmMemoria<T> implements Repositorio<T> {
    private final Map<Integer, T> dados = new LinkedHashMap<>();

    @Override
    public void salvar(int id, T entidade) {
        dados.put(id, entidade);
    }

    @Override
    public Optional<T> buscarPorId(int id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public boolean existePorId(int id) {
        return dados.containsKey(id);
    }

    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(dados.values());
    }
}
