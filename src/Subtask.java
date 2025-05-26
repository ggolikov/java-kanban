public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public Epic getEpic() {
        return this.epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
