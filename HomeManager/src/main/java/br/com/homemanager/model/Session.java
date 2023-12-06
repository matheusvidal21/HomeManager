package br.com.homemanager.model;

/**
 * Representa uma sessão do usuário para controle de autenticação e gerenciamento do usuário atual. É utilizado o padrão de projeto Singleton.
 */
public class Session {
    /**
     * Instância única da classe Session (Singleton).
     */
    private static Session instance;

    /**
     * Usuário atual logado na sessão.
     */
    private Home currentUser;

    /**
     * Construtor privado para evitar instanciação direta da classe Session.
     */
    private Session() {
        // Construtor privado para evitar instanciação direta
    }

    /**
     * Obtém a instância única da classe Session (Singleton).
     *
     * @return A instância única da classe Session
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Obtém o usuário atualmente logado na sessão.
     *
     * @return O usuário atualmente logado
     */
    public Home getCurrentUser() {
        return currentUser;
    }

    /**
     * Define o usuário atual para a sessão.
     *
     * @param user O usuário a ser definido como o atual na sessão
     */
    public void setCurrentUser(Home user) {
        currentUser = user;
    }
}