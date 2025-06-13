package br.com.quintinno.securityapi.transfer;

import java.time.LocalDate;

public record SignupRequestTransfer(
    String nome, 
    String identificador,
    String senha,
    String telefone, 
    LocalDate dataNascimento
) { }
