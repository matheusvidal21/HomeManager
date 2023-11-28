package br.com.homemanager.controller;

import br.com.homemanager.event.EventManager;
import br.com.homemanager.event.UpdateProgressEvent;
import br.com.homemanager.model.*;
import br.com.homemanager.application.Program;
import br.com.homemanager.model.enums.TaskStatus;
import br.com.homemanager.repository.HomeRepository;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

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
    @FXML
    private ProgressBar memberProgressBar;
    @FXML
    private Label lbProgress;
    @FXML
    private Label lbCongratulations;
    public void simulateProgressBar(Member member){
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(member.getWeeklyTasks());
        allTasks.addAll(member.getDailyTasks());
        lbCongratulations.setText("");
        lbCongratulations.getStyleClass().clear();

        int tasksCompleted = 0;
        int totalTasks = member.getWeeklyTasks().size() + (member.getDailyTasks().size() * 5);
        for(Task task : allTasks){
            if(task instanceof DailyTask){
                tasksCompleted += (int) ((DailyTask) task).getProgress().get();
            }else if(task.getTaskStatus() == TaskStatus.DONE){
                tasksCompleted++;
            }
        }

        double progress = (double) tasksCompleted / totalTasks;
        lbProgress.setText(String.format("%.0f", progress * 100)   + "% tasks completed");

        if(tasksCompleted == totalTasks){
            lbCongratulations.getStyleClass().add("label-styled");
            lbCongratulations.setText("Congratulations! You have completed all your tasks.");
        }

        memberProgressBar.setProgress(progress);
    }

    public void onBtnLogoutCLick(){
        HomeRepository.saveUserData();
        Program.changeScreen("loginPage");
    }

    public void onBtnHomeClick(ActionEvent event){
        EventManager.getInstance().fireEvent(new UpdateProgressEvent());
        Program.changeScreen("homePage");
    }

    public void showMemberHello(Member member){
        double textSize = 0.0;

        if(member.getName().length() < 4){
            textSize = 150.0;
        }else if (member.getName().length() < 9){
            textSize = 240.0;
        }else{
            textSize = 340.0;
        }

        lbHello.setPrefSize(textSize, 30);
        lbHello.setText("Hello, " + member.getName() + "!");
    }

    public void showMemberTasks(List<? extends Task> tasks, HBox container, String anchorStyle) {
        container.getChildren().clear();

        for (Task task : tasks) {
            Node taskNode = createTaskNode(task, anchorStyle);
            container.getChildren().add(taskNode);

            if (task instanceof DailyTask) {
                CheckBox checkBox = (CheckBox) ((AnchorPane) taskNode).getChildren().get(1);
                AtomicInteger progress = ((DailyTask) task).getProgress();
                Label labelProgress = createProgressLabel(progress);
                boolean[] isSelected = {false};

                ((AnchorPane) taskNode).getChildren().add(labelProgress);

                AnchorPane.setTopAnchor(checkBox, 70.0);
                AnchorPane.setTopAnchor(labelProgress, 95.0);
                AnchorPane.setLeftAnchor(labelProgress, 32.0);
                AnchorPane.setRightAnchor(labelProgress, 15.0);

                checkBoxAction((DailyTask) task, progress, labelProgress, isSelected, checkBox);
            } else if (task instanceof WeeklyTask) {
                CheckBox checkBox = (CheckBox) ((AnchorPane) taskNode).getChildren().get(1);
                checkBoxAction((WeeklyTask) task, checkBox);
            }
        }
        container.setSpacing(10);
    }
    private Label createProgressLabel(AtomicInteger progress) {
        Label labelProgress = new Label(progress.get() + "/5");
        labelProgress.getStyleClass().add("label-daily-progress");
        return labelProgress;
    }
    private Node createTaskNode(Task task, String anchorStyle) {
        Label label = new Label(task.getTaskName());
        CheckBox checkBox = new CheckBox();
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add(anchorStyle);

        styleAnchor(anchorPane, label, checkBox);

        anchorPane.getChildren().addAll(label, checkBox);
        return anchorPane;
    }

    private void checkBoxAction(DailyTask dailyTask, AtomicInteger progress, Label labelProgress, boolean[] isSelected, CheckBox checkBox) {
        checkBox.setSelected(dailyTask.getTaskStatus() == TaskStatus.DONE);
        checkBox.setOnAction(event -> {
            if(progress.get() == 5){
                progress.set(0);
                isSelected[0] = false;
                labelProgress.setText("0/5");
                dailyTask.setTaskStatus(TaskStatus.NOT_DONE);
            }else{
                progress.getAndIncrement();
                labelProgress.setText(progress.get() + "/5");
                if(progress.get() < 5){
                    simulateProgress(checkBox, isSelected[0]);
                    dailyTask.setTaskStatus(TaskStatus.NOT_DONE);
                }else{
                    isSelected[0] = true;
                    checkBox.setSelected(true);
                    dailyTask.setTaskStatus(TaskStatus.DONE);
                }
            }
            EventManager.getInstance().fireEvent(new UpdateProgressEvent());
            HomeRepository.saveUserData();
        });
    }

    private void simulateProgress(CheckBox checkBox, boolean isSelected){
        checkBox.setDisable(true);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    // Habilita o CheckBox após o delay
                    checkBox.setDisable(false);
                    if(!isSelected){
                        checkBox.setSelected(false);
                    }
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void checkBoxAction(WeeklyTask weeklyTask, CheckBox checkBox) {
        checkBox.setSelected(weeklyTask.getTaskStatus() == TaskStatus.DONE);
        checkBox.setOnAction(event -> {
            if(checkBox.isSelected()){
                weeklyTask.setTaskStatus(TaskStatus.DONE);
            }else{
                weeklyTask.setTaskStatus(TaskStatus.NOT_DONE);
            }
            EventManager.getInstance().fireEvent(new UpdateProgressEvent());
            HomeRepository.saveUserData();
        });
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


    /*
    public void showMemberDailyTasks(Member member){
        List<DailyTask> dailyTasks = member.getDailyTasks();
        hbDailyTasks.getChildren().clear();

        for(DailyTask dailytask : dailyTasks){
            AtomicInteger progress = dailytask.getProgress();
            final boolean[] isSelected = {false};

            Label label = new Label(dailytask.getTaskName());
            CheckBox checkBox = new CheckBox();
            AnchorPane anchorPane = new AnchorPane();
            Label labelProgress = createProgressLabel(progress);
            anchorPane.getStyleClass().add("daily-task-anchor");

            styleAnchor(anchorPane,label, checkBox);
            AnchorPane.setTopAnchor(checkBox, 70.0);
            AnchorPane.setTopAnchor(labelProgress, 95.0);
            AnchorPane.setLeftAnchor(labelProgress, 32.0);
            AnchorPane.setRightAnchor(labelProgress, 15.0);

            anchorPane.getChildren().addAll(label, checkBox, labelProgress);
            hbDailyTasks.getChildren().add(anchorPane);
            checkBoxAction(dailytask, progress, isSelected, labelProgress, checkBox);
        }
        hbDailyTasks.setSpacing(10);
    }

    private Label createProgressLabel(AtomicInteger progress) {
        Label labelProgress = new Label(progress.get() + "/5");
        labelProgress.getStyleClass().add("label-daily-progress");
        return labelProgress;
    }

    private void checkBoxAction(DailyTask dailyTask, AtomicInteger progress, boolean[] isSelected, Label labelProgress, CheckBox checkBox){
        // Se a tarefa já estiver sido feita, o checkBox já estará selecionado
        checkBox.setSelected(dailyTask.getTaskStatus() == TaskStatus.DONE);
        checkBox.setOnAction(event -> {
            if(progress.get() == 5){
                progress.set(0);
                isSelected[0] = false;
                labelProgress.setText("0/5");
                dailyTask.setTaskStatus(TaskStatus.NOT_DONE);
            }else{
                progress.getAndIncrement();
                labelProgress.setText(progress.get() + "/5");
                if(progress.get() < 5){
                    simulateProgress(checkBox, isSelected[0]);
                    dailyTask.setTaskStatus(TaskStatus.NOT_DONE);
                }else{
                    isSelected[0] = true;
                    checkBox.setSelected(true);
                    dailyTask.setTaskStatus(TaskStatus.DONE);
                }
            }
            HomeRepository.saveUserData();
        });
    }

    private void simulateProgress(CheckBox checkBox, boolean isSelected){
        checkBox.setDisable(true);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    // Habilita o CheckBox após o delay
                    checkBox.setDisable(false);
                    if(!isSelected){
                        checkBox.setSelected(false);
                    }
                })
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void showMemberWeeklyTasks(Member member){
        List<WeeklyTask> weeklyTasks = member.getWeeklyTasks();
        hbWeeklyTasks.getChildren().clear();

        for(WeeklyTask weeklytask : weeklyTasks){
            Label label = new Label(weeklytask.getTaskName());
            CheckBox checkBox = new CheckBox();
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getStyleClass().add("weekly-task-anchor");

            styleAnchor(anchorPane, label, checkBox);

            anchorPane.getChildren().addAll(label, checkBox);
            hbWeeklyTasks.getChildren().add(anchorPane);

            // Se a tarefa já estiver sido feita, o checkBox já estará selecionado
            checkBox.setSelected(weeklytask.getTaskStatus() == TaskStatus.DONE);

            checkBox.setOnAction(event -> {
                if(checkBox.isSelected()){
                    weeklytask.setTaskStatus(TaskStatus.DONE);
                }else{
                    weeklytask.setTaskStatus(TaskStatus.NOT_DONE);
                }
                HomeRepository.saveUserData();
            });
        }
        hbWeeklyTasks.setSpacing(10);
    }
*/

    public void setMemberInfo(Member member){
        addMembersButtons();
        showMemberHello(member);
        EventManager.getInstance().setUpdateProgressEventHandler(event -> {
            simulateProgressBar(member);
        });
        simulateProgressBar(member);
        showMemberTasks(member.getDailyTasks(), hbDailyTasks, "daily-task-anchor");
        showMemberTasks(member.getWeeklyTasks(), hbWeeklyTasks, "weekly-task-anchor");


        //showMemberDailyTasks(member);
        //showMemberWeeklyTasks(member);
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

