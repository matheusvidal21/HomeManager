package br.com.homemanager.controller;

import br.com.homemanager.event.EventManager;
import br.com.homemanager.model.Task;
import br.com.homemanager.model.Member;
import br.com.homemanager.application.Program;
import br.com.homemanager.model.Session;
import br.com.homemanager.model.enums.TaskStatus;
import br.com.homemanager.repository.HomeRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class HomePageController implements Initializable {
    @FXML
    private Button btnLogout;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private VBox vbteste;
    @FXML
    private VBox vbBtnMembers;
    @FXML
    private VBox vbAllTasks;
    @FXML
    private Button btnRedistributeDailyTasks;
    @FXML
    private Button btnRedistributeWeeklyTasks;
    @FXML
    private Label lbResult;
    @FXML
    private Label lbProgress;

    public void simulateProgress(){
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(Session.getInstance().getCurrentUser().getHomeDTasks());
        allTasks.addAll(Session.getInstance().getCurrentUser().getHomeWTasks());

        int totalTasks = allTasks.size();
        int tasksCompleted = (int) allTasks.stream().filter(task -> task.getTaskStatus() == TaskStatus.DONE).count();
        double progress = (double) tasksCompleted / totalTasks;

        lbProgress.setText(String.format("%.0f", progress * 100)   + "% tasks completed");
        progressBar.setProgress(progress);
    }

    public void onBtnLogoutCLick(){
        HomeRepository.saveUserData();
        Program.changeScreen("loginPage");
    }

    public void onBtnRedistributeDailyTasks(){
        displaySuccessMessage("Daily tasks redistributed");
        Session.getInstance().getCurrentUser().assignDailyTasks();
        HomeRepository.saveUserData();
    }

    public void onBtnRedistributeWeeklyTasks(){
        displaySuccessMessage("Weekly tasks redistributed");
        Session.getInstance().getCurrentUser().assignWeeklyTasks();
        HomeRepository.saveUserData();
    }

    public void showAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(Session.getInstance().getCurrentUser().getHomeWTasks());
        allTasks.addAll(Session.getInstance().getCurrentUser().getHomeDTasks());

        vbAllTasks.getChildren().clear();

        // Adiciona novos campos TextField conforme a quantidade de tarefas
        for (Task task : allTasks) {
            Label label = new Label(task.getTaskName());
            label.getStyleClass().add("label-tasks");
            Separator separator = new Separator();

            vbAllTasks.getChildren().addAll(label, separator);
        }
        vbAllTasks.setAlignment(Pos.TOP_CENTER);
        vbAllTasks.setSpacing(2);
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

    private void displaySuccessMessage(String message) {
        lbResult.setText(message);
        lbResult.setStyle("-fx-text-fill: green;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventManager.getInstance().setUpdateProgressEventHandler(event -> {
            simulateProgress();
        });
    }
}
