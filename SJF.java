import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SJF {

    public static void run(List<Process> processList, JPanel ganttPanel, DefaultTableModel tableModel,
                           JSlider speedSlider, JLabel avgTATLabel, JLabel avgWTLabel, JLabel totalTimeLabel) {
        ganttPanel.removeAll();

        new Thread(() -> {
            int currentTime = 0;
            int completed = 0;
            int totalTAT = 0;
            int totalWT = 0;
            int n = processList.size();
            boolean[] isCompleted = new boolean[n];

            while (completed < n) {
                Process shortest = null;
                int index = -1;

                for (int i = 0; i < n; i++) {
                    Process p = processList.get(i);
                    if (!isCompleted[i] && p.arrivalTime <= currentTime) {
                        if (shortest == null || p.burstTime < shortest.burstTime ||
                            (p.burstTime == shortest.burstTime && p.arrivalTime < shortest.arrivalTime)) {
                            shortest = p;
                            index = i;
                        }
                    }
                }

                if (shortest == null) {
                    currentTime++;
                    continue;
                }

                shortest.startTime = currentTime;
                shortest.completionTime = currentTime + shortest.burstTime;
                shortest.turnaroundTime = shortest.completionTime - shortest.arrivalTime;
                shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
                shortest.responseTime = shortest.startTime - shortest.arrivalTime;

                // Simulate Gantt chart
                for (int t = 0; t < shortest.burstTime; t++) {
                    int progress = (int) ((t + 1) * 100.0 / shortest.burstTime);
                    int remaining = shortest.burstTime - (t + 1);
                    int rowIndex = index;
                    String name = shortest.name;

                    JLabel box = new JLabel(name);
                    box.setPreferredSize(new Dimension(40, 40));
                    box.setHorizontalAlignment(SwingConstants.CENTER);
                    box.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    SwingUtilities.invokeLater(() -> {
                        ganttPanel.add(box);
                        ganttPanel.revalidate();
                        ganttPanel.repaint();
                        tableModel.setValueAt("Running", rowIndex, 1);
                        tableModel.setValueAt(progress + "%", rowIndex, 2);
                        tableModel.setValueAt(remaining, rowIndex, 3);
                    });

                    try {
                        double simSpeed = speedSlider.getValue() / 10.0;
                        Thread.sleep((long) (500 / simSpeed));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Finalize process display
                int rowIndexFinal = index;
                Process finalProcess = shortest;
                SwingUtilities.invokeLater(() -> {
                    tableModel.setValueAt("Completed", rowIndexFinal, 1);
                    tableModel.setValueAt("100%", rowIndexFinal, 2);
                    tableModel.setValueAt(0, rowIndexFinal, 3);
                    tableModel.setValueAt(finalProcess.waitingTime, rowIndexFinal, 4);
                });

                currentTime = shortest.completionTime;
                totalTAT += shortest.turnaroundTime;
                totalWT += shortest.waitingTime;
                isCompleted[index] = true;
                completed++;
            }

            float avgTAT = (float) totalTAT / n;
            float avgWT = (float) totalWT / n;
            int totalExecution = currentTime;

            SwingUtilities.invokeLater(() -> {
                avgTATLabel.setText("Average Turnaround: " + String.format("%.2f", avgTAT));
                avgWTLabel.setText("Average Waiting: " + String.format("%.2f", avgWT));
                totalTimeLabel.setText("Total Execution Time: " + totalExecution);
            });
        }).start();
    }
}
