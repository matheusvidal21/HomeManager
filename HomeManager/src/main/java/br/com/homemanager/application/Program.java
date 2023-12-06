package br.com.homemanager.application;

import br.com.homemanager.controller.HomePageController;
import br.com.homemanager.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principal que inicia a aplicação e gerencia as transições entre telas (cenas).
 */
public class Program extends Application {
    private static Stage stage;
    // Scene = armazenar telas
    private static Scene loginScene;
    private static Scene singupScene;
    private static Scene taskChooserScene;
    private static Scene homeScene;
    private static Scene editTaskListScene;
    private static Scene editMemberListScene;

    @Override
    public void start(Stage stage) throws Exception {
        Program.stage = stage;
        stage.setTitle("HomeManager");

        // Carregar o FXML do LoginController
        FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/br/com/homemanager/login.fxml"));
        Parent rootLogin = loaderLogin.load();
        LoginController loginController = loaderLogin.getController();

        // Carregar o FXML do HomePageController
        FXMLLoader loaderHomePage = new FXMLLoader(getClass().getResource("/br/com/homemanager/home-page.fxml"));
        Parent rootHomePage = loaderHomePage.load();
        HomePageController homePageController = loaderHomePage.getController();

        loginController.setHomePageController(homePageController);

        // Configurar as cenas
        loginScene = new Scene(rootLogin, 915, 585);
        homeScene = new Scene(rootHomePage, 915, 585);

        Parent fxmlRegisterPage = FXMLLoader.load(getClass().getResource("/br/com/homemanager/singup.fxml"));
        singupScene = new Scene(fxmlRegisterPage, 915, 585);

        Parent fxmlTaskChooserPage = FXMLLoader.load(getClass().getResource("/br/com/homemanager/task-chooser.fxml"));
        taskChooserScene = new Scene(fxmlTaskChooserPage, 915, 585);

        Parent fxmlEditTaskListPage = FXMLLoader.load(getClass().getResource("/br/com/homemanager/edit-task-list.fxml"));
        editTaskListScene= new Scene(fxmlEditTaskListPage , 915, 585);

        Parent fxmlEditMemberListPage = FXMLLoader.load(getClass().getResource("/br/com/homemanager/edit-member-list.fxml"));
        editMemberListScene= new Scene(fxmlEditMemberListPage , 915, 585);

        stage.setScene(loginScene);
        stage.show();
    }

    /**
     * Método para mudar a tela (cena) exibida.
     *
     * @param scene Nome da tela (cena) para ser exibida.
     *              Opções: "loginPage", "singupPage", "taskChooserPage", "homePage", "editTaskListPage", "editMemberListPage".
     * @throws RuntimeException Se a tela solicitada não existir.
     */
    public static void changeScreen(String scene){
        switch (scene){
            case "loginPage":{
                stage.setScene(loginScene);
                break;
            }
            case "singupPage":{
                stage.setScene(singupScene);
                break;
            }
            case "taskChooserPage":{
                stage.setScene(taskChooserScene);
                break;
            }
            case "homePage":{
                stage.setScene(homeScene);
                break;
            }
            case "editTaskListPage":{
                stage.setScene(editTaskListScene);
                break;
            }
            case "editMemberListPage":{
                stage.setScene(editMemberListScene);
                break;
            }
            default:
                throw new RuntimeException("Tela não existe!");
        }
    }

    /**
     * Método principal que inicia a aplicação.
     *
     * @param args Os argumentos da linha de comando.
     */
    public static void main(String[] args) {
        launch();
    }
}