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
    private Label lbConfirmation;
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;
    @FXML
    private Label lbWarning;

    public void onBtnHomeClick(){
        Program.changeScreen("homePage");
        HomeRepository.saveUserData();
        EventManager.getInstance().fireShowMemberButtonsEvent(new ShowMemberButtonsEvent());
        EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
        lbWarning.setText("");
        clearInputFields();
    }

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

    public void onBtnFinishClick(){
        // showConfirmation(true);
        EventManager.getInstance().fireShowMemberButtonsEvent(new ShowMemberButtonsEvent());
        EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
        HomeRepository.saveUserData();
        lbWarning.setText("");
        Program.changeScreen("homePage");
        clearInputFields();
    }

    public List<Member> getSelectedCurrentMembers(VBox vbCurrentMembers, Function<String, Member> memberConstructor){
        return vbCurrentMembers.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox) hBox.getChildren().get(0)).isSelected())
                .map(hBox -> memberConstructor.apply(((Label) hBox.getChildren().get(1)).getText()))
                .collect(Collectors.toList());
    }

    public List<Member> getSelectedNewMembers(VBox vbNewMembers, Function<String, Member> memberConstructor){
        return vbNewMembers.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox) hBox.getChildren().get(0)).isSelected() && ((TextField) hBox.getChildren().get(1)).getText().length() > 0)
                .map(hBox -> memberConstructor.apply(((TextField) hBox.getChildren().get(1)).getText()))
                .collect(Collectors.toList());
    }

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

    private void showConfirmation(boolean visualization){
        lbConfirmation.setVisible(visualization);
        btnNo.setVisible(visualization);
        btnYes.setVisible(visualization);
    }

    private void setSpacingVbox(){
        vbCurrentMembers.setSpacing(5);
        vbCurrentMembers.setAlignment(Pos.TOP_CENTER);
        vbNewMembers.setSpacing(5);
        vbNewMembers.setAlignment(Pos.TOP_CENTER);
    }

    private void clearInputFields(){
        showConfirmation(false);
        cboNewMember.getSelectionModel().clearSelection();
        vbNewMembers.getChildren().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cboNewMember.getItems().addAll(1,2,3,4,5,6,7);
        setSpacingVbox();
        showConfirmation(false);
        EventManager.getInstance().setEditMemberListEventHandler(event -> {
            onVboxCurrentMembers();
            cboNewMember.setOnAction(actionEvent -> onVboxNewMembers());
        });
    }
}