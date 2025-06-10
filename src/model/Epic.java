package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();
    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public void removeSubtask(int id) {
        int index = subtasks.indexOf(id);
        this.subtasks.remove(index);
    }

    public void clearSubtasks() {
        subtasks.clear();
    }
};
