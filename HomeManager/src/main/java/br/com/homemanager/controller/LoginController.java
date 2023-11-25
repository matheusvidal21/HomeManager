package br.com.homemanager.controller;

import br.com.homemanager.application.Program;
import br.com.homemanager.model.Home;
import br.com.homemanager.model.Session;
import br.com.homemanager.repository.HomeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginController{

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSignup;
    @FXML
    private Label lbResult;


    public void onBtnLoginClick(){
        String enteredUsername = txtUsername.getText();
        char[] enteredPassword = txtPassword.getText().toCharArray();  // Usando getPassword para obter a senha como um char[]

        // Cria uma instância de Home com as credenciais fornecidas
        Home user = HomeRepository.usernameAlreadyExists(enteredUsername);

    }

    public void onBtnSignClick(ActionEvent event){
        Program.changeScreen("singupPage");
    }

    public void onKeyReleased(){
        boolean login;
        login = (txtUsername.getText().isEmpty() | txtPassword.getText().isEmpty());
        btnLogin.setDisable(login);
    }

}
