package br.ufc.sistemapatrimonio.model.search;

import java.util.List;

public interface SearchStrategy<T extends ItemBuscavel> {
    List<T> obterTodosItens();
    List<T> obterItensUsuario();
    String getNomeTipo();
    Class<? extends Exception> getExceptionClass();
    Exception criarException(int codigoErro, String mensagem);
}
