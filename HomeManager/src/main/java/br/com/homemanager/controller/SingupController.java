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
import java.util.*;

public class SingupController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<Integer> cboNumberMembers;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vboxMemberNames;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnFinish;
    @FXML
    private Label lbResult;

    private List<TextField> listTxtFieldNames;

    public void onBtnConcluirClick(){

        String enteredUsername = txtUsername.getText();
        char[] enteredPassword = txtPassword.getText().toCharArray();

        if(HomeRepository.usernameAlreadyExists(enteredUsername) != null){
            displayErrorMessage("User already registered");
            clearInputFields();
            return;
        }

        if(cboNumberMembers.getValue() == null || !areMembersNameFilled()){
            displayErrorMessage("Fill in the names of the members");
            return;
        }

        Home homeToAdd =  new Home(enteredUsername, enteredPassword);
        if(addMembersToHome(homeToAdd)){
            HomeRepository.addHome(homeToAdd);

            displaySuccessMessage("Registered user");
            Session.getInstance().setCurrentUser(homeToAdd);
            lbResult.setText("");
            clearInputFields();
            Program.changeScreen("taskChooserPage");
        }
    }

    public void onBtnVoltarClick(ActionEvent event){
        lbResult.setText("");
        clearInputFields();
        Program.changeScreen("loginPage");
    }

    public void onVboxNomesMembrosChoose() {
        int numbersOfMembers = cboNumberMembers.getValue();

        // Limpa os campos antigos
        vboxMemberNames.getChildren().clear();
        listTxtFieldNames.clear();

        // Adiciona novos campos TextField conforme a quantidade de membros
        for (int i = 0; i < numbersOfMembers; i++) {
            Label label = new Label("Member's name " + (i + 1) + ":");
            TextField textFieldNome = new TextField();
            listTxtFieldNames.add(textFieldNome);
            textFieldNome.getStyleClass().add("text-field-member");
            label.getStyleClass().add("label-names-styled");

            // Adiciona rótulo e campo de texto ao VBox
            vboxMemberNames.getChildren().addAll(label, textFieldNome);
        }
        vboxMemberNames.setSpacing(10); // ESPAÇAMENTO ENTRE OS CAMPOS NOME
    }

    public boolean areMembersNameFilled() {
        for (TextField textField : listTxtFieldNames) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void onKeyReleased(){
        boolean concluir;
        concluir = (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty());
        btnFinish.setDisable(concluir);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cboNumberMembers.getItems().addAll(1,2,3,4,5,6,7);
        listTxtFieldNames = new ArrayList<>();
        cboNumberMembers.setOnAction(event -> onVboxNomesMembrosChoose());
    }

    private void clearInputFields(){
        txtUsername.clear();
        txtPassword.clear();
        vboxMemberNames.getChildren().clear();
        listTxtFieldNames.forEach(TextField::clear);
    }

    private void displaySuccessMessage(String message) {
        lbResult.setText(message);
        lbResult.setStyle("-fx-text-fill: green;");
    }

    private void displayErrorMessage(String message) {
        lbResult.setText(message);
        lbResult.setStyle("-fx-text-fill: red;");
    }

    private boolean addMembersToHome(Home home) {
        if(!existsRepeatedMembers()){
            listTxtFieldNames.stream()
                    .map(TextField::getText)
                    .map(Member::new)
                    .forEach(home::addMember);
            return true;
        }else{
            lbResult.setText("You have inserted repeated members");
            return false;
        }
    }

    private boolean existsRepeatedMembers(){
        Set<String> memberNames = new HashSet<>();
        for(TextField textField : listTxtFieldNames){
            String memberName = textField.getText().trim();
            if(!memberName.isEmpty()){
                if(!memberNames.add(memberName)){
                    return true;
                }
            }
        }
        return false;
    }

}



