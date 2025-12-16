package facsenacMaven.sistemaChat.domain.domainExceptions;

import facsenacMaven.sistemaChat.infrastructure.exceptions.RequestException;


public class AlunoNotFound extends RequestException {
    public AlunoNotFound(String alunoId){
        super("Aluno not found!" , "Aluno não encontrado: " + alunoId);

    }

}
