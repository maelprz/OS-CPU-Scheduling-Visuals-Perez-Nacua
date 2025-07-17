import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Comparator;

public class FCFS {
    public static void run(List<Process> processes, JPanel ganttContainer, DefaultTableModel statusModel,
                           JSlider speedSlider, JLabel avgTurnaroundLabel, JLabel avgWaitingLabel, JLabel totalExecLabel) {

        ganttContainer.removeAll();
        ganttContainer.revalidate();
        ganttContainer.repaint();

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        new Thread(() -> {
            final int[] currentTime = {0}; // Wrapped in array for mutability in lambdas
            int totalTAT = 0;
            int totalWT = 0;

            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);

                if (currentTime[0] < p.arrivalTime) {
                    currentTime[0] = p.arrivalTime;
                }

                p.startTime = currentTime[0];
                p.waitingTime = p.startTime - p.arrivalTime;
                p.completionTime = p.startTime + p.burstTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;

                for (int j = 1; j <= p.burstTime; j++) {
                    try {
                        int progress = (int) (((double) j / p.burstTime) * 100);
                        int remaining = p.burstTime - j;

                        JLabel label = new JLabel(p.name);
                        label.setPreferredSize(new java.awt.Dimension(40, 40));
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        label.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));

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
                        Thread.sleep((long) (500 / speed));

                        currentTime[0]++;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                final int row = i;
                SwingUtilities.invokeLater(() -> {
                    statusModel.setValueAt("Completed", row, 1);
                    statusModel.setValueAt("100%", row, 2);
                    statusModel.setValueAt(0, row, 3);
                    statusModel.setValueAt(processes.get(row).waitingTime, row, 4);
                });

                totalTAT += p.turnaroundTime;
                totalWT += p.waitingTime;
            }

            int n = processes.size();
            final float finalAvgTAT = (float) totalTAT / n;
            final float finalAvgWT = (float) totalWT / n;
            final int finalExecTime = currentTime[0];

            SwingUtilities.invokeLater(() -> {
                avgTurnaroundLabel.setText("Average Turnaround: " + String.format("%.2f", finalAvgTAT));
                avgWaitingLabel.setText("Average Waiting: " + String.format("%.2f", finalAvgWT));
                totalExecLabel.setText("Total Execution Time: " + finalExecTime);
            });

        }).start();
    }
}
