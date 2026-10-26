package br.com.fiap.controller;

import br.com.fiap.bo.AcessorioBo;
import br.com.fiap.entities.Acessorio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/acessorio")
public class AcessorioController {

    private final AcessorioBo acessorioBo = new AcessorioBo();

    @GetMapping
    public ResponseEntity<List<Acessorio>> listar() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(acessorioBo.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Acessorio> buscar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(acessorioBo.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Acessorio> cadastrar(@RequestBody Acessorio acessorio)
            throws SQLException, ClassNotFoundException {
        Acessorio criado = acessorioBo.cadastrar(acessorio);
        return ResponseEntity.created(URI.create("/acessorio/" + criado.getIdAcessorio())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Acessorio> atualizar(@PathVariable int id, @RequestBody Acessorio acessorio)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(acessorioBo.atualizar(id, acessorio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        acessorioBo.deletar(id);
        return ResponseEntity.noContent().build();
    }
}