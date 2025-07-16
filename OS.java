import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("CPU Scheduling Simulator");
        frame.setSize(1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        String[] columns = {"#", "Process", "Arrival Time", "Exec. Time", "Priority"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable processTable = new JTable(tableModel);
        processTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        processTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        processTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        processTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        processTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.setBounds(920, 130, 250, 240);
        frame.add(scrollPane);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(null);
        messagePanel.setBounds(930, 10, 240, 40);
        messagePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Algorithm Selected", TitledBorder.CENTER, TitledBorder.TOP));
        JLabel selectionMessage = new JLabel("");
        selectionMessage.setBounds(10, 10, 220, 20);
        messagePanel.add(selectionMessage);
        frame.add(messagePanel);

        JButton generateBtn = new JButton("Generate Random Process");
        generateBtn.setBounds(965, 61, 150, 30);
        frame.add(generateBtn);

        JButton deleteBtn = new JButton("Delete Latest Random");
        deleteBtn.setBounds(965, 89, 150, 30);
        frame.add(deleteBtn);

        String[] algorithms = {
            "Select Algorithm",
            "First In First Out (FIFO)",
            "Shortest Job First (SJF)",
            "Shortest Remaining Time First (SRTF)",
            "Round Robin",
            "Multilevel Feedback Queue (MLFQ)"
        };
        JComboBox<String> algoDropdown = new JComboBox<>(algorithms);
        algoDropdown.setBounds(926, 370, 240, 30);
        frame.add(algoDropdown);

        JButton runBtn = new JButton("Run Simulation");
        runBtn.setBounds(978, 440, 150, 30);
        frame.add(runBtn);

        String[] extensions = {
            ".ino", ".prot", ".npk", ".pem", ".script", ".config",
            ".conf", ".sh", ".col", ".so", ".targets"
        };

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBounds(20, 20, 640, 520);
        statusPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Process Status Table", TitledBorder.CENTER, TitledBorder.TOP));

        String[] statusCols = {"Process", "Status", "Completion %", "Remaining Ex.Time", "Waiting Time"};
        statusModel = new DefaultTableModel(statusCols, 0);
        JTable statusTable = new JTable(statusModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane statusScroll = new JScrollPane(statusTable);
        statusScroll.setBounds(20, 30, 600, 480);
        statusPanel.add(statusScroll);
        frame.add(statusPanel);

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(null);
        resultsPanel.setBounds(670, 20, 230, 150);
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Simulation Results", TitledBorder.CENTER, TitledBorder.TOP));

        avgTurnaroundLabel = new JLabel("Average Turnaround: ");
        avgTurnaroundLabel.setBounds(10, 30, 210, 20);
        resultsPanel.add(avgTurnaroundLabel);

        avgWaitingLabel = new JLabel("Average Waiting: ");
        avgWaitingLabel.setBounds(10, 60, 210, 20);
        resultsPanel.add(avgWaitingLabel);

        totalExecLabel = new JLabel("Total Execution Time: ");
        totalExecLabel.setBounds(10, 90, 210, 20);
        resultsPanel.add(totalExecLabel);

        frame.add(resultsPanel);

        JPanel quantumPanel = new JPanel();
        quantumPanel.setLayout(null);
        quantumPanel.setBounds(670, 180, 230, 100);
        quantumPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Time Quantum (Round Robin)", TitledBorder.CENTER, TitledBorder.TOP));

        timeQuantumSlider = new JSlider(0, 30, 5);
        timeQuantumSlider.setBounds(10, 30, 150, 40);
        timeQuantumSlider.setMajorTickSpacing(5);
        timeQuantumSlider.setPaintTicks(true);
        timeQuantumSlider.setEnabled(false);

        timeQuantumValueLabel = new JLabel("5");
        timeQuantumValueLabel.setBounds(170, 30, 40, 30);
        timeQuantumValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeQuantumValueLabel.setEnabled(false);

        timeQuantumSlider.addChangeListener(e -> {
            timeQuantumValueLabel.setText(String.valueOf(timeQuantumSlider.getValue()));
        });

        quantumPanel.add(timeQuantumSlider);
        quantumPanel.add(timeQuantumValueLabel);
        frame.add(quantumPanel);

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

            tableModel.addRow(new Object[]{
                    processCount + ".", processName, arrivalTime, execTime, priority
            });

            statusModel.addRow(new Object[]{
                    processName, "Idle", "0%", execTime, 0
            });

            processCount++;
        });

        deleteBtn.addActionListener(e -> {
            int rowCount = tableModel.getRowCount();
            if (rowCount > 0) {
                tableModel.removeRow(rowCount - 1);
                statusModel.removeRow(rowCount - 1);
                processCount--;
            } else {
                JOptionPane.showMessageDialog(frame, "No rows to delete.");
            }
        });

        algoDropdown.addActionListener(e -> {
            String selected = algoDropdown.getSelectedItem().toString();
            boolean isRR = selected.equals("Round Robin");

            selectionMessage.setText(selected.equals("Select Algorithm") ? "" : "You've selected " + selected);
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

            if (selected.equals("First In First Out (FIFO)")) {
                selectionMessage.setText("Running: FIRST IN FIRST OUT (FIFO)");
                runFCFS(processes);
            } else if (selected.equals("Round Robin")) {
                int quantum = timeQuantumSlider.getValue();
                JOptionPane.showMessageDialog(frame, "Selected Round Robin with Quantum = " + quantum);
                // runRoundRobin(processes, quantum);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a valid algorithm.");
            }
        });

        frame.setVisible(true);
    }

    static class Process {
        int id;
        String name;
        int arrivalTime;
        int burstTime;
        int priority;
        int remainingTime;
        int completionTime, turnaroundTime, waitingTime, responseTime;

        public Process(int id, String name, int arrivalTime, int burstTime, int priority) {
            this.id = id;
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.priority = priority;
            this.remainingTime = burstTime;
        }
    }

    public static void runFCFS(List<Process> list) {
        ganttContainer.removeAll();
        list.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0, totalTAT = 0, totalWT = 0, totalRT = 0;

        for (int i = 0; i < list.size(); i++) {
            Process p = list.get(i);

            if (currentTime < p.arrivalTime) currentTime = p.arrivalTime;

            p.responseTime = currentTime - p.arrivalTime;
            p.waitingTime = currentTime - p.arrivalTime;
            p.completionTime = currentTime + p.burstTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;

            for (int j = 0; j < p.burstTime; j++) {
                JLabel label = new JLabel(p.name);
                label.setPreferredSize(new Dimension(40, 40));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                ganttContainer.add(label);
            }

            statusModel.setValueAt("Completed", i, 1);
            statusModel.setValueAt("100%", i, 2);
            statusModel.setValueAt(0, i, 3);
            statusModel.setValueAt(p.waitingTime, i, 4);

            currentTime = p.completionTime;
            totalTAT += p.turnaroundTime;
            totalWT += p.waitingTime;
            totalRT += p.responseTime;
        }

        int n = list.size();
        avgTurnaroundLabel.setText("Average Turnaround: " + String.format("%.2f", (float) totalTAT / n));
        avgWaitingLabel.setText("Average Waiting: " + String.format("%.2f", (float) totalWT / n));
        totalExecLabel.setText("Total Execution Time: " + currentTime);

        ganttContainer.revalidate();
        ganttContainer.repaint();
    }
}
