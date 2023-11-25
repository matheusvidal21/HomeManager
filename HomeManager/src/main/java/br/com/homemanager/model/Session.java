package br.com.homemanager.model;

public class Session {
    private static Session instance;
    private Home currentUser;

    private Session() {
        // Construtor privado para evitar instanciação direta
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public Home getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Home user) {
        currentUser = user;
    }
}
