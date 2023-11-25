package br.com.homemanager.application;

import br.com.homemanager.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {
    private static Stage stage;
    // Scene = armazenar telas
    private static Scene loginScene;
    private static Scene singupScene;
    private static Scene homeScene;

    @Override
    public void start(Stage stage) throws Exception {
        Program.stage = stage;
        stage.setTitle("HomeManager");

        // Carregar o FXML do LoginController
        FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/br/com/homemanager/login.fxml"));
        Parent rootLogin = loaderLogin.load();
        LoginController loginController = loaderLogin.getController();


        Parent fxmlRegisterPage = FXMLLoader.load(getClass().getResource("/application/homemanager/cadastro.fxml"));
        singupScene = new Scene(fxmlRegisterPage, 715, 485);


        // Configurar as cenas
        loginScene = new Scene(rootLogin, 715, 485);

        stage.setScene(loginScene);
        stage.show();

    }

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
            case "homePage":{
                stage.setScene(homeScene);
                break;
            }
            default:
                throw new RuntimeException("Tela não existe!");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
