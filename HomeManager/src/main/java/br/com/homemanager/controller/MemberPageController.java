package br.com.homemanager.controller;

import br.com.homemanager.event.EventManager;
import br.com.homemanager.event.UpdateProgressEvent;
import br.com.homemanager.model.*;
import br.com.homemanager.application.Program;
import br.com.homemanager.model.enums.TaskStatus;
import br.com.homemanager.repository.HomeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MemberPageController implements Initializable {
    @FXML
    private Button btnLogout;
    @FXML
    private VBox vbBtnMembers;
    @FXML
    private Button btnHome;
    @FXML
    private Label lbHello;
    @FXML
    private HBox hbWeeklyTasks;
    @FXML
    private HBox hbDailyTasks;

    public void onBtnLogoutCLick(){
        HomeRepository.saveUserData();
        Program.changeScreen("loginPage");
    }

    public void onBtnHomeClick(ActionEvent event){
        EventManager.getInstance().fireEvent(new UpdateProgressEvent());
        Program.changeScreen("homePage");
    }

    public void showMemberHello(Member member){
        lbHello.setText("Hello, " + member.getName() + "!");
    }

    public void showTasks(List<? extends Task> tasks, HBox container, String anchorStyleClass){
        container.getChildren().clear();

        for(Task task : tasks){
            Label label = new Label(task.getTaskName());
            CheckBox checkBox = new CheckBox();
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getStyleClass().add(anchorStyleClass);

            styleAnchor(anchorPane, label, checkBox);

            anchorPane.getChildren().addAll(label, checkBox);
            container.getChildren().add(anchorPane);

            // Se a tarefa já estiver sido feita, o checkBox já estará selecionado
            checkBox.setSelected(task.getTaskStatus() == TaskStatus.DONE);

            checkBox.setOnAction(event -> {
                if(checkBox.isSelected()){
                    task.setTaskStatus(TaskStatus.DONE);
                }else{
                    task.setTaskStatus(TaskStatus.NOT_DONE);
                }
                HomeRepository.saveUserData();
            });
        }
        container.setSpacing(10);
    }

    public void showMemberDailyTasks(Member member){
        List<DailyTask> dailyTasks = member.getDailyTasks();
        showTasks(dailyTasks, hbDailyTasks, "daily-task-anchor");
    }

    public void showMemberWeeklyTasks(Member member){
        List<WeeklyTask> weeklyTasks = member.getWeeklyTasks();
        showTasks(weeklyTasks, hbWeeklyTasks, "weekly-task-anchor");
    }

    private void styleAnchor(AnchorPane anchorPane, Label label, CheckBox checkBox){
        // Estilizando label
        label.getStyleClass().add("label-tasks");
        label.setWrapText(true);
        label.setMaxWidth(150);

        // Estilizando checkBox
        checkBox.getStyleClass().add("custom-checkbox");

        // Estilizando anchorPane
        anchorPane.setMaxWidth(150);

        // Definindo as âncoras da Label e do CheckBox dentro do AnchorPane
        AnchorPane.setTopAnchor(label, 15.0);
        AnchorPane.setLeftAnchor(label, 5.0);
        AnchorPane.setRightAnchor(label, 5.0);

        AnchorPane.setTopAnchor(checkBox, 80.0);
        AnchorPane.setLeftAnchor(checkBox, 33.0);
        AnchorPane.setRightAnchor(checkBox, 15.0);
    }

    public void setMemberInfo(Member member){
        addMembersButtons();
        showMemberHello(member);
        showMemberDailyTasks(member);
        showMemberWeeklyTasks(member);
    }

    public void addMembersButtons() {
        List<Member> membersList = Session.getInstance().getCurrentUser().getMembersList();
        vbBtnMembers.getChildren().clear();

        // Adiciona novos campos TextField conforme a quantidade de membros
        for (Member member : membersList) {
            Button button = new Button(member.getName());
            button.getStyleClass().add("button-member");
            vbBtnMembers.getChildren().add(button);

            button.setOnAction(event -> handleMemberButtonClick(member));
        }
        vbBtnMembers.setAlignment(Pos.TOP_CENTER);
        vbBtnMembers.setSpacing(10);
    }

    private void handleMemberButtonClick(Member member) {
        loadMemberPageScene(member);
    }

    private void loadMemberPageScene(Member member) {
        try {
            // Carrega o arquivo FXML da cena da página do membro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/homemanager/member-page.fxml"));
            Parent root = loader.load();

            // Obtém o controlador da cena da página do membro
            MemberPageController memberPageController = loader.getController();
            // Configura as informações do membro no controlador da página do membro
            memberPageController.setMemberInfo(member);
            // Cria uma nova cena
            Scene memberPageScene = new Scene(root);
            // Obtém o palco atual
            Stage currentStage = (Stage) vbBtnMembers.getScene().getWindow();

            // Define a nova cena no palco atual
            currentStage.setScene(memberPageScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Lida com erros ao carregar a cena da página do membro
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

