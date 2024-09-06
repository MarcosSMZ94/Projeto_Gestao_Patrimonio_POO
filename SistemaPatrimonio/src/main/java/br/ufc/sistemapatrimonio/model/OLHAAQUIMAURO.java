package br.ufc.sistemapatrimonio.model;

public class OLHAAQUIMAURO {
        private final TelaLoginCadastroModel telaLoginCadastroModel;

        public TelaLoginCadastroController(TelaLoginCadastroModel model) {
            this.telaLoginCadastroModel = model;
        }

    // Método para fazer login de requisitante
        public Requisitante requisitanteLogin(String login, String senha) {
            Requisitante requisitante = telaLoginCadastroModel.autenticarRequisitante(login, senha);
            if (requisitante == null) {
                throw new RuntimeException("Login ou senha inválidos para requisitante.");
            }
            return requisitante;
        }

        // Método para fazer login de administrador
        public Administrador administradorLogin(String login, String senha) {
            Administrador administrador = telaLoginCadastroModel.autenticarAdministrador(login, senha);
            if (administrador == null) {
                throw new RuntimeException("Login ou senha inválidos para administrador.");
            }
            return administrador;
        }

        // Método para cadastrar um novo requisitante
        public void cadastrarNovoRequisitante(String login, String senha) {
            try {
                telaLoginCadastroModel.cadastrarRequisitante(login, senha);
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao cadastrar requisitante: " + e.getMessage());
            }
        }

        // Método para cadastrar um novo administrador
        public void cadastrarNovoAdministrador(String login, String senha) {
            try {
                telaLoginCadastroModel.cadastrarAdministrador(login, senha);
            } catch (RuntimeException e) {
                throw new RuntimeException("Erro ao cadastrar administrador: " + e.getMessage());
            }
        }
    }

