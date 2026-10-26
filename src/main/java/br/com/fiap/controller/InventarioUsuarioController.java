package br.com.fiap.controller;

import br.com.fiap.bo.InventarioUsuarioBo;
import br.com.fiap.entities.InventarioUsuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/usuario/{idUsuario}/inventario")
public class InventarioUsuarioController {

    private final InventarioUsuarioBo inventarioBo = new InventarioUsuarioBo();

    @GetMapping
    public ResponseEntity<List<InventarioUsuario>> listar(@PathVariable int idUsuario)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(inventarioBo.listarPorUsuario(idUsuario));
    }

    @GetMapping("/{idAcessorio}")
    public ResponseEntity<InventarioUsuario> buscar(@PathVariable int idUsuario, @PathVariable int idAcessorio)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(inventarioBo.buscar(idUsuario, idAcessorio));
    }

    @PostMapping("/{idAcessorio}")
    public ResponseEntity<InventarioUsuario> comprar(@PathVariable int idUsuario, @PathVariable int idAcessorio)
            throws SQLException, ClassNotFoundException {
        InventarioUsuario item = inventarioBo.comprar(idUsuario, idAcessorio);
        return ResponseEntity.created(URI.create("/usuario/" + idUsuario + "/inventario/" + idAcessorio)).body(item);
    }

    @PutMapping("/{idAcessorio}/equipar")
    public ResponseEntity<InventarioUsuario> equipar(@PathVariable int idUsuario, @PathVariable int idAcessorio)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(inventarioBo.equipar(idUsuario, idAcessorio));
    }

    @PutMapping("/{idAcessorio}/desequipar")
    public ResponseEntity<InventarioUsuario> desequipar(@PathVariable int idUsuario, @PathVariable int idAcessorio)
            throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(inventarioBo.desequipar(idUsuario, idAcessorio));
    }

    @DeleteMapping("/{idAcessorio}")
    public ResponseEntity<Void> deletar(@PathVariable int idUsuario, @PathVariable int idAcessorio)
            throws SQLException, ClassNotFoundException {
        inventarioBo.deletar(idUsuario, idAcessorio);
        return ResponseEntity.noContent().build();
    }
}