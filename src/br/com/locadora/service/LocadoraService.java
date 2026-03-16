package br.com.locadora.service;

import br.com.locadora.model.Cliente;
import br.com.locadora.model.Dvd;
import br.com.locadora.model.Locacao;
import br.com.locadora.repository.Repositorio;
import br.com.locadora.repository.RepositorioEmMemoria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class LocadoraService {
    private static final String REGEX_NOME_CLIENTE = "^[\\p{L}]+(?:\\s+[\\p{L}]+)*$";
    private final Repositorio<Cliente> clienteRepositorio;
    private final Repositorio<Dvd> dvdRepositorio;
    private final Map<Integer, Locacao> locacoesAtivas;
    private int proximoIdCliente = 1;
    private int proximoIdDvd = 1;

    public LocadoraService() {
        this(new RepositorioEmMemoria<>(), new RepositorioEmMemoria<>());
    }

    public LocadoraService(Repositorio<Cliente> clienteRepositorio, Repositorio<Dvd> dvdRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.dvdRepositorio = dvdRepositorio;
        this.locacoesAtivas = new LinkedHashMap<>();
    }

    public Cliente cadastrarCliente(String nome) {
        String nomeValidado = validarNomeCliente(nome);
        Cliente cliente = new Cliente(proximoIdCliente++, nomeValidado);
        clienteRepositorio.salvar(cliente.id(), cliente);
        return cliente;
    }

    public Dvd cadastrarDvd(String titulo, String genero, int anoLancamento) {
        String tituloValidado = validarTexto(titulo, "Titulo");
        String generoValidado = validarTexto(genero, "Genero");
        validarAno(anoLancamento);

        Dvd dvd = new Dvd(proximoIdDvd++, tituloValidado, generoValidado, anoLancamento);
        dvdRepositorio.salvar(dvd.id(), dvd);
        return dvd;
    }

    public List<Cliente> listarClientes() {
        return clienteRepositorio.listarTodos();
    }

    public List<Dvd> listarDvds() {
        return dvdRepositorio.listarTodos();
    }

    public List<Dvd> buscarDvdsPorTitulo(String trechoTitulo) {
        String termoBusca = validarTexto(trechoTitulo, "Trecho do titulo").toLowerCase(Locale.ROOT);
        List<Dvd> encontrados = new ArrayList<>();

        for (Dvd dvd : dvdRepositorio.listarTodos()) {
            if (dvd.titulo().toLowerCase(Locale.ROOT).contains(termoBusca)) {
                encontrados.add(dvd);
            }
        }

        return encontrados;
    }

    public Optional<Locacao> buscarLocacaoAtivaPorDvd(int idDvd) {
        return Optional.ofNullable(locacoesAtivas.get(idDvd));
    }

    public ResultadoOperacao alugarDvd(int idDvd, int idCliente) {
        if (!dvdRepositorio.existePorId(idDvd)) {
            return new ResultadoOperacao(false, "DVD nao encontrado.");
        }

        if (!clienteRepositorio.existePorId(idCliente)) {
            return new ResultadoOperacao(false, "Cliente nao encontrado.");
        }

        if (locacoesAtivas.containsKey(idDvd)) {
            return new ResultadoOperacao(false, "DVD ja esta alugado.");
        }

        Locacao locacao = new Locacao(idDvd, idCliente, LocalDate.now());
        locacoesAtivas.put(idDvd, locacao);
        return new ResultadoOperacao(true, "Locacao realizada com sucesso, retornando ao menu inicial.");
    }

    public ResultadoOperacao devolverDvd(int idDvd) {
        if (!dvdRepositorio.existePorId(idDvd)) {
            return new ResultadoOperacao(false, "DVD nao encontrado, retornando ao menu inicial.");
        }

        Locacao removida = locacoesAtivas.remove(idDvd);
        if (removida == null) {
            return new ResultadoOperacao(false, "Este DVD ja esta disponivel, retornando ao menu inicial.");
        }

        return new ResultadoOperacao(true, "DVD devolvido com sucesso.");
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " obrigatorio.");
        }
        return valor.trim();
    }

    private static String validarNomeCliente(String nome) {
        String nomeValidado = validarTexto(nome, "Nome do cliente").replaceAll("\\s+", " ");
        if (!nomeValidado.matches(REGEX_NOME_CLIENTE)) {
            throw new IllegalArgumentException("Nome do cliente deve conter apenas letras e espacos.");
        }
        return nomeValidado;
    }

    private static void validarAno(int anoLancamento) {
        int anoAtual = LocalDate.now().getYear();
        if (anoLancamento < 1900 || anoLancamento > anoAtual + 1) {
            throw new IllegalArgumentException("Ano de lancamento invalido.");
        }
    }
}
