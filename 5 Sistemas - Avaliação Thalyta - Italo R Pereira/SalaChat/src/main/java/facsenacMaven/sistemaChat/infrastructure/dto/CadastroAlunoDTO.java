package facsenacMaven.sistemaChat.infrastructure.dto;

import facsenacMaven.sistemaChat.domain.entity.Aluno;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data

public class CadastroAlunoDTO {

    @NotNull(message = "Name cannot be empty!")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters!")
    private final String name;

    @NotNull(message = "Email cannot be empty!")
    @Email(message = "Invalid email address!")
    private final String email;

    @NotNull(message = "Please inform a valid password")
    private final String secret;


    public static CadastroAlunoDTO createAluno(Aluno createAluno){

        return new CadastroAlunoDTO(
                createAluno.getName(),
                createAluno.getEmail(),
                createAluno.getSecret()
        );
    }

}
