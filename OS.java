import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Random;

public class OS {
    static int processCount = 1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CPU Scheduling Simulator");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Define table columns
        String[] columns = {"#", "Process", "Arrival Time", "Exec. Time", "Priority"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable processTable = new JTable(tableModel);

        // Set preferred column widths
        processTable.getColumnModel().getColumn(0).setPreferredWidth(40);   // #
        processTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Process
        processTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Arrival Time
        processTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Exec. Time
        processTable.getColumnModel().getColumn(4).setPreferredWidth(80);   // Priority

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.setBounds(640, 115, 240, 240);
        frame.add(scrollPane);

        // Message Panel Box
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(null);
        messagePanel.setBounds(640, 10, 240, 40);
        messagePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Algorithm Status", TitledBorder.LEFT, TitledBorder.TOP));

        JLabel selectionMessage = new JLabel("");
        selectionMessage.setBounds(10, 10, 220, 20);
        messagePanel.add(selectionMessage);
        frame.add(messagePanel);

        // Generate Button
        JButton generateBtn = new JButton("Generate Random Process");
        generateBtn.setBounds(688, 60, 150, 30);
        frame.add(generateBtn);

        // Delete Button
        JButton deleteBtn = new JButton("Delete Latest Random");
        deleteBtn.setBounds(688, 90, 150, 30);
        frame.add(deleteBtn);

        // Algorithm Dropdown
        String[] algorithms = {
            "Select Algorithm",
            "First In First Out (FIFO)",
            "Shortest Job First (SJF)",
            "Shortest Remaining Time First (SRTF)",
            "Round Robin",
            "Multilevel Feedback Queue (MLFQ)"
        };
        JComboBox<String> algoDropdown = new JComboBox<>(algorithms);
        algoDropdown.setBounds(640, 370, 240, 30);
        frame.add(algoDropdown);

        // File extensions for random names
        String[] extensions = {
            ".ino", ".prot", ".npk", ".pem", ".script", ".config",
            ".conf", ".sh", ".col", ".so", ".targets"
        };

        // Generate random process
        generateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                String processName = "P" + processCount + extensions[rand.nextInt(extensions.length)];
                int arrivalTime = rand.nextInt(10);     // 0–9
                int execTime = rand.nextInt(20) + 1;    // 1–20
                int priority = rand.nextInt(9) + 1;     // 1–9

                tableModel.addRow(new Object[] {
                    processCount + ".", processName, arrivalTime, execTime, priority
                });

                processCount++;
            }
        });

        // Delete latest process
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = tableModel.getRowCount();
                if (rowCount > 0) {
                    tableModel.removeRow(rowCount - 1);
                    processCount--;
                } else {
                    JOptionPane.showMessageDialog(frame, "No rows to delete.");
                }
            }
        });

        // Dropdown changes label
        algoDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = algoDropdown.getSelectedItem().toString();
                if (!selected.equals("Select Algorithm")) {
                    selectionMessage.setText("You've selected " + selected);
                } else {
                    selectionMessage.setText("");
                }
            }
        });

        frame.setVisible(true);
    }
}
