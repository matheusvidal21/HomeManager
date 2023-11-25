package br.com.homemanager.model;


import java.io.Serializable;
import java.util.List;


public class Home implements Serializable {

    private String username;
    private char[] senhaHash;
    private List<Member> membersList;
    private List<WeeklyTask> homeWTasks;
    private List<DailyTask> homeDTasks;


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

    public List<Member> getMembersList(){
        return membersList;
    }

    public void addMember(Member member){membersList.add(member); }

}
