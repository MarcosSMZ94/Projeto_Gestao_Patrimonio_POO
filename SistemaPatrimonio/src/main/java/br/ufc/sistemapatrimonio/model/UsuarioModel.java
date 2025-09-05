package br.ufc.sistemapatrimonio.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ufc.sistemapatrimonio.entities.Bem;
import br.ufc.sistemapatrimonio.entities.Local;
import br.ufc.sistemapatrimonio.entities.Patrimonio;
import br.ufc.sistemapatrimonio.entities.RequisicaoDeManutencao;
import br.ufc.sistemapatrimonio.entities.RequisicaoDeReserva;
import br.ufc.sistemapatrimonio.entities.Usuario;
import br.ufc.sistemapatrimonio.enums.TipoReserva;
import br.ufc.sistemapatrimonio.exceptions.BemException;
import br.ufc.sistemapatrimonio.exceptions.ManutencaoException;
import br.ufc.sistemapatrimonio.exceptions.PatrimonioException;
import br.ufc.sistemapatrimonio.model.search.ItemBuscavel;
import br.ufc.sistemapatrimonio.model.search.ItemSearchTemplate;
import javafx.scene.control.Alert;

public class UsuarioModel {
    private final Model model = new Model();

    public void adicionarRequisicao(int id, String nome, String local, String descricao, TipoReserva tipo) throws BemException, PatrimonioException, IOException {
        try {
            ItemSearchTemplate searchTemplate = new ItemSearchTemplate(tipo);
            ItemBuscavel itemEncontrado = searchTemplate.buscarEValidarItem(id, local);
            
            RequisicaoDeReserva requisicaoDeReserva = new RequisicaoDeReserva(
                itemEncontrado.getId(), nome, new Local(local), descricao, tipo, 
                Model.getUsuarioAutenticado().getUsername()
            );
            
            model.getrequisicaoDeReservas().add(requisicaoDeReserva);
            Model.getUsuarioAutenticado().getMinhasRequisicaoDeReservas().add(requisicaoDeReserva);
            
            if (tipo == TipoReserva.BEM) {
                Model.adicionarBem((Bem) itemEncontrado);
            } else if (tipo == TipoReserva.PATRIMONIO) {
                Model.adicionarPatrimonio((Patrimonio) itemEncontrado);
            }
            
        } catch (Exception e) {
            if (e instanceof BemException) {
                throw (BemException) e;
            } else if (e instanceof PatrimonioException) {
                throw (PatrimonioException) e;
            } else {
                throw new IOException("Erro inesperado: " + e.getMessage());
            }
        }
    }

    public void removerRequisicao(int id, TipoReserva tipo) throws IOException, PatrimonioException, BemException {
        try {
            ItemSearchTemplate searchTemplate = new ItemSearchTemplate(tipo);
            searchTemplate.removerItem(id);
            
            Usuario usuarioAutenticado = Model.getUsuarioAutenticado();
            List<RequisicaoDeReserva> removidas = new ArrayList<>();
            for (RequisicaoDeReserva requisicao : usuarioAutenticado.getMinhasRequisicaoDeReservas()) {
                if (requisicao.getId() == id && requisicao.getTipoReserva() == tipo) {
                    removidas.add(requisicao);
                }
            }
            usuarioAutenticado.getMinhasRequisicaoDeReservas().removeAll(removidas);
            
        } catch (Exception e) {
            if (e instanceof BemException) {
                throw (BemException) e;
            } else if (e instanceof PatrimonioException) {
                throw (PatrimonioException) e;
            } else {
                throw new IOException("Erro inesperado: " + e.getMessage());
            }
        }
    }

    public String listarReservasUsuario() {
        StringBuilder lista = new StringBuilder();
        for (RequisicaoDeReserva requisicaoDeReserva : Model.getUsuarioAutenticado().getMinhasRequisicaoDeReservas()) {
            // Adiciona todas as informações relevantes das reservas
            lista.append("ID: ").append(requisicaoDeReserva.getId())
                    .append(", Nome: ").append(requisicaoDeReserva.getNome())
                    .append(", Local: ").append(requisicaoDeReserva.getLocal().getEndereco())
                    .append(", Descrição: ").append(requisicaoDeReserva.getDescricao())
                    .append(", Tipo: ").append(requisicaoDeReserva.getTipoReserva().toString())
                    .append("\n");
        }
        return lista.toString();
    }

    public void adicionarManutencao(int id, String nome, String descricao, TipoReserva tipo) throws BemException, PatrimonioException, IOException {
        if (tipo == TipoReserva.BEM) {
            // Obter a lista de bens do sistema
            List<RequisicaoDeManutencao> requisicaoDeManutencaos = model.getRequisicaoDeManutencao();

            List<Bem> bensSistema = Model.getBens();

            boolean bemSelecionado = false;

            // Verificar se o bem existe no sistema e se já está alocado
            for (Bem bem : bensSistema) {
                if (bem.getId() == id) {
                    bemSelecionado = true;
                    break;
                }
            }

            // Caso o bem não seja encontrado no sistema
            if (!bemSelecionado) {
                throw new BemException(BemException.NAO_ENCONTRADO, "O bem com o ID " + id + " não foi encontrado no sistema.");
            }

            // Verificar se o bem existe no sistema e se já está alocado
            for (RequisicaoDeManutencao manutencao : requisicaoDeManutencaos) {
                if (manutencao.getId() == id && manutencao.getTipo() == tipo) {
                    if (manutencao.getStatus()) {
                        throw new BemException(BemException.EXISTENTE, "O bem com o ID " + id + " já foi cadastrado para manutenção no sistema.");
                    }
                    break;
                }
            }

            Usuario usuarioAutenticado = Model.getUsuarioAutenticado();

            RequisicaoDeManutencao requisicaoDeManutencao = new RequisicaoDeManutencao(id, nome, descricao, tipo, true, Model.getUsuarioAutenticado().getUsername());

            model.getRequisicaoDeManutencao().add(requisicaoDeManutencao);

            usuarioAutenticado.getMinhasManutencoes().add(requisicaoDeManutencao);

        } else if (tipo == TipoReserva.PATRIMONIO) {
            // Obter a lista de bens do sistema
            List<RequisicaoDeManutencao> requisicaoDeManutencaos = model.getRequisicaoDeManutencao();

            List<Patrimonio> patrimoniosSistema = Model.getPatrimonios();

            boolean patrimonioSelecionado = false;

            // Verificar se o bem existe no sistema e se já está alocado
            for (Patrimonio patrimonio : patrimoniosSistema) {
                if (patrimonio.getId() == id) {
                    patrimonioSelecionado = true;
                    break;
                }
            }

            // Caso o bem não seja encontrado no sistema
            if (!patrimonioSelecionado) {
                throw new PatrimonioException(PatrimonioException.NAO_ENCONTRADO, "O patrimonio com o ID " + id + " não foi encontrado no sistema.");
            }

            // Verificar se o bem existe no sistema e se já está alocado
            for (RequisicaoDeManutencao manutencao : requisicaoDeManutencaos) {
                if (manutencao.getId() == id && manutencao.getTipo() == tipo) {
                    if (manutencao.getStatus()) {
                        throw new PatrimonioException(PatrimonioException.EXISTENTE, "O patrimonio com o ID " + id + " já foi cadastrado para manutenção no sistema.");
                    }
                    break;
                }
            }

            Usuario usuarioAutenticado = Model.getUsuarioAutenticado();

            RequisicaoDeManutencao requisicaoDeManutencao = new RequisicaoDeManutencao(id, nome, descricao, tipo, true, Model.getUsuarioAutenticado().getUsername());

            model.getRequisicaoDeManutencao().add(requisicaoDeManutencao);

            usuarioAutenticado.getMinhasManutencoes().add(requisicaoDeManutencao);

        } else {
            throw new IOException("Algum erro ocorreu");
        }
    }

    public void editarManutencao(int id, String nome, String descricao, TipoReserva tipo) throws ManutencaoException, IOException{
        if(tipo == TipoReserva.BEM){
            List<RequisicaoDeManutencao> requisicaoDeManutencaos = model.getRequisicaoDeManutencao();

            boolean manutencaoSelecionada = false;

            // Verificar se o bem existe no sistema e se já está alocado
            for (RequisicaoDeManutencao requisicaoDeManutencao : requisicaoDeManutencaos) {
                if (requisicaoDeManutencao.getId() == id && requisicaoDeManutencao.getTipo() == tipo) {
                    manutencaoSelecionada = true;
                    requisicaoDeManutencao.setNome(nome);
                    requisicaoDeManutencao.setDescricao(descricao);
                    break;
                }
            }

            // Caso o bem não seja encontrado no sistema
            if (!manutencaoSelecionada) {
                throw new ManutencaoException(ManutencaoException.NAO_ENCONTRADO, "O Manutenção de Bem com o ID " + id + " não foi encontrado no sistema.");
            }

        } else if (tipo == TipoReserva.PATRIMONIO) {
            List<RequisicaoDeManutencao> requisicaoDeManutencaos = model.getRequisicaoDeManutencao();

            boolean manutencaoSelecionada = false;

            // Verificar se o bem existe no sistema e se já está alocado
            for (RequisicaoDeManutencao requisicaoDeManutencao : requisicaoDeManutencaos) {
                if (requisicaoDeManutencao.getId() == id && requisicaoDeManutencao.getTipo() == tipo) {
                    manutencaoSelecionada = true;
                    requisicaoDeManutencao.setNome(nome);
                    requisicaoDeManutencao.setDescricao(descricao);
                    break;
                }
            }

            // Caso o bem não seja encontrado no sistema
            if (!manutencaoSelecionada) {
                throw new ManutencaoException(ManutencaoException.NAO_ENCONTRADO, "O Manutenção de Patrimonio com o ID " + id + " não foi encontrado no sistema.");
            }
        }else {
            throw new IOException("Algum erro ocorreu");
        }
    }

    public void removerReqManutencao(int id, TipoReserva tipo) throws IOException, ManutencaoException {
        Usuario usuarioAutenticado = Model.getUsuarioAutenticado();
        //System.out.println(id + tipo.toString());
        if (tipo == TipoReserva.BEM) {
            // Remover o bem ou patrimônio
            boolean itemRemovido = false;

            Iterator<RequisicaoDeManutencao> requisicaoIterator = model.getRequisicaoDeManutencao().iterator();
            while (requisicaoIterator.hasNext()) {
                RequisicaoDeManutencao requisicao = requisicaoIterator.next();
                if (requisicao.getId() == id && requisicao.getTipo() == tipo) {
                    requisicaoIterator.remove(); // Remover a requisição de reserva associada
                    itemRemovido = true;
                    break; // Sai do loop após remover a requisição
                }
            }

            if (!itemRemovido) {
                model.mostrarPopup("Erro", "Manutenção de Bem com o ID " + id + " não encontrado.", Alert.AlertType.ERROR);
                throw new ManutencaoException(ManutencaoException.NAO_ENCONTRADO, "Manutenção de Bem com o ID " + id + " não encontrado.");
            }

        } else if (tipo == TipoReserva.PATRIMONIO) {
            boolean itemRemovido = false;

            Iterator<RequisicaoDeManutencao> requisicaoIterator = model.getRequisicaoDeManutencao().iterator();
            while (requisicaoIterator.hasNext()) {
                RequisicaoDeManutencao requisicao = requisicaoIterator.next();
                if (requisicao.getId() == id && requisicao.getTipo() == tipo) {
                    requisicaoIterator.remove(); // Remover a requisição de reserva associada
                    itemRemovido = true;
                    break; // Sai do loop após remover a requisição
                }
            }

            if (!itemRemovido) {
                model.mostrarPopup("Erro", "Manutenção de Patrimonio com o ID " + id + " não encontrado.", Alert.AlertType.ERROR);
                throw new ManutencaoException(ManutencaoException.NAO_ENCONTRADO, "Manutenção de Patrimonio com o ID " + id + " não encontrado.");
            }
        } else {
            throw new IOException("Um erro ocorreu");
        }

        List<RequisicaoDeReserva> removidas = new ArrayList<>();
        for (RequisicaoDeReserva requisicao : usuarioAutenticado.getMinhasRequisicaoDeReservas()) {
            if (requisicao.getId() == id && requisicao.getTipoReserva() == tipo) {
                removidas.add(requisicao);
            }
        }
        usuarioAutenticado.getMinhasRequisicaoDeReservas().removeAll(removidas);

        // Remover a requisição de reserva associada
        Iterator<RequisicaoDeManutencao> requisicaoIterator = usuarioAutenticado.getMinhasManutencoes().iterator();
        while (requisicaoIterator.hasNext()) {
            RequisicaoDeManutencao requisicao = requisicaoIterator.next();
            if (requisicao.getId() == id && requisicao.getTipo() == tipo) {
                requisicaoIterator.remove(); // Remover a requisição de reserva associada
                break; // Sai do loop após remover a requisição
            }
        }
    }

    public String listarManutencoesUsuario() {
        StringBuilder lista = new StringBuilder();
        for (RequisicaoDeManutencao requisicaoDeManutencao : Model.getUsuarioAutenticado().getMinhasManutencoes()) {
            // Adiciona todas as informações relevantes das reservas
            lista.append("ID: ").append(requisicaoDeManutencao.getId())
                    .append(", Nome: ").append(requisicaoDeManutencao.getNome())
                    .append(", Descrição: ").append(requisicaoDeManutencao.getDescricao())
                    .append(", Tipo: ").append(requisicaoDeManutencao.getTipo().toString())
                    .append("\n");
        }
        return lista.toString();
    }

}
