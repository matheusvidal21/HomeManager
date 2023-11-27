package br.com.homemanager.model;

import br.com.homemanager.repository.HomeRepository;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Home implements Serializable {

    private String username;
    private char[] hashPassword;
    private List<Member> membersList;
    private List<WeeklyTask> homeWTasks;
    private List<DailyTask> homeDTasks;

    public Home(String username, char[] password) {
        this.username = username;
        this.hashPassword = generateHashPassword(password);
        this.membersList = new ArrayList<>();
        this.homeDTasks = new ArrayList<>();
        this.homeWTasks = new ArrayList<>();
    }

    public List<WeeklyTask> getHomeWTasks() {
        return homeWTasks;
    }

    public List<DailyTask> getHomeDTasks() {
        return homeDTasks;
    }

    public void printHomeTasks(){
        homeWTasks.forEach(System.out::println);
        homeDTasks.forEach(System.out::println);
    }

    public void addAllWeeklyTasks(List<WeeklyTask> weeklyTasks) {
        homeWTasks.addAll(weeklyTasks);
        HomeRepository.saveUserData();
    }

    public void addAllDailyTasks(List<DailyTask> dailyTasks) {
        homeDTasks.addAll(dailyTasks);
        HomeRepository.saveUserData();
    }

    public void addWeeklyTask(WeeklyTask weeklyTask) {
        homeWTasks.add(weeklyTask);
        HomeRepository.saveUserData();
    }

    public void addDailyTask(DailyTask dailyTask) {
        homeDTasks.add(dailyTask);
        HomeRepository.saveUserData();
    }

    public List<Member> getMembersList(){
        return membersList;
    }

    public void addMember(Member member){membersList.add(member); }

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

    public boolean checkPassword(char[] password) {
        char[] enteredHashPassword = generateHashPassword(password);
        return Arrays.equals(hashPassword, enteredHashPassword);
    }

    public String getUsername() {
        return username;
    }

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
