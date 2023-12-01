package br.com.homemanager.controller;

import br.com.homemanager.model.Member;
import br.com.homemanager.application.Program;
import br.com.homemanager.model.Home;
import br.com.homemanager.model.Session;
import br.com.homemanager.repository.HomeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SingupController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<Integer> cboQuantidadeMembros;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vboxNomesMembros;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnConcluir;
    @FXML
    private Label lbResult;

    private List<TextField> listaTxtFieldNomes;

    public void onBtnConcluirClick(){

        String enteredUsername = txtUsername.getText();
        char[] enteredPassword = txtPassword.getText().toCharArray();

        if(HomeRepository.usernameAlreadyExists(enteredUsername) != null){
            displayErrorMessage("User already registered");
            clearInputFields();
            return;
        }

        if(cboQuantidadeMembros.getValue() == null || !areMembersNameFilled()){
            displayErrorMessage("Fill in the names of the members");
            return;
        }

        Home homeToAdd =  new Home(enteredUsername, enteredPassword);
        addMembersToHome(homeToAdd);
        HomeRepository.addHome(homeToAdd);

        displaySuccessMessage("Registered user");
        Session.getInstance().setCurrentUser(homeToAdd);
        lbResult.setText("");
        clearInputFields();
        Program.changeScreen("taskChooserPage");

    }

    public void onBtnVoltarClick(ActionEvent event){
        lbResult.setText("");
        clearInputFields();
        Program.changeScreen("loginPage");
    }

    public void onVboxNomesMembrosChoose() {
        int numbersOfMembers = cboQuantidadeMembros.getValue();

        // Limpa os campos antigos
        vboxNomesMembros.getChildren().clear();
        listaTxtFieldNomes.clear();

        // Adiciona novos campos TextField conforme a quantidade de membros
        for (int i = 0; i < numbersOfMembers; i++) {
            Label label = new Label("Member's name " + (i + 1) + ":");
            TextField textFieldNome = new TextField();
            listaTxtFieldNomes.add(textFieldNome);
            textFieldNome.getStyleClass().add("text-field-member");
            label.getStyleClass().add("label-names-styled");

            // Adiciona rótulo e campo de texto ao VBox
            vboxNomesMembros.getChildren().addAll(label, textFieldNome);
        }
        vboxNomesMembros.setSpacing(10); // ESPAÇAMENTO ENTRE OS CAMPOS NOME
    }

    public boolean areMembersNameFilled() {
        for (TextField textField : listaTxtFieldNomes) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void onKeyReleased(){
        boolean concluir;
        concluir = (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty());
        btnConcluir.setDisable(concluir);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cboQuantidadeMembros.getItems().addAll(1,2,3,4,5,6,7,8,9);
        listaTxtFieldNomes = new ArrayList<>();
        cboQuantidadeMembros.setOnAction(event -> onVboxNomesMembrosChoose());
    }

    private void clearInputFields(){
        txtUsername.clear();
        txtPassword.clear();
        vboxNomesMembros.getChildren().clear();
        listaTxtFieldNomes.forEach(TextField::clear);
    }

    private void displaySuccessMessage(String message) {
        lbResult.setText(message);
        lbResult.setStyle("-fx-text-fill: green;");
    }

    private void displayErrorMessage(String message) {
        lbResult.setText(message);
        lbResult.setStyle("-fx-text-fill: red;");
    }

    private void addMembersToHome(Home home) {
        listaTxtFieldNomes.stream()
                .map(TextField::getText)
                .map(Member::new)
                .forEach(home::addMember);
    }
}



