package br.com.homemanager.controller;

import br.com.homemanager.event.*;
import br.com.homemanager.model.Home;
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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controlador responsável pela página principal (Home Page) do aplicativo.
 * Gerencia as interações do usuário na página inicial, exibindo informações sobre as tarefas, progresso e membros.
 */
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
    private Button btnEditTaskList;
    @FXML
    private Button btnEditMemberList;
    @FXML
    private ScrollPane scrollPaneButtonMember;
    @FXML
    private Label lbResult;
    @FXML
    private Label lbProgress;

    /**
     * Inicializa a barra de progresso simulando o progresso das tarefas da casa concluídas.
     */
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

    /**
     * Realiza o logout do usuário e retorna para a tela de login.
     */
    public void onBtnLogoutCLick(){
        lbResult.setText("");
        HomeRepository.saveUserData();
        Program.changeScreen("loginPage");
    }

    /**
     * Redistribui as tarefas diárias entre os membros e salva as alterações.
     */
    public void onBtnRedistributeDailyTasks(){
        displaySuccessMessage("Daily tasks redistributed");
        Session.getInstance().getCurrentUser().assignDailyTasks();
        HomeRepository.saveUserData();
    }

    /**
     * Redistribui as tarefas semanais entre os membros e salva as alterações.
     */
    public void onBtnRedistributeWeeklyTasks(){
        displaySuccessMessage("Weekly tasks redistributed");
        Session.getInstance().getCurrentUser().assignWeeklyTasks();
        HomeRepository.saveUserData();
    }

    /**
     * Altera para a tela de edição da lista de tarefas e salva as alterações.
     */
    public void onBtnEditTaskList(){
        EventManager.getInstance().fireEditTaskListEvent(new EditTaskListEvent());
        lbResult.setText("");
        HomeRepository.saveUserData();
        Program.changeScreen("editTaskListPage");
    }

    /**
     * Altera para a tela de edição da lista de membros e salva as alterações.
     */
    public void onBtnEditMemberList(){
        EventManager.getInstance().fireEditMemberListEvent(new EditMemberListEvent());
        lbResult.setText("");
        HomeRepository.saveUserData();
        Program.changeScreen("editMemberListPage");
    }

    /**
     * Reinicia a semana, limpando as tarefas dos membros e altera o progresso para o status inicial.
     */
    public void onBtnRestartWeek(){
        Home currentUser = Session.getInstance().getCurrentUser();

        currentUser.getMembersList().forEach(member -> {
            member.getDailyTasks().clear();
            member.getWeeklyTasks().clear();
        });

        currentUser.getHomeWTasks().forEach(weeklyTask -> weeklyTask.setTaskStatus(TaskStatus.NOT_DONE));
        currentUser.getHomeDTasks().forEach(dailyTask -> {
            dailyTask.setTaskStatus(TaskStatus.NOT_DONE);
            dailyTask.setProgress(new AtomicInteger(0));
        });

        EventManager.getInstance().fireHomeEvent(new UpdateHomeProgressEvent());
        HomeRepository.saveUserData();
        displaySuccessMessage("Week restarted");
    }

    /**
     * Exibe todas as tarefas na tela, mostrando seus nomes.
     */
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
     * Manipula o clique nos botões de membros, atualiza o progresso e carrega a página do membro correspondente.
     *
     * @param member O membro do qual o botão foi clicado.
     */
    private void handleMemberButtonClick(Member member) {
        lbResult.setText("");
        EventManager.getInstance().fireProgressEvent(new UpdateProgressEvent());
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
     * Exibe uma mensagem de sucesso com um texto especificado.
     *
     * @param message O texto da mensagem de sucesso a ser exibido.
     */
    private void displaySuccessMessage(String message) {
        lbResult.setText(message);
        lbResult.setStyle("-fx-text-fill: green;");
    }

    /**
     * Método de inicialização que configura os manipuladores de eventos para atualizar o progresso e exibir tarefas e membros.
     *
     * @param url            A URL do recurso a ser inicializado.
     * @param resourceBundle O ResourceBundle para localizar o recurso.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventManager.getInstance().setUpdateHomeProgressEventHandler(event -> {
            simulateProgress();
        });
        EventManager.getInstance().setShowAllTaskEventHandler(event-> {
            showAllTasks();
        });
        EventManager.getInstance().setShowMemberButtonsEventHandler(event -> {
            addMembersButtons();
        });
    }
}
