import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SRTF {
    private static final Map<String, Color> processColors = new HashMap<>();
    private static final Random rand = new Random();

    private static Color getColorForProcess(String name) {
        return processColors.computeIfAbsent(name, _ ->
                new Color(rand.nextInt(128) + 64, rand.nextInt(128) + 64, rand.nextInt(128) + 64));
    }

    public static void run(List<Process> processes, JPanel ganttContainer, DefaultTableModel statusModel,
                           JSlider speedSlider, JLabel avgTurnaroundLabel, JLabel avgWaitingLabel, JLabel totalExecLabel) {

        ganttContainer.removeAll();

        new Thread(() -> {
            int time = 0;
            int completed = 0;
            int n = processes.size();
            int totalTAT = 0;
            int totalWT = 0;
            boolean[] isCompleted = new boolean[n];

            while (completed < n) {
                int minRemaining = Integer.MAX_VALUE;
                int idx = -1;

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
                    current.startTime = time;
                    current.responseTime = time - current.arrivalTime;
                }

                JLabel label = new JLabel(current.name);
                label.setPreferredSize(new Dimension(40, 40));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(getColorForProcess(current.name));
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                SwingUtilities.invokeLater(() -> {
                    ganttContainer.add(label);
                    ganttContainer.revalidate();
                    ganttContainer.repaint();
                });

                current.remainingTime--;
                int progress = (int)(((double)(current.burstTime - current.remainingTime) / current.burstTime) * 100);
                final int row = idx;

                SwingUtilities.invokeLater(() -> {
                    statusModel.setValueAt("Running", row, 1);
                    statusModel.setValueAt(progress + "%", row, 2);
                    statusModel.setValueAt(current.remainingTime, row, 3);
                });

                try {
                    double speed = speedSlider.getValue() / 10.0;
                    Thread.sleep((long)(500 / speed));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (current.remainingTime == 0) {
                    current.completionTime = time + 1;
                    current.turnaroundTime = current.completionTime - current.arrivalTime;
                    current.waitingTime = current.turnaroundTime - current.burstTime;
                    totalTAT += current.turnaroundTime;
                    totalWT += current.waitingTime;
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
