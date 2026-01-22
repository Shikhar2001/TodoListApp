import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AddTaskDialog extends JDialog {
    private boolean saved = false;
    private final JTextField titleField;
    private final JTextArea descArea;
    private final JSpinner dateSpinner;

    public AddTaskDialog(Frame parent) {
        super(parent, "Create New Task", true);
        setSize(400, 320);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(8,8));
        getContentPane().setBackground(new Color(250, 248, 249));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        center.add(new JLabel("Title:"));
        titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        center.add(titleField);
        center.add(Box.createRigidArea(new Dimension(0,8)));

        center.add(new JLabel("Description:"));
        descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        center.add(descScroll);
        center.add(Box.createRigidArea(new Dimension(0,8)));

        center.add(new JLabel("Due Date (optional):"));
        // Date spinner
        Date initDate = new Date();
        SpinnerDateModel model = new SpinnerDateModel(initDate, null, null, java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        center.add(dateSpinner);

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        bottom.add(save);
        bottom.add(cancel);
        add(bottom, BorderLayout.SOUTH);

        save.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a title");
                return;
            }
            saved = true;
            setVisible(false);
        });

        cancel.addActionListener(e -> {
            saved = false;
            setVisible(false);
        });
    }

    public boolean isSaved() { return saved; }
    public String getTitleText() { return titleField.getText().trim(); }
    public String getDescriptionText() { return descArea.getText().trim(); }
    public String getDueDateString() {
        Date d = (Date) dateSpinner.getValue();
        return Task.formatDate(d);
    }
}
