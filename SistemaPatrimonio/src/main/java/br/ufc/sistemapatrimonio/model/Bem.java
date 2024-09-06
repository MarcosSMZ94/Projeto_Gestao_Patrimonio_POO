
package br.ufc.sistemapatrimonio.model;


public class Bem {
    private final int id;
    private String nome;
    private String localAlocado;
    private TipoBem tipo;  // Composição com TipoBem
    
    // Construtor
    public Bem(int id, String nome, String localAlocado, TipoBem tipo) {
        this.id = id;
        setNome(nome);
        setLocalAlocado(localAlocado);
        this.tipo = tipo;
    }

    // Métodos de acesso
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome;
    }

    public String getLocalAlocado() {
        return localAlocado;
    }

    public void setLocalAlocado(String localAlocado) {
        if (localAlocado == null || localAlocado.trim().isEmpty()) {
            throw new IllegalArgumentException("Local Alocado não pode ser vazio.");
        }
        this.localAlocado = localAlocado;
    }

    public TipoBem getTipo() {
        return tipo;
    }

    public void setTipo(TipoBem tipo) {
        this.tipo = tipo;
    }
}

