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
    private Button btnYes;
    @FXML
    private Button btnNo;
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

        clearAllTasks(currentUser);
        addSelectedTasks(currentUser);
        saveAndNotifyChanges(currentUser);

        Program.changeScreen("homePage");
    }

    private void clearAllTasks(Home currentUser){
        currentUser.getHomeDTasks().clear();
        currentUser.getHomeWTasks().clear();
        currentUser.getMembersList().forEach(member -> {
            member.getDailyTasks().clear();
            member.getWeeklyTasks().clear();
        });
    }

    private void addSelectedTasks(Home currentUser){
        currentUser.addAllDailyTasks(getSelectedTasks(vbDailyTasks, DailyTask::new));
        currentUser.addAllWeeklyTasks(getSelectedTasks(vbWeeklyTasks, WeeklyTask::new));
        currentUser.addAllDailyTasks(getSelectedNewTasks(vbDailyTasks, DailyTask::new));
        currentUser.addAllWeeklyTasks(getSelectedNewTasks(vbWeeklyTasks, WeeklyTask::new));
    }

    private void saveAndNotifyChanges(Home currentUser){
        clearInputFields();
        EventManager.getInstance().fireProgressEvent(new UpdateProgressEvent());
        EventManager.getInstance().fireShowAllTaksEvent(new ShowAllTaskEvent());
        EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
        HomeRepository.saveUserData();
    }

    private <T extends Task> List<T> getSelectedTasks(VBox vbTasks, Function<String, T> taskConstructor){
        return vbTasks.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox)hBox.getChildren().get(0)).isSelected() && hBox.getChildren().get(1) instanceof Label)
                .map(hBox -> taskConstructor.apply(((Label) hBox.getChildren().get(1)).getText()))
                .collect(Collectors.toList());
    }

    private <T extends Task> List<T> getSelectedNewTasks(VBox vbTasks, Function<String, T> taskConstructor){
        return vbTasks.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .filter(hBox -> ((CheckBox)hBox.getChildren().get(0)).isSelected() && hBox.getChildren().get(1) instanceof TextField)
                .filter(hBox -> ((TextField) hBox.getChildren().get(1)).getLength() > 0)
                .map(hBox -> taskConstructor.apply((((TextField) hBox.getChildren().get(1)).getText())))
                .collect(Collectors.toList());
    }

    private void showTasks(VBox container, List<? extends Task> tasks) {
        container.setAlignment(Pos.TOP_CENTER);
        container.setSpacing(5);
        container.getChildren().clear();

        for (Task task : tasks) {
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            checkBox.getStyleClass().add("custom-checkbox");

            Label label = new Label(task.getTaskName());
            label.getStyleClass().add("label-tasks");

            HBox hBox = new HBox(checkBox, label);
            hBox.setSpacing(7);
            container.getChildren().add(hBox);
        }
    }

    public void showDailyTasks() {
        List<DailyTask> allTasks = new ArrayList<>(Session.getInstance().getCurrentUser().getHomeDTasks());
        showTasks(vbDailyTasks, allTasks);
    }

    public void showWeeklyTasks() {
        List<WeeklyTask> allTasks = new ArrayList<>(Session.getInstance().getCurrentUser().getHomeWTasks());
        showTasks(vbWeeklyTasks, allTasks);
    }

    private void onVboxTasks(VBox container, ComboBox<Integer> comboBox, List<? extends Task> tasks) {
        if (comboBox.getValue() != null) {
            int numberOfTasks = comboBox.getValue();
            int lastIndex = tasks.size();
            container.getChildren().remove(lastIndex, container.getChildren().size());

            for (int i = 0; i < numberOfTasks; i++) {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(true);
                checkBox.getStyleClass().add("custom-checkbox");
                TextField textFieldTask = new TextField();
                textFieldTask.getStyleClass().add("text-field-no-border");

                HBox hbox = new HBox(checkBox, textFieldTask);
                hbox.setSpacing(7);
                container.getChildren().add(hbox);
            }
        }
    }

    public void onVboxDailyTasks() {
        onVboxTasks(vbDailyTasks, cboDailyTasks, Session.getInstance().getCurrentUser().getHomeDTasks());
    }

    public void onVboxWeeklyTasks() {
        onVboxTasks(vbWeeklyTasks, cboWeeklyTasks, Session.getInstance().getCurrentUser().getHomeWTasks());
    }


    public void clearInputFields(){
        cboWeeklyTasks.getSelectionModel().clearSelection();
        cboDailyTasks.getSelectionModel().clearSelection();
        vbDailyTasks.getChildren().clear();
        vbWeeklyTasks.getChildren().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cboDailyTasks.getItems().addAll(1,2,3,4,5,6,7,8);
        cboWeeklyTasks.getItems().addAll(1,2,3,4,5,6,7,8);
        EventManager.getInstance().setEditTaskListEventHandler(event -> {
            lbConfirmation.setVisible(false);
            btnNo.setVisible(false);
            btnYes.setVisible(false);
            showDailyTasks();
            showWeeklyTasks();
            cboDailyTasks.setOnAction(actionEvent -> onVboxDailyTasks());
            cboWeeklyTasks.setOnAction(actionEvent -> onVboxWeeklyTasks());
        });
    }
}