package br.com.homemanager.repository;

import br.com.homemanager.model.DailyTask;
import br.com.homemanager.model.Home;
import br.com.homemanager.model.WeeklyTask;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por lidar com o armazenamento e manipulação dos dados relacionados às casas (homes) dos usuários.
 */
public class HomeRepository {

    private static List<Home> homeList = new ArrayList<>();
    private static final String FILE_PATH = "./storage/user_data.dat";

    static {
        loadUserData();
        printHomeList();
    }

    /**
     * Imprime a lista de casas e suas respectivas tarefas.
     */
    public static void printHomeList(){
        homeList.forEach(System.out::println);
        homeList.forEach(Home::printTasks);
    }

    /**
     * Limpa os dados de usuário armazenados.
     */
    public static void clearUserData() {
        homeList = new ArrayList<>(); // Reinicializa a lista para vazia
        saveUserData(); // Salva a lista vazia no arquivo
    }

    /**
     * Adiciona uma nova casa (Home) à lista de casas.
     *
     * @param home A casa (Home) a ser adicionada.
     */
    public static void addHome(Home home){
        homeList.add(home);
        saveUserData();
    }

    /**
     * Verifica se um nome de usuário já existe.
     *
     * @param username O nome de usuário a ser verificado.
     * @return A Home associada ao nome de usuário, se existir; caso contrário, retorna null.
     */
    public static Home usernameAlreadyExists(String username) {
        for (Home home : homeList) {
            if (home.getUsername().equals(username)) {
                return home;
            }
        }
        return null; // Retorna null se o usuário não for encontrado
    }

    /**
     * Salva os dados de usuário no arquivo.
     */
    public static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(homeList);
        } catch (IOException e) {
            System.out.printf("The file could not be opened: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega os dados de usuário do arquivo.
     */
    private static void loadUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                homeList = (List<Home>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("The file could not be opened: " + e.getMessage());
            homeList = new ArrayList<>();
        }
    }

    /**
     * Gera uma lista de tarefas diárias padrão.
     *
     * @return A lista de tarefas diárias padrão.
     */
    public static List<DailyTask> defaultDailyTasks(){
        List<DailyTask> defaultDailyTasks = new ArrayList<>();
        defaultDailyTasks.add(new DailyTask("Varrer a casa"));
        defaultDailyTasks.add(new DailyTask("Lavar a louça"));
        defaultDailyTasks.add(new DailyTask("Arrumar as camas"));
        defaultDailyTasks.add(new DailyTask("Fazer almoço"));
        defaultDailyTasks.add(new DailyTask("Alimentar o cachorro"));
        defaultDailyTasks.add(new DailyTask("Limpar superfícies"));
        defaultDailyTasks.add(new DailyTask("Levar o lixo"));
        defaultDailyTasks.add(new DailyTask("Regar as plantas"));
        return defaultDailyTasks;
    }

    /**
     * Gera uma lista de tarefas semanais padrão.
     *
     * @return A lista de tarefas semanais padrão.
     */
    public static List<WeeklyTask> defaultWeeklyTasks(){
        List<WeeklyTask> defaultWeeklyTasks = new ArrayList<>();
        defaultWeeklyTasks.add(new WeeklyTask("Lavar os banheiros"));
        defaultWeeklyTasks.add(new WeeklyTask("Lavar roupas"));
        defaultWeeklyTasks.add(new WeeklyTask("Limpar fogão"));
        defaultWeeklyTasks.add(new WeeklyTask("Limpar geladeira"));
        defaultWeeklyTasks.add(new WeeklyTask("Trocar lençóis"));
        defaultWeeklyTasks.add(new WeeklyTask("Trocar toalhas"));
        defaultWeeklyTasks.add(new WeeklyTask("Dar banho no cachorro"));
        defaultWeeklyTasks.add(new WeeklyTask("Fazer as compras"));
        defaultWeeklyTasks.add(new WeeklyTask("Ir ao açougue"));
        return defaultWeeklyTasks;
    }
}