package br.com.locadora.model;

import java.time.LocalDate;

public record Locacao(int idDvd, int idCliente, LocalDate dataLocacao) {
}
