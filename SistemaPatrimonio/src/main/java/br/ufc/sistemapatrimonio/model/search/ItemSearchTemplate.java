package br.ufc.sistemapatrimonio.model.search;

import java.util.List;

import br.ufc.sistemapatrimonio.entities.Local;
import br.ufc.sistemapatrimonio.enums.TipoReserva;
import br.ufc.sistemapatrimonio.exceptions.BemException;
import br.ufc.sistemapatrimonio.exceptions.PatrimonioException;

public class ItemSearchTemplate {
    
    private final SearchStrategy<? extends ItemBuscavel> strategy;
    
    public ItemSearchTemplate(TipoReserva tipo) {
        this.strategy = SearchStrategyFactory.criarEstrategia(tipo);
    }

    public ItemBuscavel buscarEValidarItem(int id, String local) throws Exception {
        // 1. Buscar item no sistema
        ItemBuscavel itemEncontrado = buscarItemNoSistema(id);
        
        // 2. Validar se item está disponível
        validarDisponibilidade(itemEncontrado, id);
        
        // 3. Validar se usuário já possui o item
        validarSeUsuarioJaPossui(id);
        
        // 4. Configurar local
        configurarLocal(itemEncontrado, local);
        
        // 5. Marcar como alocado
        itemEncontrado.setAlocstatus(true);
        
        return itemEncontrado;
    }
    
    public void removerItem(int id) throws Exception {
        List<? extends ItemBuscavel> itens = strategy.obterTodosItens();
        
        boolean itemRemovido = false;
        for (ItemBuscavel item : itens) {
            if (item.getId() == id) {
                item.setAlocstatus(false);
                itemRemovido = true;
                break;
            }
        }
        
        if (!itemRemovido) {
            String nomeTipo = strategy.getNomeTipo();
            String mensagem = nomeTipo.substring(0, 1).toUpperCase() + nomeTipo.substring(1) + 
                             " com o ID " + id + " não encontrado.";
            
            if (strategy instanceof BemSearchStrategy) {
                throw (Exception) strategy.criarException(BemException.NAO_ENCONTRADO, mensagem);
            } else if (strategy instanceof PatrimonioSearchStrategy) {
                throw (Exception) strategy.criarException(PatrimonioException.NAO_ENCONTRADO, mensagem);
            }
        }
    }
    
    private ItemBuscavel buscarItemNoSistema(int id) throws Exception {
        List<? extends ItemBuscavel> itens = strategy.obterTodosItens();
        
        for (ItemBuscavel item : itens) {
            if (item.getId() == id) {
                return item;
            }
        }
        
        // Item não encontrado
        String nomeTipo = strategy.getNomeTipo();
        String mensagem = "O " + nomeTipo + " com o ID " + id + " não foi encontrado no sistema.";
        
        if (strategy instanceof BemSearchStrategy) {
            throw (Exception) strategy.criarException(BemException.NAO_ENCONTRADO, mensagem);
        } else if (strategy instanceof PatrimonioSearchStrategy) {
            throw (Exception) strategy.criarException(PatrimonioException.NAO_ENCONTRADO, mensagem);
        }
        
        throw new IllegalStateException("Estratégia não reconhecida");
    }
    
    private void validarDisponibilidade(ItemBuscavel item, int id) throws Exception {
        if (item.isAlocstatus()) {
            String nomeTipo = strategy.getNomeTipo();
            String mensagem = "O " + nomeTipo + " com o ID " + id + " já foi alocado no sistema.";
            
            if (strategy instanceof BemSearchStrategy) {
                throw (Exception) strategy.criarException(BemException.EXISTENTE, mensagem);
            } else if (strategy instanceof PatrimonioSearchStrategy) {
                throw (Exception) strategy.criarException(PatrimonioException.EXISTENTE, mensagem);
            }
        }
    }
    
    private void validarSeUsuarioJaPossui(int id) throws Exception {
        List<? extends ItemBuscavel> itensUsuario = strategy.obterItensUsuario();
        
        for (ItemBuscavel item : itensUsuario) {
            if (item.getId() == id) {
                String nomeTipo = strategy.getNomeTipo();
                String mensagem = "O " + nomeTipo + " com o ID " + id + " já foi requisitado por você.";
                
                if (strategy instanceof BemSearchStrategy) {
                    throw (Exception) strategy.criarException(BemException.EXISTENTE, mensagem);
                } else if (strategy instanceof PatrimonioSearchStrategy) {
                    throw (Exception) strategy.criarException(PatrimonioException.EXISTENTE, mensagem);
                }
            }
        }
    }
    
    private void configurarLocal(ItemBuscavel item, String local) {
        Local novoLocal = new Local(local);
        item.setLocal(novoLocal);
    }
    
    public SearchStrategy<? extends ItemBuscavel> getStrategy() {
        return strategy;
    }
}
