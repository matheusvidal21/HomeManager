package br.com.homemanager.controller;

import br.com.homemanager.event.EventManager;
import br.com.homemanager.event.UpdateHomeProgressEvent;
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

/**
 * Controlador responsável pela página de um membro, exibindo informações sobre suas tarefas diárias e semanais.
 * Permite a interação do membro com suas tarefas, permitindo marcar as tarefas como concluídas e acompanhar seu progresso.
 */
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

    /**
     * Simula o progresso do membro exibindo na barra de progresso o status das tarefas realizadas.
     *
     * @param member O membro para o qual o progresso está sendo simulado.
     */
    public void simulateProgressBar(Member member){
        clearCongratulationsLabel();
        lbProgress.setText("You don't have any tasks yet");

        if(member.getDailyTasks().size() > 0 || member.getWeeklyTasks().size() > 0) {
            int tasksCompleted = countCompletedTasks(member);
            int totalTasks = member.getWeeklyTasks().size() + (member.getDailyTasks().size() * 5);
            double progress = (double) tasksCompleted / totalTasks;

            lbProgress.setText(String.format("%.0f", progress * 100)   + "% tasks completed");

            if(tasksCompleted == totalTasks){
                showCongratulationsMessage();
            }
            memberProgressBar.setProgress(progress);
        }
    }

    /**
     * Conta o número de tarefas concluídas pelo membro, incluindo tarefas diárias e semanais.
     *
     * @param member O membro para o qual as tarefas concluídas estão sendo contadas.
     * @return O número total de tarefas concluídas pelo membro.
     */
    private int countCompletedTasks(Member member){
        int tasksCompleted = 0;
        for (Task task : member.getWeeklyTasks()) {
            if (task.getTaskStatus() == TaskStatus.DONE) {
                tasksCompleted++;
            }
        }

        for (DailyTask dailyTask : member.getDailyTasks()) {
            tasksCompleted += dailyTask.getProgress().get();
        }
        return tasksCompleted;
    }

    /**
     * Limpa o rótulo de congratulações.
     */
    public void clearCongratulationsLabel(){
        lbCongratulations.getStyleClass().clear();
        lbCongratulations.setText("");
    }

    /**
     * Exibe uma mensagem de congratulações quando todas as tarefas do membro foram concluídas.
     */
    private void showCongratulationsMessage() {
        lbCongratulations.getStyleClass().add("label-result-styled");
        lbCongratulations.setText("Congratulations! You have completed all your tasks.");
    }

    /**
     * Realiza o logout do membro e retorna à tela de login.
     */
    public void onBtnLogoutCLick(){
        HomeRepository.saveUserData();
        Program.changeScreen("loginPage");
    }


    /**
     * Redireciona o membro para a tela inicial.
     *
     * @param event O evento de clique do botão.
     */
    public void onBtnHomeClick(ActionEvent event){
        EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
        HomeRepository.saveUserData();
        Program.changeScreen("homePage");
    }

    /**
     * Exibe uma saudação personalizada para o membro.
     *
     * @param member O membro para o qual a saudação está sendo exibida.
     */
    public void showMemberHello(Member member){
        double textSize;

        if(member.getName().length() < 4){
            textSize = 190.0;
        }else if (member.getName().length() < 9){
            textSize = 300.0;
        }else{
            textSize = 390.0;
        }

        lbHello.setPrefSize(textSize, 53);
        lbHello.setText("Hello, " + member.getName() + "!");
    }

    /**
     * Exibe as tarefas do membro na interface gráfica.
     *
     * @param tasks       A lista de tarefas do membro.
     * @param container   O contêiner que exibirá as tarefas.
     * @param anchorStyle O estilo a ser aplicado ao contêiner.
     */
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

                AnchorPane.setTopAnchor(checkBox, 90.0);
                AnchorPane.setTopAnchor(labelProgress, 112.0);
                AnchorPane.setLeftAnchor(labelProgress, 35.0);
                AnchorPane.setRightAnchor(labelProgress, 15.0);

                checkBoxAction((DailyTask) task, progress, labelProgress, isSelected, checkBox);
            } else if (task instanceof WeeklyTask) {
                CheckBox checkBox = (CheckBox) ((AnchorPane) taskNode).getChildren().get(1);
                checkBoxAction((WeeklyTask) task, checkBox);
            }
        }
        container.setSpacing(10);
    }

    /**
     * Cria um rótulo de progresso para exibir o andamento das tarefas diárias.
     *
     * @param progress O progresso das tarefas diárias.
     * @return O rótulo de progresso criado.
     */
    private Label createProgressLabel(AtomicInteger progress) {
        Label labelProgress = new Label(progress.get() + "/5");
        labelProgress.getStyleClass().add("label-daily-progress");
        return labelProgress;
    }

    /**
     * Cria um nó para representar uma tarefa do membro na interface gráfica.
     *
     * @param task        A tarefa do membro.
     * @param anchorStyle O estilo a ser aplicado ao contêiner.
     * @return O nó criado para representar a tarefa.
     */
    private Node createTaskNode(Task task, String anchorStyle) {
        Label label = new Label(task.getTaskName());
        CheckBox checkBox = new CheckBox();
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add(anchorStyle);

        styleAnchor(anchorPane, label, checkBox);

        anchorPane.getChildren().addAll(label, checkBox);
        return anchorPane;
    }

    /**
     * Manipula a ação do CheckBox para tarefas diárias, atualizando o progresso e o status da tarefa.
     *
     * @param dailyTask      A tarefa diária.
     * @param progress       O progresso das tarefas diárias.
     * @param labelProgress  O rótulo de progresso.
     * @param isSelected     Um array que indica se a tarefa foi selecionada.
     * @param checkBox       O CheckBox da tarefa diária.
     */
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
            EventManager.getInstance().fireProgressEvent(new UpdateProgressEvent());
            HomeRepository.saveUserData();
        });
    }

    /**
     * Simula o progresso da tarefa diária, desabilitando temporariamente o CheckBox.
     *
     * @param checkBox   O CheckBox da tarefa diária.
     * @param isSelected Um booleano que indica se a tarefa foi selecionada.
     */
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

    /**
     * Manipula a ação do CheckBox para tarefas semanais, atualizando o status da tarefa.
     *
     * @param weeklyTask A tarefa semanal.
     * @param checkBox   O CheckBox da tarefa semanal.
     */
    private void checkBoxAction(WeeklyTask weeklyTask, CheckBox checkBox) {
        checkBox.setSelected(weeklyTask.getTaskStatus() == TaskStatus.DONE);
        checkBox.setOnAction(event -> {
            if(checkBox.isSelected()){
                weeklyTask.setTaskStatus(TaskStatus.DONE);
            }else{
                weeklyTask.setTaskStatus(TaskStatus.NOT_DONE);
            }
            EventManager.getInstance().fireProgressEvent(new UpdateProgressEvent());
            HomeRepository.saveUserData();
        });
    }

    /**
     * Aplica estilos aos elementos gráficos das tarefas.
     *
     * @param anchorPane O contêiner das tarefas.
     * @param label      O rótulo da tarefa.
     * @param checkBox   O CheckBox da tarefa.
     */
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

        AnchorPane.setTopAnchor(checkBox, 90.0);
        AnchorPane.setLeftAnchor(checkBox, 37.0);
        AnchorPane.setRightAnchor(checkBox, 15.0);
    }

    /**
     * Configura as informações do membro na tela.
     *
     * @param member O membro para o qual as informações estão sendo configuradas.
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

    }

    /**
     * Adiciona botões de membros da casa na tela.
     */
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

    /**
     * Manipula o clique nos botões de membros, carregando a página do membro correspondente.
     *
     * @param member O membro para o qual a página está sendo carregada.
     */
    private void handleMemberButtonClick(Member member) {
        loadMemberPageScene(member);
    }

    /**
     * Carrega a cena da página do membro correspondente ao membro selecionado.
     *
     * @param member O membro para o qual a página está sendo carregada.
     */
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

    /**
     * Método de inicialização da classe.
     *
     * @param url            A URL do recurso a ser inicializado.
     * @param resourceBundle O ResourceBundle para localizar o recurso.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}

