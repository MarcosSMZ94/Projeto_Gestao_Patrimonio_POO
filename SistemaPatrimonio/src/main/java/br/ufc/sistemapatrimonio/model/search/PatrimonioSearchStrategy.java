package br.ufc.sistemapatrimonio.model.search;

import java.util.List;

import br.ufc.sistemapatrimonio.entities.Patrimonio;
import br.ufc.sistemapatrimonio.exceptions.PatrimonioException;
import br.ufc.sistemapatrimonio.model.Model;


public class PatrimonioSearchStrategy implements SearchStrategy<Patrimonio> {
    
    @Override
    public List<Patrimonio> obterTodosItens() {
        return Model.getPatrimonios();
    }
    
    @Override
    public List<Patrimonio> obterItensUsuario() {
        return Model.getUsuarioAutenticado().getMeusPatrimonios();
    }
    
    @Override
    public String getNomeTipo() {
        return "patrimônio";
    }
    
    @Override
    public Class<? extends Exception> getExceptionClass() {
        return PatrimonioException.class;
    }
    
    @Override
    public Exception criarException(int codigoErro, String mensagem) {
        return new PatrimonioException(codigoErro, mensagem);
    }
}
