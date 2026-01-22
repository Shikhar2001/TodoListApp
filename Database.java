import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:todo.db";

    public Database() {
        createTable();
    }

    // Create table with columns for title, description, due_date, completed
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "due_date TEXT," +   // store date as TEXT yyyy-MM-dd
                "completed INTEGER DEFAULT 0)";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("createTable error: " + e.getMessage());
        }
    }

    // Add a task (returns generated id or -1)
    public int addTask(String title, String description, String dueDate) {
        String sql = "INSERT INTO tasks(title, description, due_date) VALUES(?,?,?)";

        //Error handling
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, dueDate);
            int affected = pstmt.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("addTask error: " + e.getMessage());
        }
        return -1;
    }

    //fetches all the tasks
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY id";


        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("due_date"),
                        rs.getInt("completed") == 1
                ));
            }
        } catch (SQLException e) {
            System.err.println("getAllTasks error: " + e.getMessage());
        }
        return tasks;
    }

    //updating the table if task completed or not
    public void markCompleted(int id) {
        String sql = "UPDATE tasks SET completed = 1 WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("markCompleted error: " + e.getMessage());
        }
    }

    //deleting a task
    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteTask error: " + e.getMessage());
        }
    }
}
