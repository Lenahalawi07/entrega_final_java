package br.com.fiap.controller;

import br.com.fiap.bo.UsuarioMissaoBo;
import br.com.fiap.dto.ProgressoRequest;
import br.com.fiap.entities.UsuarioMissao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/usuario/{idUsuario}/missoes")
public class UsuarioMissaoController {

    private final UsuarioMissaoBo usuarioMissaoBo = new UsuarioMissaoBo();

    @GetMapping
    public ResponseEntity<List<UsuarioMissao>> listar(@PathVariable int idUsuario)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(usuarioMissaoBo.listarPorUsuario(idUsuario));
    }

    @GetMapping("/{idMissao}")
    public ResponseEntity<UsuarioMissao> buscar(@PathVariable int idUsuario, @PathVariable int idMissao)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(usuarioMissaoBo.buscar(idUsuario, idMissao));
    }

    @PostMapping("/{idMissao}")
    public ResponseEntity<UsuarioMissao> atribuir(@PathVariable int idUsuario, @PathVariable int idMissao)
            throws SQLException, ClassNotFoundException {
        UsuarioMissao criada = usuarioMissaoBo.atribuir(idUsuario, idMissao);
        return ResponseEntity.created(URI.create("/usuario/" + idUsuario + "/missoes/" + idMissao)).body(criada);
    }

    @PutMapping("/{idMissao}/progresso")
    public ResponseEntity<UsuarioMissao> atualizarProgresso(@PathVariable int idUsuario,
                                                            @PathVariable int idMissao,
                                                            @RequestBody ProgressoRequest request)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(usuarioMissaoBo.atualizarProgresso(idUsuario, idMissao, request.progresso()));
    }

    @PostMapping("/{idMissao}/concluir")
    public ResponseEntity<UsuarioMissao> concluir(@PathVariable int idUsuario, @PathVariable int idMissao)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(usuarioMissaoBo.concluir(idUsuario, idMissao));
    }

    @DeleteMapping("/{idMissao}")
    public ResponseEntity<Void> deletar(@PathVariable int idUsuario, @PathVariable int idMissao)
            throws SQLException, ClassNotFoundException {
        usuarioMissaoBo.deletar(idUsuario, idMissao);
        return ResponseEntity.noContent().build();
    }
}