import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class OS {
    static int processCount = 1;

    static JLabel avgTurnaroundLabel;
    static JLabel avgWaitingLabel;
    static JLabel totalExecLabel;
    static JPanel ganttContainer;
    static JSlider timeQuantumSlider;
    static JLabel timeQuantumValueLabel;
    static DefaultTableModel statusModel;
    static JSlider speedSlider;
    static JLabel speedValueLabel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CPU Scheduling Simulator");
        frame.setSize(1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        String[] columns = {"#", "Process", "Arrival Time", "Exec. Time", "Priority"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable processTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.setBounds(920, 130, 250, 240);
        frame.add(scrollPane);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(null);
        messagePanel.setBounds(918, 10, 253, 40);
        messagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Algorithm Selected", TitledBorder.CENTER, TitledBorder.TOP));
        JLabel selectionMessage = new JLabel("");
        selectionMessage.setBounds(10, 10, 220, 20);
        messagePanel.add(selectionMessage);
        frame.add(messagePanel);

        JButton generateBtn = new JButton("Random Process");
        generateBtn.setBounds(965, 61, 150, 30);
        frame.add(generateBtn);

        JButton deleteBtn = new JButton("Delete Random");
        deleteBtn.setBounds(965, 89, 150, 30);
        frame.add(deleteBtn);

        String[] algorithms = {"Select Algorithm", "First In First Out (FIFO)", "Shortest Job First (SJF)", "Shortest Remaining Time First (SRTF)", "Round Robin (RR)", "Multilevel Feedback Queue (MLFQ)"};
        JComboBox<String> algoDropdown = new JComboBox<>(algorithms);
        algoDropdown.setBounds(924, 375, 240, 30);
        frame.add(algoDropdown);

        JButton runBtn = new JButton("Run Simulation");
        runBtn.setBounds(978, 440, 150, 30);
        frame.add(runBtn);

        JButton resetBtn = new JButton("Reset");
        resetBtn.setBounds(978, 480, 150, 30);
        frame.add(resetBtn);

        String[] extensions = {".ino", ".prot", ".npk", ".pem", ".script", ".config", ".conf", ".sh", ".col", ".so", ".targets"};

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBounds(20, 20, 640, 520);
        statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Process Status Table", TitledBorder.CENTER, TitledBorder.TOP));

        String[] statusCols = {"Process", "Status", "Completion %", "Remaining Ex.Time", "Waiting Time"};
        statusModel = new DefaultTableModel(statusCols, 0);
        JTable statusTable = new JTable(statusModel) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JScrollPane statusScroll = new JScrollPane(statusTable);
        statusScroll.setBounds(20, 30, 600, 480);
        statusPanel.add(statusScroll);
        frame.add(statusPanel);

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(null);
        resultsPanel.setBounds(670, 20, 230, 150);
        resultsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Simulation Results", TitledBorder.CENTER, TitledBorder.TOP));
        avgTurnaroundLabel = new JLabel("Average Turnaround: ");
        avgWaitingLabel = new JLabel("Average Waiting: ");
        totalExecLabel = new JLabel("Total Execution Time: ");
        avgTurnaroundLabel.setBounds(10, 30, 210, 20);
        avgWaitingLabel.setBounds(10, 60, 210, 20);
        totalExecLabel.setBounds(10, 90, 210, 20);
        resultsPanel.add(avgTurnaroundLabel);
        resultsPanel.add(avgWaitingLabel);
        resultsPanel.add(totalExecLabel);
        frame.add(resultsPanel);

        JPanel quantumPanel = new JPanel();
        quantumPanel.setLayout(null);
        quantumPanel.setBounds(670, 180, 230, 100);
        quantumPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Time Quantum (Round Robin)", TitledBorder.CENTER, TitledBorder.TOP));
        timeQuantumSlider = new JSlider(0, 30, 5);
        timeQuantumSlider.setBounds(10, 30, 150, 40);
        timeQuantumSlider.setMajorTickSpacing(5);
        timeQuantumSlider.setPaintTicks(true);
        timeQuantumSlider.setEnabled(false);
        timeQuantumValueLabel = new JLabel("5");
        timeQuantumValueLabel.setBounds(170, 30, 40, 30);
        timeQuantumValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeQuantumValueLabel.setEnabled(false);
        timeQuantumSlider.addChangeListener(e -> timeQuantumValueLabel.setText(String.valueOf(timeQuantumSlider.getValue())));
        quantumPanel.add(timeQuantumSlider);
        quantumPanel.add(timeQuantumValueLabel);
        frame.add(quantumPanel);

        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(null);
        speedPanel.setBounds(670, 290, 230, 100);
        speedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Simulation Speed (x)", TitledBorder.CENTER, TitledBorder.TOP));
        speedSlider = new JSlider(1, 50, 10);
        speedSlider.setBounds(10, 30, 150, 40);
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedValueLabel = new JLabel("1.0x");
        speedValueLabel.setBounds(170, 30, 50, 30);
        speedValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        speedSlider.addChangeListener(e -> speedValueLabel.setText(String.format("%.1fx", speedSlider.getValue() / 10.0)));
        speedPanel.add(speedSlider);
        speedPanel.add(speedValueLabel);
        frame.add(speedPanel);

        ganttContainer = new JPanel();
        ganttContainer.setLayout(new BoxLayout(ganttContainer, BoxLayout.X_AXIS));
        JScrollPane ganttScroll = new JScrollPane(ganttContainer);
        ganttScroll.setBounds(20, 600, 1150, 80);
        ganttScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        ganttScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        frame.add(ganttScroll);

        generateBtn.addActionListener(e -> {
            Random rand = new Random();
            String processName = "P" + processCount + extensions[rand.nextInt(extensions.length)];
            int arrivalTime = rand.nextInt(10);
            int execTime = rand.nextInt(20) + 1;
            int priority = rand.nextInt(9) + 1;
            tableModel.addRow(new Object[]{processCount + ".", processName, arrivalTime, execTime, priority});
            statusModel.addRow(new Object[]{processName, "Idle", "0%", execTime, 0});
            processCount++;
        });

        deleteBtn.addActionListener(e -> {
            int rowCount = tableModel.getRowCount();
            if (rowCount > 0) {
                tableModel.removeRow(rowCount - 1);
                statusModel.removeRow(rowCount - 1);
                processCount--;
            } else JOptionPane.showMessageDialog(frame, "No rows to delete.");
        });

        algoDropdown.addActionListener(e -> {
            String selected = algoDropdown.getSelectedItem().toString();
            boolean isRR = selected.equals("Round Robin (RR)");
            selectionMessage.setHorizontalAlignment(SwingConstants.CENTER);
            selectionMessage.setBorder(new EmptyBorder(5, 0, 0, 0));
            selectionMessage.setText(selected.equals("Select Algorithm") ? "" : selected);
            timeQuantumSlider.setEnabled(isRR);
            timeQuantumValueLabel.setEnabled(isRR);
        });

        runBtn.addActionListener(e -> {
            String selected = algoDropdown.getSelectedItem().toString();
            List<Process> processes = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = tableModel.getValueAt(i, 1).toString();
                int arrival = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                int burst = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
                int priority = Integer.parseInt(tableModel.getValueAt(i, 4).toString());
                processes.add(new Process(i + 1, name, arrival, burst, priority));
            }
            switch (selected) {
                case "First In First Out (FIFO)":
                    selectionMessage.setText("Running: FIRST IN FIRST OUT (FIFO)");
                    FCFS.run(processes, ganttContainer, statusModel, speedSlider, avgTurnaroundLabel, avgWaitingLabel, totalExecLabel);
                    break;
                case "Shortest Job First (SJF)":
                    selectionMessage.setText("Running: SHORTEST JOB FIRST (SJF)");
                    SJF.run(processes, ganttContainer, statusModel, speedSlider, avgTurnaroundLabel, avgWaitingLabel, totalExecLabel);
                    break;
                case "Shortest Remaining Time First (SRTF)":
                    selectionMessage.setText("Running: SHORTEST REMAINING TIME FIRST (SRTF)");
                    SRTF.run(processes, ganttContainer, statusModel, speedSlider, avgTurnaroundLabel, avgWaitingLabel, totalExecLabel);
                    break;
                case "Round Robin (RR)":
                    int quantum = timeQuantumSlider.getValue();
                    selectionMessage.setText("Running: ROUND ROBIN (RR) (Quantum: " + quantum + ")");
                    RR.run(processes, quantum, ganttContainer, statusModel, speedSlider, avgTurnaroundLabel, avgWaitingLabel, totalExecLabel);
                    break;
                case "Multilevel Feedback Queue (MLFQ)":
                    selectionMessage.setText("Running: MULTILEVEL FEEDBACK QUEUE (MLFQ)");
                    MLFQ.run(processes, ganttContainer, statusModel, speedSlider, avgTurnaroundLabel, avgWaitingLabel, totalExecLabel);
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Please select a valid algorithm.");
            }
        });

        resetBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            statusModel.setRowCount(0);
            ganttContainer.removeAll();
            ganttContainer.revalidate();
            ganttContainer.repaint();
            processCount = 1;
            avgTurnaroundLabel.setText("Average Turnaround: ");
            avgWaitingLabel.setText("Average Waiting: ");
            totalExecLabel.setText("Total Execution Time: ");
            selectionMessage.setText("");
            algoDropdown.setSelectedIndex(0);
            timeQuantumSlider.setValue(5);
            speedSlider.setValue(10);
            timeQuantumSlider.setEnabled(false);
            timeQuantumValueLabel.setEnabled(false);
        });

        frame.setVisible(true);
    }
}
