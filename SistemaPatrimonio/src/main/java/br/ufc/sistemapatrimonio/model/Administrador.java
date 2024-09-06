package br.ufc.sistemapatrimonio.model;

import br.ufc.sistemapatrimonio.exceptions.DadoNaoEncontradoException;

import java.util.ArrayList;
import java.util.HashMap;

public class Administrador extends Usuario {
    private final HashMap<Integer, Bem> bensMap = new HashMap<>();
    private final HashMap<Integer, Patrimonio> patrimoniosMap = new HashMap<>();
    private final HashMap<Integer, Usuario> usuariosMap = new HashMap<>();
    private final HashMap<Integer, RequisicaoDeManutencao> manutencoesMap = new HashMap<>();
    private final ArrayList<String> historicoDeAcoes = new ArrayList<>(); // Histórico de ações

    public Administrador(String login, String senha) {
        super();
    }

    // Método para registrar ações no histórico
    public void registrarAcao(String acao) {
        historicoDeAcoes.add(acao);
    }

    // Método para obter o histórico de ações
    public ArrayList<String> getHistoricoDeAcoes() {
        return historicoDeAcoes;
    }

    // Métodos para adicionar dados
    public void adicionarBem(Bem bem) {
        bensMap.put(bem.getId(), bem);
        registrarAcao("Adicionou o bem: " + bem.getId());
    }

    public void adicionarPatrimonio(Patrimonio patrimonio) {
        patrimoniosMap.put(patrimonio.getId(), patrimonio);
        registrarAcao("Adicionou o patrimônio: " + patrimonio.getId());
    }

    public void adicionarRequisicaoDeManutencao(RequisicaoDeManutencao requisicao) {
        manutencoesMap.put(requisicao.getId(), requisicao);
        registrarAcao("Adicionou a requisição de manutenção: " + requisicao.getId());
    }

    // Métodos para buscar dados
    public Bem buscarBem(int id) throws DadoNaoEncontradoException {
        Bem bem = bensMap.get(id);
        if (bem == null) {
            throw new DadoNaoEncontradoException("Bem não encontrado.");
        }
        return bem;
    }

    public Patrimonio buscarPatrimonio(int id) throws DadoNaoEncontradoException {
        Patrimonio patrimonio = patrimoniosMap.get(id);
        if (patrimonio == null) {
            throw new DadoNaoEncontradoException("Patrimônio não encontrado.");
        }
        return patrimonio;
    }

    public Usuario buscarUsuario(int id) throws DadoNaoEncontradoException {
        Usuario usuario = usuariosMap.get(id);
        if (usuario == null) {
            throw new DadoNaoEncontradoException("Usuário não encontrado.");
        }
        return usuario;
    }

    public RequisicaoDeManutencao buscarRequisicao(int id) throws DadoNaoEncontradoException {
        RequisicaoDeManutencao requisicao = manutencoesMap.get(id);
        if (requisicao == null) {
            throw new DadoNaoEncontradoException("Requisição de Manutenção não encontrada.");
        }
        return requisicao;
    }

    // Métodos para listar todos os dados
    public ArrayList<Bem> listarBens() {
        return new ArrayList<>(bensMap.values());
    }

    public ArrayList<Patrimonio> listarPatrimonios() {
        return new ArrayList<>(patrimoniosMap.values());
    }

    public ArrayList<Usuario> listarUsuarios() {
        return new ArrayList<>(usuariosMap.values());
    }

    public ArrayList<RequisicaoDeManutencao> listarManutencoes() {
        return new ArrayList<>(manutencoesMap.values());
    }

    // Métodos para excluir dados
    public void excluirBem(int id) throws DadoNaoEncontradoException {
        if (bensMap.containsKey(id)) {
            bensMap.remove(id);
            registrarAcao("Excluiu o bem: " + id);
        } else {
            throw new DadoNaoEncontradoException("Bem não encontrado.");
        }
    }

    public void excluirPatrimonio(int id) throws DadoNaoEncontradoException {
        if (patrimoniosMap.containsKey(id)) {
            patrimoniosMap.remove(id);
            registrarAcao("Excluiu o patrimônio: " + id);
        } else {
            throw new DadoNaoEncontradoException("Patrimônio não encontrado.");
        }
    }

    public void excluirUsuario(int id) throws DadoNaoEncontradoException {
        if (usuariosMap.containsKey(id)) {
            usuariosMap.remove(id);
            registrarAcao("Excluiu o usuário: " + id);
        } else {
            throw new DadoNaoEncontradoException("Usuário não encontrado.");
        }
    }

    public void excluirRequisicaoDeManutencao(int id) throws DadoNaoEncontradoException {
        if (manutencoesMap.containsKey(id)) {
            manutencoesMap.remove(id);
            registrarAcao("Excluiu a requisição de manutenção: " + id);
        } else {
            throw new DadoNaoEncontradoException("Requisição de Manutenção não encontrada.");
        }
    }

    // Métodos para aprovar/rejeitar requisições de manutenção
    public void aprovarRequisicaoDeManutencao(int id) throws DadoNaoEncontradoException {
        RequisicaoDeManutencao requisicao = buscarRequisicao(id);
        requisicao.setStatus(Boolean.TRUE);
        registrarAcao("Aprovou a requisição de manutenção: " + id);
    }

    public void rejeitarRequisicaoDeManutencao(int id) throws DadoNaoEncontradoException {
        RequisicaoDeManutencao requisicao = buscarRequisicao(id);
        requisicao.setStatus(Boolean.FALSE);
        registrarAcao("Rejeitou a requisição de manutenção: " + id);
    }

    // Métodos para atualizar dados
    public void atualizarBem(Bem bem) throws DadoNaoEncontradoException {
        if (bensMap.containsKey(bem.getId())) {
            bensMap.put(bem.getId(), bem);
            registrarAcao("Atualizou o bem: " + bem.getId());
        } else {
            throw new DadoNaoEncontradoException("Bem não encontrado.");
        }
    }

    public void atualizarPatrimonio(Patrimonio patrimonio) throws DadoNaoEncontradoException {
        if (patrimoniosMap.containsKey(patrimonio.getId())) {
            patrimoniosMap.put(patrimonio.getId(), patrimonio);
            registrarAcao("Atualizou o patrimônio: " + patrimonio.getId());
        } else {
            throw new DadoNaoEncontradoException("Patrimônio não encontrado.");
        }
    }
}
