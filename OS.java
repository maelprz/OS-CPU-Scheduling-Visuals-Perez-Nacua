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
    static JSlider speedSlider;
    static JLabel speedValueLabel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CPU Scheduling Simulator");
        frame.setSize(1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        String[] columns = {"#", "Process", "Arrival Time", "Exec. Time", "Priority"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable processTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.setBounds(920, 130, 250, 240);
        frame.add(scrollPane);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(null);
        messagePanel.setBounds(930, 10, 240, 40);
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

        String[] algorithms = {"Select Algorithm", "First In First Out (FIFO)", "Shortest Job First (SJF)", "Shortest Remaining Time First (SRTF)", "Round Robin", "Multilevel Feedback Queue (MLFQ)"};
        JComboBox<String> algoDropdown = new JComboBox<>(algorithms);
        algoDropdown.setBounds(926, 370, 240, 30);
        frame.add(algoDropdown);

        JButton runBtn = new JButton("Run Simulation");
        runBtn.setBounds(978, 440, 150, 30);
        frame.add(runBtn);

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
            switch (selected) {
                case "First In First Out (FIFO)":
                    selectionMessage.setText("Running: FIRST IN FIRST OUT (FIFO)");
                    runFCFS(processes);
                    break;
                case "Shortest Job First (SJF)":
                    selectionMessage.setText("Running: SHORTEST JOB FIRST (SJF)");
                    runSJF(processes);
                    break;
                case "Shortest Remaining Time First (SRTF)":
                    selectionMessage.setText("Running: SHORTEST REMAINING TIME FIRST (SRTF)");
                    runSRTF(processes);
                    break;
                case "Round Robin":
                    int quantum = timeQuantumSlider.getValue();
                    JOptionPane.showMessageDialog(frame, "Selected Round Robin with Quantum = " + quantum);
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Please select a valid algorithm.");
            }
        });

        frame.setVisible(true);
    }

    static class Process {
        int id;
        String name;
        int arrivalTime, burstTime, priority, remainingTime;
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
        new Thread(() -> {
            int currentTime = 0, totalTAT = 0, totalWT = 0, totalRT = 0;
            for (int i = 0; i < list.size(); i++) {
                Process p = list.get(i);
                if (currentTime < p.arrivalTime) currentTime = p.arrivalTime;
                p.responseTime = currentTime - p.arrivalTime;
                p.waitingTime = currentTime - p.arrivalTime;
                p.completionTime = currentTime + p.burstTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                for (int j = 0; j < p.burstTime; j++) {
                    try {
                        int elapsed = j + 1;
                        int progress = (int)(((double)elapsed / p.burstTime) * 100);
                        int remaining = p.burstTime - elapsed;
                        JLabel label = new JLabel(p.name);
                        label.setPreferredSize(new Dimension(40, 40));
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        SwingUtilities.invokeAndWait(() -> ganttContainer.add(label));
                        final int row = i;
                        final int finalProgress = progress;
                        final int finalRemaining = remaining;
                        SwingUtilities.invokeLater(() -> {
                            statusModel.setValueAt("Running", row, 1);
                            statusModel.setValueAt(finalProgress + "%", row, 2);
                            statusModel.setValueAt(finalRemaining, row, 3);
                            ganttContainer.revalidate();
                            ganttContainer.repaint();
                        });
                        double speed = speedSlider.getValue() / 10.0;
                        Thread.sleep((long)(500 / speed));
                    } catch (Exception e) { e.printStackTrace(); }
                }
                final int row = i;
                SwingUtilities.invokeLater(() -> {
                    statusModel.setValueAt("Completed", row, 1);
                    statusModel.setValueAt("100%", row, 2);
                    statusModel.setValueAt(0, row, 3);
                    statusModel.setValueAt(list.get(row).waitingTime, row, 4);
                });
                currentTime = p.completionTime;
                totalTAT += p.turnaroundTime;
                totalWT += p.waitingTime;
                totalRT += p.responseTime;
            }
            int n = list.size();
            final float finalAvgTAT = (float) totalTAT / n;
            final float finalAvgWT = (float) totalWT / n;
            final int finalExecTime = currentTime;
            SwingUtilities.invokeLater(() -> {
                avgTurnaroundLabel.setText("Average Turnaround: " + String.format("%.2f", finalAvgTAT));
                avgWaitingLabel.setText("Average Waiting: " + String.format("%.2f", finalAvgWT));
                totalExecLabel.setText("Total Execution Time: " + finalExecTime);
            });
        }).start();
    }

    public static void runSJF(List<Process> processes) {
        ganttContainer.removeAll();
        new Thread(() -> {
            int currentTime = 0, completed = 0, n = processes.size();
            int totalTAT = 0, totalWT = 0;
            boolean[] done = new boolean[n];

            while (completed < n) {
                Process shortest = null;
                int minBurst = Integer.MAX_VALUE;
                int idx = -1;

                for (int i = 0; i < n; i++) {
                    Process p = processes.get(i);
                    if (!done[i] && p.arrivalTime <= currentTime && p.burstTime < minBurst) {
                        shortest = p;
                        minBurst = p.burstTime;
                        idx = i;
                    }
                }

                if (shortest == null) {
                    currentTime++;
                    continue;
                }

                shortest.waitingTime = currentTime - shortest.arrivalTime;
                shortest.responseTime = shortest.waitingTime;
                shortest.completionTime = currentTime + shortest.burstTime;
                shortest.turnaroundTime = shortest.completionTime - shortest.arrivalTime;

                for (int j = 0; j < shortest.burstTime; j++) {
                    try {
                        int elapsed = j + 1;
                        int progress = (int)(((double)elapsed / shortest.burstTime) * 100);
                        int remaining = shortest.burstTime - elapsed;
                        JLabel label = new JLabel(shortest.name);
                        label.setPreferredSize(new Dimension(40, 40));
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        SwingUtilities.invokeAndWait(() -> ganttContainer.add(label));
                        final int row = idx;
                        final int finalProgress = progress;
                        final int finalRemaining = remaining;
                        SwingUtilities.invokeLater(() -> {
                            statusModel.setValueAt("Running", row, 1);
                            statusModel.setValueAt(finalProgress + "%", row, 2);
                            statusModel.setValueAt(finalRemaining, row, 3);
                            ganttContainer.revalidate();
                            ganttContainer.repaint();
                        });
                        double speed = speedSlider.getValue() / 10.0;
                        Thread.sleep((long)(500 / speed));
                    } catch (Exception e) { e.printStackTrace(); }
                }

                final int row = idx;
                SwingUtilities.invokeLater(() -> {
                    statusModel.setValueAt("Completed", row, 1);
                    statusModel.setValueAt("100%", row, 2);
                    statusModel.setValueAt(0, row, 3);
                    statusModel.setValueAt(processes.get(row).waitingTime, row, 4);
                });

                currentTime = shortest.completionTime;
                totalTAT += shortest.turnaroundTime;
                totalWT += shortest.waitingTime;
                done[idx] = true;
                completed++;
            }

            final float avgTAT = (float) totalTAT / n;
            final float avgWT = (float) totalWT / n;
            final int finalExecTime = currentTime;
            SwingUtilities.invokeLater(() -> {
                avgTurnaroundLabel.setText("Average Turnaround: " + String.format("%.2f", avgTAT));
                avgWaitingLabel.setText("Average Waiting: " + String.format("%.2f", avgWT));
                totalExecLabel.setText("Total Execution Time: " + finalExecTime);
            });
        }).start();
    }

    public static void runSRTF(List<Process> processes) {
        ganttContainer.removeAll();
        new Thread(() -> {
            int time = 0, completed = 0, n = processes.size();
            int totalTAT = 0, totalWT = 0;
            boolean[] isCompleted = new boolean[n];

            while (completed < n) {
                int minRemaining = Integer.MAX_VALUE, idx = -1;
                for (int i = 0; i < n; i++) {
                    Process p = processes.get(i);
                    if (p.arrivalTime <= time && !isCompleted[i] && p.remainingTime < minRemaining && p.remainingTime > 0) {
                        minRemaining = p.remainingTime;
                        idx = i;
                    }
                }

                if (idx == -1) {
                    time++;
                    continue;
                }

                Process current = processes.get(idx);
                if (current.remainingTime == current.burstTime) {
                    current.responseTime = time - current.arrivalTime;
                }

                JLabel label = new JLabel(current.name);
                label.setPreferredSize(new Dimension(40, 40));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                SwingUtilities.invokeLater(() -> {
                    ganttContainer.add(label);
                    ganttContainer.revalidate();
                    ganttContainer.repaint();
                });

                current.remainingTime--;
                int progress = (int)(((double)(current.burstTime - current.remainingTime) / current.burstTime) * 100);
                int remaining = current.remainingTime;
                final int row = idx;
                SwingUtilities.invokeLater(() -> {
                    statusModel.setValueAt("Running", row, 1);
                    statusModel.setValueAt(progress + "%", row, 2);
                    statusModel.setValueAt(remaining, row, 3);
                });

                try {
                    double speed = speedSlider.getValue() / 10.0;
                    Thread.sleep((long)(500 / speed));
                } catch (Exception e) { e.printStackTrace(); }

                if (current.remainingTime == 0) {
                    current.completionTime = time + 1;
                    current.turnaroundTime = current.completionTime - current.arrivalTime;
                    current.waitingTime = current.turnaroundTime - current.burstTime;
                    isCompleted[idx] = true;
                    completed++;
                    SwingUtilities.invokeLater(() -> {
                        statusModel.setValueAt("Completed", row, 1);
                        statusModel.setValueAt("100%", row, 2);
                        statusModel.setValueAt(0, row, 3);
                        statusModel.setValueAt(current.waitingTime, row, 4);
                    });
                }
                time++;
            }

            final float avgTAT = (float) totalTAT / n;
            final float avgWT = (float) totalWT / n;
            final int finalExecTime = time;
            SwingUtilities.invokeLater(() -> {
                avgTurnaroundLabel.setText("Average Turnaround: " + String.format("%.2f", avgTAT));
                avgWaitingLabel.setText("Average Waiting: " + String.format("%.2f", avgWT));
                totalExecLabel.setText("Total Execution Time: " + finalExecTime);
            });
        }).start();
    }
}
