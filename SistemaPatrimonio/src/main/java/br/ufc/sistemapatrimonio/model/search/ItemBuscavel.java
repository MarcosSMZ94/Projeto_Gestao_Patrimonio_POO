package br.ufc.sistemapatrimonio.model.search;

import br.ufc.sistemapatrimonio.entities.Local;

public interface ItemBuscavel {
    int getId();
    String getNome();
    boolean isAlocstatus();
    void setAlocstatus(boolean status);
    void setLocal(Local local);
}
