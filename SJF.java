import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Dimension;


public class SJF {
    public static void run(List<Process> processes, JPanel ganttContainer, DefaultTableModel statusModel,
                           JSlider speedSlider, JLabel avgTurnaroundLabel, JLabel avgWaitingLabel, JLabel totalExecLabel) {

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
}
