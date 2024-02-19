package com.victor.demo.controller;

import com.victor.demo.model.Livro;
import com.victor.demo.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public List<Livro> listar() {
        return livroRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> obter(@PathVariable Long id) {
        if (id != null) {
            Optional<Livro> livroOptional = livroRepository.findById(id);
            return livroOptional.map(livro -> new ResponseEntity<>(livro, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Livro> adicionar(@RequestBody Livro livro) {
        if (livro == null) {
            return ResponseEntity.badRequest().build();
        }
        Livro novoLivro = livroRepository.save(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id, @RequestBody Livro livroAtualizado) {
        if (id != null) {
            Optional<Livro> optionalLivro = livroRepository.findById(id);
            return optionalLivro.map(livro -> {
                livro.setTitulo(livroAtualizado.getTitulo());
                livro.setAutor(livroAtualizado.getAutor());
                livro.setIsbn(livroAtualizado.getIsbn());
                livro.setAnoPublicacao(livroAtualizado.getAnoPublicacao());
                livro.setEditora(livroAtualizado.getEditora());
                livroRepository.save(livro);
                return ResponseEntity.ok().body(livro);
            }).orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (id != null) {
            if (livroRepository.existsById(id)) {
                livroRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
