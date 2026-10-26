package br.com.fiap.controller;

import br.com.fiap.bo.UsuarioBo;
import br.com.fiap.dto.LoginRequest;
import br.com.fiap.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioBo usuarioBo = new UsuarioBo();

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(usuarioBo.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(usuarioBo.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws SQLException, ClassNotFoundException {
        Usuario criado = usuarioBo.cadastrar(usuario);
        return ResponseEntity.created(URI.create("/usuario/" + criado.getIdUsuario())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable int id, @RequestBody Usuario usuario)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(usuarioBo.atualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        usuarioBo.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest login) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(usuarioBo.login(login.email(), login.senha()));
    }
}