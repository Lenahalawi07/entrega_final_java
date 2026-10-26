package br.com.fiap.controller;

import br.com.fiap.bo.AvatarBo;
import br.com.fiap.entities.Avatar;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarBo avatarBo = new AvatarBo();

    @GetMapping
    public ResponseEntity<List<Avatar>> listar() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(avatarBo.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avatar> buscar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(avatarBo.buscarPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Avatar> buscarPorUsuario(@PathVariable int idUsuario)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(avatarBo.buscarPorUsuarioId(idUsuario));
    }

    @PostMapping
    public ResponseEntity<Avatar> cadastrar(@RequestBody Avatar avatar) throws SQLException, ClassNotFoundException {
        Avatar criado = avatarBo.cadastrar(avatar);
        return ResponseEntity.created(URI.create("/avatar/" + criado.getIdAvatar())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avatar> atualizar(@PathVariable int id, @RequestBody Avatar avatar)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(avatarBo.atualizar(id, avatar));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        avatarBo.deletar(id);
        return ResponseEntity.noContent().build();
    }
}