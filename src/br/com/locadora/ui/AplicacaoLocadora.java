package br.com.locadora.ui;

import br.com.locadora.model.Cliente;
import br.com.locadora.model.Dvd;
import br.com.locadora.model.Locacao;
import br.com.locadora.service.LocadoraService;
import br.com.locadora.service.ResultadoOperacao;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AplicacaoLocadora {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final LocadoraService locadoraService;
    private final Scanner scanner;

    public AplicacaoLocadora() {
        this(new LocadoraService(), new Scanner(System.in));
    }

    AplicacaoLocadora(LocadoraService locadoraService, Scanner scanner) {
        this.locadoraService = locadoraService;
        this.scanner = scanner;
    }

    public void executar() {
        boolean executando = true;

        while (executando) {
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opcao: ");

            switch (opcao) {
                case 1 -> cadastrarDvd();
                case 2 -> cadastrarCliente();
                case 3 -> listarDvds();
                case 4 -> listarClientes();
                case 5 -> alugarDvd();
                case 6 -> devolverDvd();
                case 7 -> buscarDvdPorTitulo();
                case 0 -> {
                    executando = false;
                    System.out.println("Sistema encerrado.");
                }
                default -> System.out.println("Opcao invalida.");
            }

            System.out.println();
        }

        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("=== LOCADORA DE DVDS ===");
        System.out.println("1 - Cadastrar DVD");
        System.out.println("2 - Cadastrar Cliente");
        System.out.println("3 - Listar DVDs");
        System.out.println("4 - Listar Clientes");
        System.out.println("5 - Alugar DVD");
        System.out.println("6 - Devolver DVD");
        System.out.println("7 - Buscar DVD por titulo");
        System.out.println("0 - Sair");
    }

    private void cadastrarDvd() {
        try {
            System.out.println("Cadastro de DVD");
            String titulo = lerTexto("Titulo: ");
            String genero = lerTexto("Genero: ");
            int anoLancamento = lerInteiro("Ano de lancamento: ");

            Dvd dvd = locadoraService.cadastrarDvd(titulo, genero, anoLancamento);
            System.out.println("DVD cadastrado. ID: " + dvd.id());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cadastrarCliente() {
        try {
            System.out.println("Cadastro de Cliente");
            String nome = lerTexto("Nome: ");
            Cliente cliente = locadoraService.cadastrarCliente(nome);
            System.out.println("Cliente cadastrado. ID: " + cliente.id());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarDvds() {
        List<Dvd> dvds = locadoraService.listarDvds();
        System.out.println("Lista de DVDs");

        if (dvds.isEmpty()) {
            System.out.println("Nenhum DVD cadastrado.");
            return;
        }

        for (Dvd dvd : dvds) {
            exibirDvdComStatus(dvd);
        }
    }

    private void listarClientes() {
        List<Cliente> clientes = locadoraService.listarClientes();
        System.out.println("Lista de Clientes");

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println("ID: " + cliente.id() + " | Nome: " + cliente.nome());
        }
    }

    private void alugarDvd() {
        int idDvd = lerInteiro("ID do DVD: ");
        int idCliente = lerInteiro("ID do cliente: ");

        ResultadoOperacao resultado = locadoraService.alugarDvd(idDvd, idCliente);
        System.out.println(resultado.mensagem());
    }

    private void devolverDvd() {
        int idDvd = lerInteiro("ID do DVD para devolucao: ");
        ResultadoOperacao resultado = locadoraService.devolverDvd(idDvd);
        System.out.println(resultado.mensagem());
    }

    private void buscarDvdPorTitulo() {
        String trechoTitulo = lerTexto("Digite o titulo ou parte do titulo: ");
        List<Dvd> encontrados = locadoraService.buscarDvdsPorTitulo(trechoTitulo);
        System.out.println("Resultado da busca");

        if (encontrados.isEmpty()) {
            System.out.println("Nenhum DVD encontrado com esse criterio.");
            return;
        }

        for (Dvd dvd : encontrados) {
            exibirDvdComStatus(dvd);
        }
    }

    private void exibirDvdComStatus(Dvd dvd) {
        Optional<Locacao> locacao = locadoraService.buscarLocacaoAtivaPorDvd(dvd.id());
        String status = locacao
                .map(value -> "Alugado para cliente ID " + value.idCliente() + " em " + value.dataLocacao().format(FORMATO_DATA))
                .orElse("Disponivel");
        System.out.println("ID: " + dvd.id() + " | Titulo: " + dvd.titulo() + " | Genero: " + dvd.genero()
                + " | Ano: " + dvd.anoLancamento() + " | Status: " + status);
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }

    private String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            if (!valor.isEmpty()) {
                return valor;
            }

            System.out.println("Campo obrigatorio. Tente novamente.");
        }
    }
}
