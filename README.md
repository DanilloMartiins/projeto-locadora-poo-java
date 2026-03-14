# Locadora de DVDs - Java

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-brightgreen)
![Linguagem](https://img.shields.io/badge/linguagem-Java-orange)
![Execucao](https://img.shields.io/badge/execucao-Console-blue)
![GitHub Repo stars](https://img.shields.io/github/stars/DanilloMartiins/projeto-locadora-poo-java?style=social)

Repositorio com um sistema de locadora de DVDs em Java, desenvolvido para praticar programacao orientada a objetos, organizacao em camadas e versionamento com Git/GitHub.

## Sobre o Projeto

Este projeto implementa um sistema de locadora em modo console com foco em:

- Arquitetura em camadas (UI, Service, Repository, Model)
- Uso de `record` para entidades de dominio
- Uso de `interface` para desacoplamento de repositorio
- Validacoes de entrada (ex.: nome de cliente sem numeros/caracteres especiais)
- Operacoes de cadastro, locacao, devolucao e busca por titulo

## Estrutura do Repositorio

```txt
.
|-- README.md
|-- .gitignore
|-- Inicial Java.iml
`-- src
    |-- Main.java
    |-- br/com/DIO/br/com/DIO/JJClass.java
    `-- br/com/locadora
        |-- model
        |   |-- Cliente.java
        |   |-- Dvd.java
        |   `-- Locacao.java
        |-- repository
        |   |-- Repositorio.java
        |   `-- RepositorioEmMemoria.java
        |-- service
        |   |-- LocadoraService.java
        |   `-- ResultadoOperacao.java
        `-- ui
            `-- AplicacaoLocadora.java
```

## Funcionalidades

1. Cadastro de DVDs
2. Cadastro de clientes
3. Listagem de DVDs (com status de disponibilidade)
4. Listagem de clientes
5. Locacao de DVD
6. Devolucao de DVD
7. Busca de DVD por titulo (parcial e sem diferenciar maiusculas/minusculas)

## Tecnologias Utilizadas

- Java (JDK 17+)
- Programacao orientada a objetos
- Git
- GitHub

## Como Executar

1. Clone o repositorio:

```bash
git clone https://github.com/DanilloMartiins/projeto-locadora-poo-java.git
```

2. Acesse a pasta do projeto:

```bash
cd projeto-locadora-poo-java
```

3. Compile os arquivos Java:

```powershell
$files = Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 $files
```

4. Execute a aplicacao:

```bash
java -cp src Main
```

## Aprendizados

Durante o desenvolvimento deste projeto, pratiquei:

- Modelagem de dominio com `record`
- Separacao de responsabilidades por camada
- Regras de negocio centralizadas em service
- Validacao de dados de entrada
- Fluxo de versionamento com `add`, `commit` e `push`

## Autor

Danillo Martins  
GitHub: [@DanilloMartiins](https://github.com/DanilloMartiins)
