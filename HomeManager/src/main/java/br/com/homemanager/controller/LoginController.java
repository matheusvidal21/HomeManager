package br.com.homemanager.controller;

import br.com.homemanager.application.Program;
import br.com.homemanager.event.EventManager;
import br.com.homemanager.event.UpdateHomeProgressEvent;
import br.com.homemanager.event.UpdateProgressEvent;
import br.com.homemanager.model.Home;
import br.com.homemanager.model.Session;
import br.com.homemanager.repository.HomeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

/**
 * Controlador responsável pela lógica da tela de login.
 */
public class LoginController{

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSignup;
    @FXML
    private Label lbResult;
    private HomePageController homePageController;

    /**
     * Define o controlador da página inicial.
     *
     * @param homePageController O controlador da página inicial.
     */
    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    /**
     * Manipula o clique no botão de login. Implementa a lógica de autenticação do usuário e o redirecionamento para a página inicial.
     */
    public void onBtnLoginClick(){
        String enteredUsername = txtUsername.getText();
        char[] enteredPassword = txtPassword.getText().toCharArray();  // Usando getPassword para obter a senha como um char[]

        // Cria uma instância de Home com as credenciais fornecidas
        Home user = HomeRepository.usernameAlreadyExists(enteredUsername);

        // Verifica a autenticação usando o método verificarSenha
        if (user != null && user.checkPassword(enteredPassword)) {
            Session.getInstance().setCurrentUser(user);

            if (homePageController != null) {
                homePageController.showAllTasks();
                homePageController.addMembersButtons();
            }
            EventManager.getInstance().fireProgressEvent(new UpdateProgressEvent());
            EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
            Program.changeScreen("homePage");
            clearInputFields();
            lbResult.setText("");
        } else {
            lbResult.setText("User not found or incorrect password");
        }
    }

    /**
     * Manipula o clique no botão de registro. Redireciona o usuário para a tela de cadastro.
     *
     * @param event O evento de clique no botão.
     */
    public void onBtnSignClick(ActionEvent event){
        clearInputFields();
        Program.changeScreen("singupPage");
    }

    /**
     * Limpa os campos de entrada.
     */
    private void clearInputFields(){
        btnLogin.setDisable(true);
        txtUsername.clear();
        txtPassword.clear();
    }

    /**
     * Atualiza o estado do botão de login com base nos campos de entrada preenchidos.
     */
    public void onKeyReleased(){
        boolean login;
        login = (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty());
        btnLogin.setDisable(login);
    }

}