import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MainDashboard extends JFrame {
    private final TaskManager manager;
    private final JPanel listPanel;
    private final JScrollPane scrollPane;

    // Palette for task cards
    private final Color[] palette = new Color[] {
            new Color(117,97,157),
            new Color(190,174,219),
            new Color(150,128,162)
    };

    public MainDashboard() {
        manager = new TaskManager();
        setTitle("To-Do List");
        setSize(480, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Root panel
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12,12,12,12));
        root.setBackground(new Color(63,42,82));
        setContentPane(root);

        // Header
        JLabel header = new JLabel("My Tasks");
        header.setFont(new Font("Dialog", Font.BOLD, 24)); // larger font
        header.setForeground(Color.WHITE);
        root.add(header, BorderLayout.NORTH);

        // List panel
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        scrollPane = new JScrollPane(listPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        root.add(scrollPane, BorderLayout.CENTER);

        // Bottom add button
        JPanel bottom = new JPanel(new BorderLayout(6,6));
        bottom.setOpaque(false);
        JButton addBtn = new JButton("+ Add Task");
        addBtn.setBackground(new Color(75,21,53));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setOpaque(true);
        addBtn.setFont(new Font("Dialog", Font.BOLD, 16));
        bottom.add(addBtn, BorderLayout.EAST);
        root.add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            AddTaskDialog dialog = new AddTaskDialog(this);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                manager.addTask(dialog.getTitleText(), dialog.getDescriptionText(), dialog.getDueDateString());
                refreshList();
            }
        });

        refreshList();
        setVisible(true);
    }

    private void refreshList() {
        listPanel.removeAll();
        List<Task> tasks = manager.getAllTasks();
        if (tasks.isEmpty()) {
            JLabel empty = new JLabel("No tasks yet. Click + Add Task to create one.");
            empty.setForeground(new Color(200,200,200)); // light gray
            empty.setFont(new Font("Dialog", Font.PLAIN, 16));
            empty.setBorder(new EmptyBorder(20,20,20,20));
            listPanel.add(empty);
        } else {
            int colorIdx = 0;
            for (Task t : tasks) {
                JPanel card = createTaskCard(t, palette[colorIdx % palette.length]);
                listPanel.add(card);
                listPanel.add(Box.createRigidArea(new Dimension(0,10)));
                colorIdx++;
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createTaskCard(Task t, Color bg) {
        JPanel card = new JPanel(new BorderLayout(8,8));
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 1),
                new EmptyBorder(10,10,10,10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140)); // slightly taller

        // Title + due date
        JLabel title = new JLabel(t.getTitle());
        title.setFont(new Font("Dialog", Font.BOLD, 16)); // larger
        title.setForeground(Color.WHITE);

        String due = (t.getDueDate() == null || t.getDueDate().isEmpty()) ? "No due date" : t.getDueDate();
        JLabel dateLabel = new JLabel("Due: " + due);
        dateLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(230,230,230));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(title, BorderLayout.WEST);
        north.add(dateLabel, BorderLayout.EAST);

        // Description
        JTextArea desc = new JTextArea(t.getDescription() == null ? "" : t.getDescription());
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setFont(new Font("Dialog", Font.PLAIN, 14));
        desc.setForeground(new Color(240,240,240));
        desc.setBorder(null);

        // Buttons with palette shades
        Color doneColor = bg.brighter();
        Color delColor = bg.darker();

        JButton doneBtn = new JButton(t.isCompleted() ? "Completed" : "Mark Done");
        doneBtn.setEnabled(!t.isCompleted());
        styleButton(doneBtn, doneColor);

        JButton delBtn = new JButton("Delete");
        styleButton(delBtn, delColor);

        JPanel btns = new JPanel();
        btns.setOpaque(false);
        btns.add(doneBtn);
        btns.add(delBtn);

        // Combine
        card.add(north, BorderLayout.NORTH);
        card.add(desc, BorderLayout.CENTER);
        card.add(btns, BorderLayout.SOUTH);

        // Button actions
        doneBtn.addActionListener(e -> {
            manager.markTaskAsDone(t.getId());
            refreshList();
        });

        delBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete \"" + t.getTitle() + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteTask(t.getId());
                refreshList();
            }
        });

        return card;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Dialog", Font.BOLD, 14));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainDashboard::new);
    }
}
