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

    public static void main(String[] args) {
        JFrame frame = new JFrame("CPU Scheduling Simulator");
        frame.setSize(1200, 650);
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
        scrollPane.setBounds(920, 90, 250, 240);
        frame.add(scrollPane);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(null);
        messagePanel.setBounds(930, 10, 240, 40);
        messagePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Algorithm Selected", TitledBorder.LEFT, TitledBorder.TOP));

        JLabel selectionMessage = new JLabel("");
        selectionMessage.setBounds(10, 10, 220, 20);
        messagePanel.add(selectionMessage);
        frame.add(messagePanel);

        JButton generateBtn = new JButton("Generate Random Process");
        generateBtn.setBounds(988, 60, 150, 30);
        frame.add(generateBtn);

        JButton deleteBtn = new JButton("Delete Latest Random");
        deleteBtn.setBounds(988, 90, 150, 30);
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
        algoDropdown.setBounds(940, 370, 240, 30);
        frame.add(algoDropdown);

        JButton runBtn = new JButton("Run Simulation");
        runBtn.setBounds(988, 410, 150, 30);
        frame.add(runBtn);

        String[] extensions = {
            ".ino", ".prot", ".npk", ".pem", ".script", ".config",
            ".conf", ".sh", ".col", ".so", ".targets"
        };

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBounds(20, 20, 640, 550); 
        statusPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Process Status Table", TitledBorder.LEFT, TitledBorder.TOP));

        String[] statusCols = {"Process", "Status", "Completion %", "Remaining Exec. Time", "Waiting Time"};
        DefaultTableModel statusModel = new DefaultTableModel(statusCols, 0);
        JTable statusTable = new JTable(statusModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JScrollPane statusScroll = new JScrollPane(statusTable);
        statusScroll.setBounds(20, 30, 600, 480); // adjusted to fit inside the panel
        statusPanel.add(statusScroll);
        frame.add(statusPanel);


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
            if (!selected.equals("Select Algorithm")) {
                selectionMessage.setText("You've selected " + selected);
            } else {
                selectionMessage.setText("");
            }
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
        list.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0, totalTAT = 0, totalWT = 0, totalRT = 0;
        System.out.println("\n--- FCFS Scheduling Result ---");
        System.out.println("ID\tName\tAT\tBT\tCT\tTAT\tWT\tRT");

        for (Process p : list) {
            if (currentTime < p.arrivalTime) currentTime = p.arrivalTime;
            p.responseTime = currentTime - p.arrivalTime;
            p.waitingTime = currentTime - p.arrivalTime;
            p.completionTime = currentTime + p.burstTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            currentTime = p.completionTime;

            totalTAT += p.turnaroundTime;
            totalWT += p.waitingTime;
            totalRT += p.responseTime;

            System.out.printf("%d\t%s\t%d\t%d\t%d\t%d\t%d\t%d\n",
                p.id, p.name, p.arrivalTime, p.burstTime,
                p.completionTime, p.turnaroundTime,
                p.waitingTime, p.responseTime);
        }
        int n = list.size();
        System.out.println("Average Turnaround Time: " + (float) totalTAT / n);
        System.out.println("Average Waiting Time: " + (float) totalWT / n);
        System.out.println("Average Response Time: " + (float) totalRT / n);
    }
}
