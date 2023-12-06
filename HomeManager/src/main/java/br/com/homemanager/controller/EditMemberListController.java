package br.com.homemanager.controller;

import br.com.homemanager.application.Program;
import br.com.homemanager.event.EditMemberListEvent;
import br.com.homemanager.event.EventManager;
import br.com.homemanager.event.ShowMemberButtonsEvent;
import br.com.homemanager.event.UpdateHomeProgressEvent;
import br.com.homemanager.model.Home;
import br.com.homemanager.model.Member;
import br.com.homemanager.model.Session;
import br.com.homemanager.model.Task;
import br.com.homemanager.repository.HomeRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Controlador responsável pela edição da lista de membros.
 * Permite adicionar ou remover membros, visualizar membros existentes e controlar eventos associados à interface gráfica.
 * Gerencia a atualização da lista de membros, a exibição de avisos e confirmações na interface.
 */
public class EditMemberListController implements Initializable {
    @FXML
    private Button btnFinish;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;
    @FXML
    private ComboBox<Integer> cboNewMember;
    @FXML
    private ScrollPane scrollPaneCurrentMembers;
    @FXML
    private ScrollPane scrollPaneNewMembers;
    @FXML
    private VBox vbCurrentMembers;
    @FXML
    private VBox vbNewMembers;
    @FXML
    private Label lbWarning;

    /**
     * Altera a tela para a página inicial.
     * Limpa campos de entrada e atualiza a barra de progresso na tela inicial.
     */
    public void onBtnHomeClick(){
        Program.changeScreen("homePage");
        HomeRepository.saveUserData();
        EventManager.getInstance().fireShowMemberButtonsEvent(new ShowMemberButtonsEvent());
        EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
        lbWarning.setText("");
        clearInputFields();
    }

    /**
     * Remove membros selecionados da lista de membros existentes.
     * Atualiza a exibição dos membros atuais após a remoção.
     */
    public void onBtnRemoveClick(){
        lbWarning.setText("");
        Home currentUser = Session.getInstance().getCurrentUser();
        if(currentUser.getMembersList().size() > 0){
            List<Member> memberListToRemove = getSelectedCurrentMembers(vbCurrentMembers, Member::new);
            currentUser.getMembersList().removeAll(memberListToRemove);
            HomeRepository.saveUserData();
            EventManager.getInstance().fireEditMemberListEvent(new EditMemberListEvent());
            clearInputFields();
        }else{
            lbWarning.setText("Your member list is already empty");
        }
    }

    /**
     * Adiciona membros selecionados à lista de membros existentes.
     * Exibe um aviso caso algum membro já exista na lista.
     */
    public void onBtnAddClick(){
        Home currentUser = Session.getInstance().getCurrentUser();
        List<Member> memberListToAdd = getSelectedNewMembers(vbNewMembers, Member::new);
        StringBuilder duplicateNames = new StringBuilder();

        for(Member newMember : memberListToAdd){
            String newMemberName = newMember.getName();
            if(!currentUser.getMembersList().stream().anyMatch(member -> member.getName().equals(newMemberName))){
                currentUser.getMembersList().add(newMember);
            }else{
                if(!duplicateNames.isEmpty()){
                    duplicateNames.append(", ");
                }
                duplicateNames.append(newMemberName);
            }
        }

        if(!duplicateNames.isEmpty()){
            lbWarning.setText("The members with the following names already exist on the list: " + duplicateNames.toString());
        }else{
            lbWarning.setText("");
        }

        HomeRepository.saveUserData();
        clearInputFields();
        EventManager.getInstance().fireEditMemberListEvent(new EditMemberListEvent());
    }

    /**
     * Altera a tela para a página inicial após salvar as informações do usuário e notificar eventos.
     * Limpa campos de entrada e atualiza a barra de progresso na tela inicial.
     */
    public void onBtnFinishClick(){
        EventManager.getInstance().fireShowMemberButtonsEvent(new ShowMemberButtonsEvent());
        EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
        HomeRepository.saveUserData();
        lbWarning.setText("");
        Program.changeScreen("homePage");
        clearInputFields();
    }

    /**
     * Retorna uma lista dos membros atualmente selecionados na interface gráfica.
     *
     * @param vbCurrentMembers   VBox que exibe os membros atuais na interface
     * @param memberConstructor  Função construtora de membros
     * @return                   Lista dos membros atualmente selecionados
     */
    public List<Member> getSelectedCurrentMembers(VBox vbCurrentMembers, Function<String, Member> memberConstructor){
        return vbCurrentMembers.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox) hBox.getChildren().get(0)).isSelected())
                .map(hBox -> memberConstructor.apply(((Label) hBox.getChildren().get(1)).getText()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna uma lista dos novos membros atualmente selecionados na interface gráfica.
     *
     * @param vbNewMembers       VBox que exibe os novos membros na interface
     * @param memberConstructor  Função construtora de membros
     * @return                   Lista dos novos membros atualmente selecionados
     */
    public List<Member> getSelectedNewMembers(VBox vbNewMembers, Function<String, Member> memberConstructor){
        return vbNewMembers.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox) hBox.getChildren().get(0)).isSelected() && ((TextField) hBox.getChildren().get(1)).getText().length() > 0)
                .map(hBox -> memberConstructor.apply(((TextField) hBox.getChildren().get(1)).getText()))
                .collect(Collectors.toList());
    }

    /**
     * Atualiza a exibição dos membros existentes na interface.
     * Exibe os membros atuais em um VBox na interface gráfica.
     */
    public void onVboxCurrentMembers(){
        List<Member> memberList = Session.getInstance().getCurrentUser().getMembersList();
        if(memberList != null){
            vbCurrentMembers.getChildren().clear();

            for(Member member : memberList){
                CheckBox checkBox = new CheckBox();
                checkBox.getStyleClass().add("custom-checkbox");

                Label label = new Label(member.getName());
                label.getStyleClass().add("label-member");

                HBox.setMargin(checkBox, new Insets(4, 0, 0, 0));

                HBox hBox = new HBox(checkBox, label);
                hBox.setSpacing(9);
                vbCurrentMembers.getChildren().add(hBox);
            }
        }
    }

    /**
     * Adiciona membros à lista baseado na seleção do ComboBox de novos membros.
     * Atualiza a exibição dos novos membros em um VBox na interface gráfica.
     */
    public void onVboxNewMembers(){
        if(cboNewMember.getValue() != null){
            int numberOfMembers = cboNewMember.getValue();
            vbNewMembers.getChildren().clear();

            for(int i = 0; i < numberOfMembers; i++){
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(true);
                checkBox.getStyleClass().add("custom-checkbox");
                TextField textFieldMember = new TextField();
                textFieldMember.getStyleClass().add("text-field-no-border");

                HBox hBox = new HBox(checkBox, textFieldMember);
                hBox.setSpacing(9);
                vbNewMembers.getChildren().add(hBox);
            }
        }
    }

    /**
     * Configura o espaçamento e alinhamento de elementos na interface gráfica.
     */
    private void setSpacingVbox(){
        vbCurrentMembers.setSpacing(5);
        vbCurrentMembers.setAlignment(Pos.TOP_CENTER);
        vbNewMembers.setSpacing(5);
        vbNewMembers.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * Limpa os campos de entrada e as confirmações na interface gráfica.
     */
    private void clearInputFields(){
        cboNewMember.getSelectionModel().clearSelection();
        vbNewMembers.getChildren().clear();
    }

    /**
     * Inicializa a interface gráfica quando o controlador é carregado.
     * Configura elementos gráficos, como ComboBoxes e EventManager, e define ações para interações com os membros.
     *
     * @param url           Localização do recurso inicializado
     * @param resourceBundle Recurso inicializado contendo dados localizados
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cboNewMember.getItems().addAll(1,2,3,4,5,6,7);
        setSpacingVbox();
        EventManager.getInstance().setEditMemberListEventHandler(event -> {
            onVboxCurrentMembers();
            cboNewMember.setOnAction(actionEvent -> onVboxNewMembers());
        });
    }
}