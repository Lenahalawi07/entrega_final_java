package br.com.fiap.bo;

import br.com.fiap.dao.AdministradorDao;
import br.com.fiap.entities.Administrador;
import br.com.fiap.entities.Usuario;
import br.com.fiap.excecoes.ConflitoException;
import br.com.fiap.excecoes.NaoAutorizadoException;
import br.com.fiap.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.excecoes.ValidacaoException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.List;

public class AdministradorBo {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final int SENHA_MINIMA = 6;
    private static final int ORA_REGISTROS_FILHOS = 2292;

    public List<Administrador> listar() throws SQLException, ClassNotFoundException {
        try (AdministradorDao dao = new AdministradorDao()) {
            return dao.listar();
        }
    }

    public Administrador buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (AdministradorDao dao = new AdministradorDao()) {
            Administrador administrador = dao.buscarPorId(id);
            if (administrador == null) {
                throw new RecursoNaoEncontradoException("Administrador " + id + " nao encontrado");
            }
            return administrador;
        }
    }

    public Administrador cadastrar(Administrador administrador) throws SQLException, ClassNotFoundException {
        validarDados(administrador);
        validarSenha(administrador.getSenha());
        try (AdministradorDao dao = new AdministradorDao()) {
            if (dao.buscarPorEmail(administrador.getEmail()) != null) {
                throw new ConflitoException("E-mail ja cadastrado");
            }
            administrador.setSenha(ENCODER.encode(administrador.getSenha()));
            dao.inserir(administrador);
            return dao.buscarPorEmail(administrador.getEmail());
        }
    }

    public Administrador atualizar(int id, Administrador dados) throws SQLException, ClassNotFoundException {
        validarDados(dados);
        try (AdministradorDao dao = new AdministradorDao()) {
            Administrador atual = dao.buscarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Administrador " + id + " nao encontrado");
            }
            Administrador comEmail = dao.buscarPorEmail(dados.getEmail());
            if (comEmail != null && comEmail.getIdAdministrador() != id) {
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
        try (AdministradorDao dao = new AdministradorDao()) {
            int linhas;
            try {
                linhas = dao.deletar(id);
            } catch (SQLException e) {
                if (e.getErrorCode() == ORA_REGISTROS_FILHOS) {
                    throw new ConflitoException("Administrador possui registros vinculados e nao pode ser removido");
                }
                throw e;
            }
            if (linhas == 0) {
                throw new RecursoNaoEncontradoException("Administrador " + id + " nao encontrado");
            }
        }
    }

    public Administrador login(String email, String senha) throws SQLException, ClassNotFoundException {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new ValidacaoException("E-mail e senha sao obrigatorios");
        }
        try (AdministradorDao dao = new AdministradorDao()) {
            Administrador administrador = dao.buscarPorEmail(email.trim());
            if (administrador == null || !ENCODER.matches(senha, administrador.getSenha())) {
                throw new NaoAutorizadoException("E-mail ou senha invalidos");
            }
            return administrador;
        }
    }

    private void validarDados(Administrador administrador) {
        if (administrador == null) {
            throw new ValidacaoException("Dados do administrador sao obrigatorios");
        }
        if (administrador.getNome() == null || administrador.getNome().isBlank()) {
            throw new ValidacaoException("Nome e obrigatorio");
        }
        administrador.setNome(administrador.getNome().trim());
        if (administrador.getEmail() != null) {
            administrador.setEmail(administrador.getEmail().trim());
        }
        if (!Usuario.emailValido(administrador.getEmail())) {
            throw new ValidacaoException("E-mail invalido");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < SENHA_MINIMA) {
            throw new ValidacaoException("Senha deve ter no minimo " + SENHA_MINIMA + " caracteres");
        }
    }
}
