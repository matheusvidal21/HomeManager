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
    @Override
    public void start(Stage stage) throws Exception {
        Program.stage = stage;
        stage.setTitle("HomeManager");

        // Carregar o FXML do LoginController
        FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/br/com/homemanager/login.fxml"));
        Parent rootLogin = loaderLogin.load();
        LoginController loginController = loaderLogin.getController();

        // --- loginController

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
        }
    }
}
