package br.com.homemanager.controller;

import br.com.homemanager.model.Member;
import br.com.homemanager.application.Program;
import br.com.homemanager.model.DailyTask;
import br.com.homemanager.model.Session;
import br.com.homemanager.model.WeeklyTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        Program.changeScreen("loginPage");
    }

    public void onBtnHomeClick(ActionEvent event){
        Program.changeScreen("homePage");
    }

    public void showMemberHello(Member member){
        lbHello.setText("Hello, " + member.getName() + "!");
    }

    public void showMemberDailyTasks(Member member){
        List<DailyTask> dailyTasks = member.getDailyTasks();
        hbDailyTasks.getChildren().clear();

        for(DailyTask dailytask : dailyTasks){
            Label label = new Label(dailytask.getTaskName());
            label.getStyleClass().add("label-tasks");
            label.setWrapText(true);
            label.setMaxWidth(150);

            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getStyleClass().add("daily-task-anchor");
            anchorPane.setMaxWidth(150);

            // Definindo as âncoras do Label dentro do AnchorPane
            AnchorPane.setTopAnchor(label, 15.0);
            AnchorPane.setLeftAnchor(label, 5.0);
            AnchorPane.setRightAnchor(label, 5.0);

            anchorPane.getChildren().add(label);
            hbDailyTasks.getChildren().add(anchorPane);
        }
        hbDailyTasks.setSpacing(10);
    }

    public void showMemberWeeklyTasks(Member member){
        List<WeeklyTask> weeklyTasks = member.getWeeklyTasks();
        hbWeeklyTasks.getChildren().clear();

        for(WeeklyTask weeklytask : weeklyTasks){
            Label label = new Label(weeklytask.getTaskName());
            label.getStyleClass().add("label-tasks");
            label.setWrapText(true);
            label.setMaxWidth(150);

            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getStyleClass().add("weekly-task-anchor");
            anchorPane.setMaxWidth(150);

            // Definindo as âncoras do Label dentro do AnchorPane
            AnchorPane.setTopAnchor(label, 15.0);
            AnchorPane.setLeftAnchor(label, 5.0);
            AnchorPane.setRightAnchor(label, 5.0);

            anchorPane.getChildren().add(label);
            hbWeeklyTasks.getChildren().add(anchorPane);
        }
        hbWeeklyTasks.setSpacing(10);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("member-page.fxml"));
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

