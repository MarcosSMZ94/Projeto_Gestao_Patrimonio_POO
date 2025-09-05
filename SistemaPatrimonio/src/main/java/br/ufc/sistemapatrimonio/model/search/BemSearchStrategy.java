package br.ufc.sistemapatrimonio.model.search;

import java.util.List;

import br.ufc.sistemapatrimonio.entities.Bem;
import br.ufc.sistemapatrimonio.exceptions.BemException;
import br.ufc.sistemapatrimonio.model.Model;

public class BemSearchStrategy implements SearchStrategy<Bem> {
    
    @Override
    public List<Bem> obterTodosItens() {
        return Model.getBens();
    }
    
    @Override
    public List<Bem> obterItensUsuario() {
        return Model.getUsuarioAutenticado().getMeusBens();
    }
    
    @Override
    public String getNomeTipo() {
        return "bem";
    }
    
    @Override
    public Class<? extends Exception> getExceptionClass() {
        return BemException.class;
    }
    
    @Override
    public Exception criarException(int codigoErro, String mensagem) {
        return new BemException(codigoErro, mensagem);
    }
}
