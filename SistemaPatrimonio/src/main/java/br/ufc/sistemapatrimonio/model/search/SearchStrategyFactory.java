package br.ufc.sistemapatrimonio.model.search;

import br.ufc.sistemapatrimonio.enums.TipoReserva;

public class SearchStrategyFactory {
    
    public static SearchStrategy<? extends ItemBuscavel> criarEstrategia(TipoReserva tipo) {
        switch (tipo) {
            case BEM:
                return new BemSearchStrategy();
            case PATRIMONIO:
                return new PatrimonioSearchStrategy();
            default:
                throw new IllegalArgumentException("Tipo não suportado: " + tipo);
        }
    }
}
