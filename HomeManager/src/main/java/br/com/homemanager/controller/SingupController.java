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

/**
 * Controlador responsável pela lógica da tela de cadastro de usuários.
 */
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

    /**
     * Manipula o clique no botão "Finish" para registrar um novo usuário.
     * Faz uma verificação se o usuário já existe no repositório e se todos os membros estão preenchidos com nomes diferentes.
     */
    public void onBtnFinishClick(){

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

    /**
     * Manipula o clique no botão "Back" para retornar à tela de login.
     *
     * @param event O evento de clique no botão.
     */
    public void onBtnBackClick(ActionEvent event){
        lbResult.setText("");
        clearInputFields();
        Program.changeScreen("loginPage");
    }

    /**
     * Manipula a escolha do número de membros para adicionar ao novo usuário.
     * Adiciona campos TextField para o usuário preencher os nomes dos membros, com base na quantidade informada no ComboBox.
     */
    public void onVboxMemberNamesChoose() {
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

    /**
     * Verifica se todos os campos de nome dos membros estão preenchidos.
     *
     * @return true se todos os campos estiverem preenchidos, false caso contrário.
     */
    public boolean areMembersNameFilled() {
        for (TextField textField : listTxtFieldNames) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Atualiza o estado do botão de "Finish" com base nos campos de entrada preenchidos.
     */
    public void onKeyReleased(){
        boolean finish;
        finish = (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty());
        btnFinish.setDisable(finish);
    }

    /**
     * Limpa os campos de entrada da tela de cadastro.
     */
    private void clearInputFields(){
        txtUsername.clear();
        txtPassword.clear();
        vboxMemberNames.getChildren().clear();
        listTxtFieldNames.forEach(TextField::clear);
    }

    /**
     * Exibe uma mensagem de sucesso na interface.
     *
     * @param message A mensagem de sucesso a ser exibida.
     */
    private void displaySuccessMessage(String message) {
        lbResult.setText(message);
        lbResult.setStyle("-fx-text-fill: green;");
    }

    /**
     * Exibe uma mensagem de erro na interface.
     *
     * @param message A mensagem de erro a ser exibida.
     */
    private void displayErrorMessage(String message) {
        lbResult.setText(message);
        lbResult.setStyle("-fx-text-fill: red;");
    }

    /**
     * Adiciona membros a uma instância de Home, caso não tenha membros repetidos.
     *
     * @param home A instância de Home onde os membros serão adicionados.
     * @return true se os membros são adicionados com sucesso, false se há membros repetidos.
     */
    private boolean addMembersToHome(Home home) {
        if(!existsRepeatedMembers()){
            listTxtFieldNames.stream()
                    .map(TextField::getText)
                    .map(Member::new)
                    .forEach(home::addMember);
            return true;
        }else{
            displayErrorMessage("You have inserted repeated members");
            return false;
        }
    }

    /**
     * Verifica se há membros repetidos nos campos de nome dos membros.
     *
     * @return true se há membros repetidos, false caso contrário.
     */
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
    /**
     * Inicializa a tela de cadastro.
     *
     * @param url            O localizador de recursos.
     * @param resourceBundle O pacote de recursos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cboNumberMembers.getItems().addAll(1,2,3,4,5,6,7);
        listTxtFieldNames = new ArrayList<>();
        cboNumberMembers.setOnAction(event -> onVboxMemberNamesChoose());
    }
}