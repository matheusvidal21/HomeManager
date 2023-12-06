package br.com.homemanager.model;

import br.com.homemanager.repository.HomeRepository;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Representa uma casa com informações sobre membros, tarefas semanais e diárias.
 */
public class Home implements Serializable {

    private String username;
    private char[] hashPassword;
    private List<Member> membersList;
    private List<WeeklyTask> homeWTasks;
    private List<DailyTask> homeDTasks;

    /**
     * Construtor da classe Home.
     *
     * @param username O nome de usuário da casa.
     * @param password A senha da casa.
     */
    public Home(String username, char[] password) {
        this.username = username;
        this.hashPassword = generateHashPassword(password);
        this.membersList = new ArrayList<>();
        this.homeDTasks = new ArrayList<>();
        this.homeWTasks = new ArrayList<>();
    }

    /**
     * Obtém a lista de tarefas semanais da casa.
     *
     * @return A lista de tarefas semanais.
     */
    public List<WeeklyTask> getHomeWTasks() {
        return homeWTasks;
    }

    /**
     * Obtém a lista de tarefas diárias da casa.
     *
     * @return A lista de tarefas diárias.
     */
    public List<DailyTask> getHomeDTasks() {
        return homeDTasks;
    }

    /**
     * Imprime todas as tarefas da casa (semanais e diárias).
     */
    public void printHomeTasks(){
        homeWTasks.forEach(System.out::println);
        homeDTasks.forEach(System.out::println);
    }

    /**
     * Adiciona uma lista de tarefas semanais à casa.
     *
     * @param weeklyTasks A lista de tarefas semanais a ser adicionada.
     */
    public void addAllWeeklyTasks(List<WeeklyTask> weeklyTasks) {
        homeWTasks.addAll(weeklyTasks);
        HomeRepository.saveUserData();
    }

    /**
     * Adiciona uma lista de tarefas diárias à casa.
     *
     * @param dailyTasks A lista de tarefas diárias a ser adicionada.
     */
    public void addAllDailyTasks(List<DailyTask> dailyTasks) {
        homeDTasks.addAll(dailyTasks);
        HomeRepository.saveUserData();
    }

    /**
     * Adiciona uma tarefa semanal à casa.
     *
     * @param weeklyTask A tarefa semanal a ser adicionada.
     */
    public void addWeeklyTask(WeeklyTask weeklyTask) {
        homeWTasks.add(weeklyTask);
        HomeRepository.saveUserData();
    }

    /**
     * Adiciona uma tarefa diária à casa.
     *
     * @param dailyTask A tarefa diária a ser adicionada.
     */
    public void addDailyTask(DailyTask dailyTask) {
        homeDTasks.add(dailyTask);
        HomeRepository.saveUserData();
    }

    /**
     * Obtém a lista de membros da casa.
     *
     * @return A lista de membros.
     */
    public List<Member> getMembersList(){
        return membersList;
    }

    /**
     * Adiciona um membro à lista de membros da casa.
     *
     * @param member O membro a ser adicionado.
     */
    public void addMember(Member member){membersList.add(member); }

    /**
     * Obtém o nome de usuário da casa.
     *
     * @return O nome de usuário da casa.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gera um hash para a senha fornecida usando o algoritmo SHA-256.
     *
     * @param senha A senha para gerar o hash.
     * @return O hash gerado como um array de caracteres.
     */
    public static char[] generateHashPassword(char[] senha) {
        try {
            // Use SHA-256 para hashing (ou outro algoritmo mais seguro)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(new String(senha).getBytes());

            // Converte bytes para caracteres hexadecimais
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Retorna o hash como um array de caracteres
            return hexString.toString().toCharArray();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Em um ambiente real, você lidaria com essa exceção de maneira mais apropriada
            return null;
        }
    }

    /**
     * Verifica se a senha fornecida corresponde à senha armazenada.
     *
     * @param password A senha a ser verificada.
     * @return Verdadeiro se a senha fornecida corresponder à senha armazenada; caso contrário, falso.
     */
    public boolean checkPassword(char[] password) {
        char[] enteredHashPassword = generateHashPassword(password);
        return Arrays.equals(hashPassword, enteredHashPassword);
    }

    /**
     * Distribui tarefas semanais entre os membros da casa.
     *
     * Se não houver membros ou tarefas semanais para distribuir, imprime uma mensagem no console.
     */
    public void assignWeeklyTasks() {
        if (membersList.isEmpty() || homeWTasks.isEmpty()) {
            System.out.println("Sem membros ou tarefas semanais para distribuir.");
            return;
        }

        // Calcula o número de tarefas que cada membro deve receber
        int tasksPerMember = homeWTasks.size() / membersList.size();
        int remainingTasks = homeWTasks.size() % membersList.size();

        List<WeeklyTask> homeAvailableTasks = new ArrayList<>(homeWTasks);
        // Distribui tarefas para cada membro
        for (Member membro : membersList) {
            int numberOfTasks = tasksPerMember + (remainingTasks > 0 ? 1 : 0);
            remainingTasks--;

            // Embaralha a lista de tarefas semanais para distribuição aleatória
            Collections.shuffle(homeAvailableTasks);

            // Remove as tarefas atuais do membro
            homeAvailableTasks.removeAll(membro.getWeeklyTasks());

            List<WeeklyTask> homeAvailableTasksCopy = new ArrayList<>(homeAvailableTasks);
            int endIndex = Math.min(numberOfTasks, homeAvailableTasksCopy.size());

            if(endIndex == homeAvailableTasksCopy.size() && endIndex != numberOfTasks){
                remainingTasks++;
            }
            List<WeeklyTask> tasksToMember = homeAvailableTasksCopy.subList(0, endIndex);

            homeAvailableTasks.addAll(membro.getWeeklyTasks());
            membro.removeWeeklyTasks();
            membro.addAllWeeklyTasks(tasksToMember);

            // Remove as tarefas que o membro pegou
            homeAvailableTasks.removeAll(tasksToMember);
        }
        HomeRepository.saveUserData();
    }

    /**
     * Distribui tarefas diárias entre os membros da casa.
     *
     * Se não houver membros ou tarefas diárias para distribuir, imprime uma mensagem no console.
     */
    public void assignDailyTasks() {
        if (membersList.isEmpty() || homeDTasks.isEmpty()) {
            System.out.println("Sem membros ou tarefas diárias para distribuir.");
            return;
        }

        // Calcula o número de tarefas que cada membro deve receber
        int tasksPerMember = homeDTasks.size() / membersList.size();
        int remainingTasks = homeDTasks.size() % membersList.size();

        List<DailyTask> homeAvailableTasks = new ArrayList<>(homeDTasks);
        // Distribui tarefas para cada membro
        for (Member membro : membersList) {
            int numberOfTasks = tasksPerMember + (remainingTasks > 0 ? 1 : 0);
            remainingTasks--;

            // Embaralha a lista de tarefas semanais para distribuição aleatória
            Collections.shuffle(homeAvailableTasks);

            // Remove as tarefas atuais do membro
            homeAvailableTasks.removeAll(membro.getDailyTasks());

            List<DailyTask> homeAvailableTasksCopy = new ArrayList<>(homeAvailableTasks);
            int endIndex = Math.min(numberOfTasks, homeAvailableTasksCopy.size());

            if(endIndex == homeAvailableTasksCopy.size()){
                remainingTasks++;
            }
            List<DailyTask> tasksToMember = homeAvailableTasksCopy.subList(0, endIndex);

            homeAvailableTasks.addAll(membro.getDailyTasks());
            membro.removeDailyTasks();
            membro.addAllDailyTasks(tasksToMember);

            // Remove as tarefas que o membro pegou
            homeAvailableTasks.removeAll(tasksToMember);
        }
        HomeRepository.saveUserData();
    }

    /**
     * Imprime as tarefas atribuídas a cada membro da casa, tanto semanais quanto diárias.
     */
    public void printTasks(){
        for (Member member : membersList) {
            System.out.println("Weekly Tasks for Member: " + member.getName());
            for (WeeklyTask weeklyTask : member.getWeeklyTasks()) {
                System.out.println(" - " + weeklyTask.getTaskName() + ": " + weeklyTask.getTaskStatus());
            }

            System.out.println("Daily Tasks for Member: " + member.getName());
            for (DailyTask dailyTask : member.getDailyTasks()) {
                System.out.println(" - " + dailyTask.getTaskName() + ": " + dailyTask.getTaskStatus());
            }

            System.out.println(); // Adiciona uma linha em branco entre os membros
        }
    }

    @Override
    public String toString() {
        return "Home{" +
                "username='" + username + '\'' +
                ", membersList = " + membersList +
                ", weeklyList = " + homeWTasks +
                '}';
    }
}