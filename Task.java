import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private int id;
    private String title;
    private String description;
    private String dueDate;       // stored as "yyyy-MM-dd"
    private boolean completed;

    public Task(int id, String title, String description, String dueDate, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public Task(String title, String description, String dueDate) {
        this(-1, title, description, dueDate, false);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        String dd = (dueDate == null || dueDate.isEmpty()) ? "No due date" : dueDate;
        return id + ". " + title + " [" + (completed ? "Done" : "Pending") + "] - " + dd;
    }

    // helper to format java.util.Date to yyyy-MM-dd
    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date);
    }
}
