package br.com.fiap.controller;

import br.com.fiap.bo.MissaoBo;
import br.com.fiap.entities.Missao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/missao")
public class MissaoController {

    private final MissaoBo missaoBo = new MissaoBo();

    @GetMapping
    public ResponseEntity<List<Missao>> listar() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(missaoBo.listar());
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<Missao>> listarAtivas() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(missaoBo.listarAtivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Missao> buscar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(missaoBo.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Missao> cadastrar(@RequestBody Missao missao) throws SQLException, ClassNotFoundException {
        Missao criada = missaoBo.cadastrar(missao);
        return ResponseEntity.created(URI.create("/missao/" + criada.getIdMissao())).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Missao> atualizar(@PathVariable int id, @RequestBody Missao missao)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(missaoBo.atualizar(id, missao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) throws SQLException, ClassNotFoundException {
        missaoBo.deletar(id);
        return ResponseEntity.noContent().build();
    }
}