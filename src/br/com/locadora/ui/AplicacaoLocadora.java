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
                    System.out.println("Sistema encerrado, tchau!!!");
                }
                default -> System.out.println("Opcao invalida.");
            }

            System.out.println();
        }

        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("=== BEM-VINDOS A LOCADORA DE DVDS The Martins's ===");
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
        System.out.println("Cadastro de DVD");
        exibirOpcaoVoltar();

        Optional<String> titulo = lerTextoOuVoltar("Titulo: ");
        if (titulo.isEmpty()) {
            informarRetornoMenu();
            return;
        }

        Optional<String> genero = lerTextoOuVoltar("Genero: ");
        if (genero.isEmpty()) {
            informarRetornoMenu();
            return;
        }

        while (true) {
            Optional<Integer> anoLancamento = lerInteiroOuVoltar("Ano de lancamento: ");
            if (anoLancamento.isEmpty()) {
                informarRetornoMenu();
                return;
            }

            try {
                Dvd dvd = locadoraService.cadastrarDvd(titulo.get(), genero.get(), anoLancamento.get());
                System.out.println("DVD cadastrado. ID: " + dvd.id() + ". retornando ao menu inicial.");
                return;
            } catch (IllegalArgumentException e) {
                if ("Ano de lancamento invalido.".equals(e.getMessage())) {
                    System.out.println("Ano de lancamento invalido. Tente novamente");
                    continue;
                }
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    private void cadastrarCliente() {
        System.out.println("Cadastro de Cliente");
        exibirOpcaoVoltar();

        while (true) {
            Optional<String> nome = lerTextoOuVoltar("Nome: ");
            if (nome.isEmpty()) {
                informarRetornoMenu();
                return;
            }

            try {
                Cliente cliente = locadoraService.cadastrarCliente(nome.get());
                System.out.println("Cliente cadastrado. ID: " + cliente.id() + ". retornando ao menu inicial.");
                return;
            } catch (IllegalArgumentException e) {
                if ("Nome do cliente deve conter apenas letras e espacos.".equals(e.getMessage())) {
                    System.out.println("Nome do cliente deve conter apenas letras e espaços, tente novamente.");
                    continue;
                }
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    private void listarDvds() {
        List<Dvd> dvds = locadoraService.listarDvds();
        System.out.println("Lista de DVDs");

        if (dvds.isEmpty()) {
            System.out.println("Nenhum DVD cadastrado, retornando ao menu inicial.");
            return;
        }

        for (Dvd dvd : dvds) {
            exibirDvdComStatus(dvd);
        }

        aguardarVoltarAoMenu();
    }

    private void listarClientes() {
        List<Cliente> clientes = locadoraService.listarClientes();
        System.out.println("Lista de Clientes");

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado, retornando ao menu inicial.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println("ID: " + cliente.id() + " | Nome: " + cliente.nome());
        }

        aguardarVoltarAoMenu();
    }

    private void alugarDvd() {
        exibirOpcaoVoltar();

        while (true) {
            Optional<Integer> idDvd = lerInteiroOuVoltar("ID do DVD: ");
            if (idDvd.isEmpty()) {
                informarRetornoMenu();
                return;
            }

            Optional<Integer> idCliente = lerInteiroOuVoltar("ID do cliente: ");
            if (idCliente.isEmpty()) {
                informarRetornoMenu();
                return;
            }

            ResultadoOperacao resultado = locadoraService.alugarDvd(idDvd.get(), idCliente.get());
            if (resultado.sucesso()) {
                System.out.println(resultado.mensagem());
                return;
            }

            System.out.println(resultado.mensagem() + " Tente novamente ou digite 0 para voltar ao menu inicial.");
        }
    }

    private void devolverDvd() {
        exibirOpcaoVoltar();

        while (true) {
            Optional<Integer> idDvd = lerInteiroOuVoltar("ID do DVD para devolucao: ");
            if (idDvd.isEmpty()) {
                informarRetornoMenu();
                return;
            }

            ResultadoOperacao resultado = locadoraService.devolverDvd(idDvd.get());
            if (resultado.sucesso()) {
                System.out.println(resultado.mensagem());
                return;
            }

            System.out.println(resultado.mensagem() + " Tente novamente ou digite 0 para voltar ao menu inicial.");
        }
    }

    private void buscarDvdPorTitulo() {
        exibirOpcaoVoltar();

        Optional<String> trechoTitulo = lerTextoOuVoltar("Digite o titulo ou parte do titulo: ");
        if (trechoTitulo.isEmpty()) {
            informarRetornoMenu();
            return;
        }

        List<Dvd> encontrados = locadoraService.buscarDvdsPorTitulo(trechoTitulo.get());
        System.out.println("Resultado da busca");

        if (encontrados.isEmpty()) {
            System.out.println("Nenhum DVD encontrado com esse criterio, retornando ao menu inicial.");
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

    private void exibirOpcaoVoltar() {
        System.out.println("Digite 0 para voltar ao menu inicial a qualquer momento.");
    }

    private void informarRetornoMenu() {
        System.out.println("Retornando ao menu inicial.");
    }

    private void aguardarVoltarAoMenu() {
        exibirOpcaoVoltar();
        while (true) {
            String valor = lerTexto("Escolha: ");
            if ("0".equals(valor)) {
                informarRetornoMenu();
                return;
            }
            System.out.println("Opcao invalida. Digite 0 para voltar ao menu inicial.");
        }
    }

    private Optional<Integer> lerInteiroOuVoltar(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            if ("0".equals(valor)) {
                return Optional.empty();
            }

            try {
                return Optional.of(Integer.parseInt(valor));
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }

    private Optional<String> lerTextoOuVoltar(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            if ("0".equals(valor)) {
                return Optional.empty();
            }

            if (!valor.isEmpty()) {
                return Optional.of(valor);
            }

            System.out.println("Campo obrigatorio. Tente novamente.");
        }
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
