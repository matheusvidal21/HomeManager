package br.com.homemanager.controller;

import br.com.homemanager.model.Task;
import br.com.homemanager.application.Program;
import br.com.homemanager.model.DailyTask;
import br.com.homemanager.model.Home;
import br.com.homemanager.model.Session;
import br.com.homemanager.model.WeeklyTask;
import br.com.homemanager.repository.HomeRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskChooserController implements Initializable {
    @FXML
    private VBox vbdailyTasks;
    @FXML
    private VBox vbWeeklyTasks;
    @FXML
    private CheckBox cbSpecialDTask;
    @FXML
    private TextField txtSpecialDTask;
    @FXML
    private CheckBox cbSpecialWTask;
    @FXML
    private TextField txtSpecialWTask;
    @FXML
    private Button btnConcluir;

    public void onBtnConcluirClick(){
        // Home currentUser = Session.getInstance().getCurrentUser();
        Session.getInstance().getCurrentUser().addAllDailyTasks(getSelectedTasks(vbdailyTasks, DailyTask::new));
        Session.getInstance().getCurrentUser().addAllWeeklyTasks(getSelectedTasks(vbWeeklyTasks, WeeklyTask::new));
        getSpecialDTask();
        getSpecialWTask();
        Session.getInstance().getCurrentUser().printHomeTasks();
        Program.changeScreen("loginPage");
    }

    public void getSpecialDTask(){
        Home currentUser = Session.getInstance().getCurrentUser();
        if(cbSpecialDTask.isSelected() && !txtSpecialDTask.getText().isEmpty()){
            currentUser.addDailyTask(new DailyTask(txtSpecialDTask.getText()));
        }
    }

    public void getSpecialWTask(){
        Home currentUser = Session.getInstance().getCurrentUser();
        if(cbSpecialWTask.isSelected() && !txtSpecialWTask.getText().isEmpty()){
            currentUser.addWeeklyTask(new WeeklyTask(txtSpecialWTask.getText()));
        }
    }

    private void showDailyTasks(){
        for(DailyTask dailyTask : HomeRepository.defaultDailyTasks()){
            CheckBox checkBox = new CheckBox();
            Label label = new Label(dailyTask.getTaskName());
            checkBox.getStyleClass().add("custom-checkbox");
            label.getStyleClass().add("label-tasks");
            HBox hBox = new HBox(checkBox, label);
            hBox.setSpacing(7);
            vbdailyTasks.getChildren().add(hBox);
        }
        vbdailyTasks.setAlignment(Pos.TOP_CENTER);
        vbdailyTasks.setSpacing(5);
    }

    private void showWeeklyTasks(){
        for(WeeklyTask weeklyTask : HomeRepository.defaultWeeklyTasks()){
            CheckBox checkBox = new CheckBox();
            Label label = new Label(weeklyTask.getTaskName());
            checkBox.getStyleClass().add("custom-checkbox");
            label.getStyleClass().add("label-tasks");
            HBox hBox = new HBox(checkBox, label);
            hBox.setSpacing(7);
            vbWeeklyTasks.getChildren().add(hBox);
        }
        vbWeeklyTasks.setAlignment(Pos.TOP_CENTER);
        vbWeeklyTasks.setSpacing(5);
    }

    private <T extends Task> List<T> getSelectedTasks(VBox vbTasks, Function<String, T> taskConstructor) {
        return vbTasks.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox) hBox.getChildren().get(0)).isSelected())
                .map(hBox -> taskConstructor.apply(((Label) hBox.getChildren().get(1)).getText()))
                .collect(Collectors.toList());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showDailyTasks();
        showWeeklyTasks();
    }
}