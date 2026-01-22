import java.util.List;

public class TaskManager {
    private final Database db;

    public TaskManager() {
        db = new Database();
    }

    // Add task with title, description, dueDate (yyyy-MM-dd)
    public void addTask(String title, String description, String dueDate) {
        int id = db.addTask(title, description, dueDate);
        System.out.println("Added task (id=" + id + "): " + title);
    }

    public List<Task> getAllTasks() {
        return db.getAllTasks();
    }

    public void markTaskAsDone(int id) {
        db.markCompleted(id);
    }

    public void deleteTask(int id) {
        db.deleteTask(id);
    }
}
