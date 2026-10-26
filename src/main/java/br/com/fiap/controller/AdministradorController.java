package br.com.fiap.controller;

import br.com.fiap.bo.AdministradorBo;
import br.com.fiap.dto.LoginRequest;
import br.com.fiap.entities.Administrador;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {

    private final AdministradorBo administradorBo = new AdministradorBo();

    @GetMapping
    public ResponseEntity<List<Administrador>> listar() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(administradorBo.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> buscar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(administradorBo.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Administrador> cadastrar(@RequestBody Administrador administrador)
            throws SQLException, ClassNotFoundException {
        Administrador criado = administradorBo.cadastrar(administrador);
        return ResponseEntity.created(URI.create("/administrador/" + criado.getIdAdministrador())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrador> atualizar(@PathVariable int id, @RequestBody Administrador administrador)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(administradorBo.atualizar(id, administrador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        administradorBo.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Administrador> login(@RequestBody LoginRequest login)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(administradorBo.login(login.email(), login.senha()));
    }
}