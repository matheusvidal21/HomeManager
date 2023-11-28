package br.com.homemanager.controller;

import br.com.homemanager.application.Program;
import br.com.homemanager.event.EventManager;
import br.com.homemanager.event.ShowAllTaskEvent;
import br.com.homemanager.event.UpdateHomeProgressEvent;
import br.com.homemanager.event.UpdateProgressEvent;
import br.com.homemanager.model.*;
import br.com.homemanager.repository.HomeRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EditTaskListController implements Initializable {
    @FXML
    private Button btnFinish;
    @FXML
    private Button btnHome;
    @FXML
    private ComboBox<Integer> cboDailyTasks;
    @FXML
    private ComboBox<Integer> cboWeeklyTasks;
    @FXML
    private ScrollPane scrollPaneDailyTasks;
    @FXML
    private ScrollPane scrollPaneWeeklyTasks;
    @FXML
    private VBox vbDailyTasks;
    @FXML
    private VBox vbWeeklyTasks;
    @FXML
    private Label lbConfirmation;
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    public void onBtnHomeClick(){
        clearInputFields();
        Program.changeScreen("homePage");
    }

    public void onBtnFinishClick(){
        lbConfirmation.setVisible(true);
        btnNo.setVisible(true);
        btnYes.setVisible(true);

        btnYes.setOnAction(event -> saveNewTaskList());
        btnNo.setOnAction(event -> onBtnHomeClick());
    }

    private void saveNewTaskList(){
        Home currentUser = Session.getInstance().getCurrentUser();
        List<Member> memberList = currentUser.getMembersList();

        currentUser.getHomeDTasks().clear();
        currentUser.getHomeWTasks().clear();
        memberList.forEach(member -> member.getDailyTasks().clear());
        memberList.forEach(member -> member.getWeeklyTasks().clear());

        currentUser.addAllDailyTasks(getSelectedTasks(vbDailyTasks, DailyTask::new));
        currentUser.addAllWeeklyTasks(getSelectedTasks(vbWeeklyTasks, WeeklyTask::new));
        currentUser.addAllDailyTasks(getSelectedNewTasks(vbDailyTasks, DailyTask::new));
        currentUser.addAllWeeklyTasks(getSelectedNewTasks(vbWeeklyTasks, WeeklyTask::new));

        clearInputFields();
        EventManager.getInstance().fireProgressEvent(new UpdateProgressEvent());
        EventManager.getInstance().fireShowAllTaksEvent(new ShowAllTaskEvent());
        EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
        HomeRepository.saveUserData();
        Program.changeScreen("homePage");
    }

    private <T extends Task> List<T> getSelectedTasks(VBox vbTasks, Function<String, T> taskConstructor){
        return vbTasks.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox)hBox.getChildren().get(0)).isSelected() && hBox.getChildren().get(1) instanceof Label)
                .map(hBox -> taskConstructor.apply(((Label) hBox.getChildren().get(1)).getText()))
                .collect(Collectors.toList());
    }

    private <T extends Task> List<T> getNoSelectedTasks(VBox vbTasks, Function<String, T> taskConstructor){
        return vbTasks.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> !((CheckBox)hBox.getChildren().get(0)).isSelected() && hBox.getChildren().get(1) instanceof Label)
                .map(hBox -> taskConstructor.apply(((Label) hBox.getChildren().get(1)).getText()))
                .collect(Collectors.toList());
    }

    private <T extends Task> List<T> getSelectedNewTasks(VBox vbTasks, Function<String, T> taskConstructor){
        return vbTasks.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox)hBox.getChildren().get(0)).isSelected() && hBox.getChildren().get(1) instanceof TextField)
                .map(hBox -> taskConstructor.apply((((TextField) hBox.getChildren().get(1)).getText())))
                .collect(Collectors.toList());
    }

    public void showDailyTasks(){
        vbDailyTasks.setAlignment(Pos.TOP_CENTER);
        vbDailyTasks.setSpacing(5);
        vbDailyTasks.getChildren().clear();

        List<DailyTask> allTasks = new ArrayList<>();
        allTasks.addAll(Session.getInstance().getCurrentUser().getHomeDTasks());

        for(DailyTask task : allTasks){
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);

            Label label = new Label(task.getTaskName());
            label.getStyleClass().add("label-tasks");

            HBox hBox = new HBox(checkBox, label);
            hBox.setSpacing(7);
            vbDailyTasks.getChildren().add(hBox);
        }
    }

    public void onVboxDailyTasks(){
        if(cboDailyTasks.getValue() != null){
            int numbersOfTasks = cboDailyTasks.getValue();
            int indexLastTask = Session.getInstance().getCurrentUser().getHomeDTasks().size();
            vbDailyTasks.getChildren().remove(indexLastTask, vbDailyTasks.getChildren().size());

            for(int i = 0; i < numbersOfTasks; i++){
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(true);
                TextField textFieldTask = new TextField();

                HBox hbox = new HBox(checkBox, textFieldTask);
                hbox.setSpacing(7);
                vbDailyTasks.getChildren().add(hbox);
            }
        }
    }

    public void showWeeklyTasks(){
        vbWeeklyTasks.setAlignment(Pos.TOP_CENTER);
        vbWeeklyTasks.setSpacing(5);
        vbWeeklyTasks.getChildren().clear();

        List<WeeklyTask> allTasks = new ArrayList<>();
        allTasks.addAll(Session.getInstance().getCurrentUser().getHomeWTasks());

        for(WeeklyTask task : allTasks){
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);

            Label label = new Label(task.getTaskName());
            label.getStyleClass().add("label-tasks");

            HBox hBox = new HBox(checkBox, label);
            hBox.setSpacing(7);
            vbWeeklyTasks.getChildren().add(hBox);
        }
    }

    public void onVboxWeeklyTasks(){
        if(cboWeeklyTasks.getValue() != null){
            int numberOfTasks = cboWeeklyTasks.getValue();
            int indexLasTask = Session.getInstance().getCurrentUser().getHomeWTasks().size();
            vbWeeklyTasks.getChildren().remove(indexLasTask, vbWeeklyTasks.getChildren().size());

            for(int i = 0; i < numberOfTasks; i++){
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(true);
                TextField textFieldTask = new TextField();

                HBox hbox = new HBox(checkBox, textFieldTask);
                hbox.setSpacing(7);
                vbWeeklyTasks.getChildren().add(hbox);
            }
        }
    }

    public void clearInputFields(){
        cboWeeklyTasks.getSelectionModel().clearSelection();
        cboDailyTasks.getSelectionModel().clearSelection();
        vbDailyTasks.getChildren().clear();
        vbWeeklyTasks.getChildren().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventManager.getInstance().setEditTaskListEventHandler(event -> {
            lbConfirmation.setVisible(false);
            btnNo.setVisible(false);
            btnYes.setVisible(false);
            cboDailyTasks.getItems().addAll(1,2,3,4,5,6,7,8);
            cboWeeklyTasks.getItems().addAll(1,2,3,4,5,6,7,8);
            showDailyTasks();
            showWeeklyTasks();
            cboDailyTasks.setOnAction(actionEvent -> onVboxDailyTasks());
            cboWeeklyTasks.setOnAction(actionEvent -> onVboxWeeklyTasks());
        });
    }

}
