package facsenacMaven.sistemaChat.infrastructure.controller;

import facsenacMaven.sistemaChat.domain.ApplicationService.AlunoService;
import facsenacMaven.sistemaChat.domain.ApplicationService.SessionService;
import facsenacMaven.sistemaChat.domain.entity.Aluno;
import facsenacMaven.sistemaChat.domain.entity.Message;
import facsenacMaven.sistemaChat.infrastructure.dto.CadastroAlunoDTO;
import facsenacMaven.sistemaChat.infrastructure.dto.LoginDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<Aluno> createAluno(@RequestBody @Valid CadastroAlunoDTO dto) {
        Aluno created = alunoService.createAluno(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getAluno(@PathVariable String id) {
        Aluno aluno = alunoService.loadAluno(id);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/login")
    public ResponseEntity<Aluno> login(@RequestBody @Valid LoginDTO dto) {
        Aluno aluno = alunoService.login(dto);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        alunoService.logoutByEmail(email);
        return ResponseEntity.ok("Logout realizado para " + email);
    }

    @GetMapping("/session")
    public ResponseEntity<Boolean> isLogged(@RequestParam("email") String email) {
        boolean logged = sessionService.isLogged(email);
        return ResponseEntity.ok(logged);
    }

    @PostMapping("/session/register/{email}")
    public ResponseEntity<Boolean> registerSession(@PathVariable String email) {
        boolean registered = sessionService.register(email);
        return ResponseEntity.ok(registered);
    }

    @GetMapping("/session/logged")
    public ResponseEntity<List<String>> getLoggedUsers() {
        return ResponseEntity.ok(alunoService.listarUsuariosLogados());
    }

    @PostMapping("/session/unregister/{email}")
    public ResponseEntity<Boolean> unregisterSession(@PathVariable String email) {
        boolean removed = sessionService.unregister(email);
        return ResponseEntity.ok(removed);
    }

    @PostMapping("/messages/send")
    public ResponseEntity<Message> enviarMensagem(@RequestBody Map<String, String> body) {
        String senderEmail = body.get("senderEmail");
        String recipientEmail = body.get("recipientEmail");
        String content = body.get("content");
        Message message = alunoService.enviarMensagem(senderEmail, recipientEmail, content);
        return ResponseEntity.ok(message);
    }
}