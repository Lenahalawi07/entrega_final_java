package br.com.fiap.bo;

import br.com.fiap.dao.UsuarioDao;
import br.com.fiap.entities.Usuario;
import br.com.fiap.excecoes.ConflitoException;
import br.com.fiap.excecoes.NaoAutorizadoException;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.ValidacaoException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.List;

public class UsuarioBo {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final int SENHA_MINIMA = 6;
    private static final int ORA_REGISTROS_FILHOS = 2292;

    public List<Usuario> listar() throws SQLException, ClassNotFoundException {
        try (UsuarioDao dao = new UsuarioDao()) {
            return dao.listar();
        }
    }

    public Usuario buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (UsuarioDao dao = new UsuarioDao()) {
            Usuario usuario = dao.buscarPorId(id);
            if (usuario == null) {
                throw new RecursoNaoEncontradoException("Usuario " + id + " nao encontrado");
            }
            return usuario;
        }
    }

    public Usuario cadastrar(Usuario usuario) throws SQLException, ClassNotFoundException {
        validarDados(usuario);
        validarSenha(usuario.getSenha());
        try (UsuarioDao dao = new UsuarioDao()) {
            if (dao.buscarPorEmail(usuario.getEmail()) != null) {
                throw new ConflitoException("E-mail ja cadastrado");
            }
            usuario.setSenha(ENCODER.encode(usuario.getSenha()));
            dao.inserir(usuario);
            return dao.buscarPorEmail(usuario.getEmail());
        }
    }

    public Usuario atualizar(int id, Usuario dados) throws SQLException, ClassNotFoundException {
        validarDados(dados);
        try (UsuarioDao dao = new UsuarioDao()) {
            Usuario atual = dao.buscarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Usuario " + id + " nao encontrado");
            }
            Usuario comEmail = dao.buscarPorEmail(dados.getEmail());
            if (comEmail != null && comEmail.getIdUsuario() != id) {
                throw new ConflitoException("E-mail ja cadastrado");
            }
            atual.setNome(dados.getNome());
            atual.setEmail(dados.getEmail());
            if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
                validarSenha(dados.getSenha());
                atual.setSenha(ENCODER.encode(dados.getSenha()));
            }
            dao.atualizar(atual);
            return atual;
        }
    }

    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (UsuarioDao dao = new UsuarioDao()) {
            int linhas;
            try {
                linhas = dao.deletar(id);
            } catch (SQLException e) {
                if (e.getErrorCode() == ORA_REGISTROS_FILHOS) {
                    throw new ConflitoException("Usuario possui registros vinculados e nao pode ser removido");
                }
                throw e;
            }
            if (linhas == 0) {
                throw new RecursoNaoEncontradoException("Usuario " + id + " nao encontrado");
            }
        }
    }

    public Usuario login(String email, String senha) throws SQLException, ClassNotFoundException {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new ValidacaoException("E-mail e senha sao obrigatorios");
        }
        try (UsuarioDao dao = new UsuarioDao()) {
            Usuario usuario = dao.buscarPorEmail(email.trim());
            if (usuario == null || !ENCODER.matches(senha, usuario.getSenha())) {
                throw new NaoAutorizadoException("E-mail ou senha invalidos");
            }
            return usuario;
        }
    }

    private void validarDados(Usuario usuario) {
        if (usuario == null) {
            throw new ValidacaoException("Dados do usuario sao obrigatorios");
        }
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new ValidacaoException("Nome e obrigatorio");
        }
        if (!Usuario.emailValido(usuario.getEmail())) {
            throw new ValidacaoException("E-mail invalido");
        }
        usuario.setNome(usuario.getNome().trim());
        usuario.setEmail(usuario.getEmail().trim());
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < SENHA_MINIMA) {
            throw new ValidacaoException("Senha deve ter no minimo " + SENHA_MINIMA + " caracteres");
        }
    }
}