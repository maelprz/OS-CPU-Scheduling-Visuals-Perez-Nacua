import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class RR {
    public static void run(List<Process> processes, int quantum, JPanel ganttContainer, DefaultTableModel statusModel,
                           JSlider speedSlider, JLabel avgTurnaroundLabel, JLabel avgWaitingLabel, JLabel totalExecLabel) {

        ganttContainer.removeAll();

        new Thread(() -> {
            int time = 0, completed = 0, n = processes.size();
            int totalTAT = 0, totalWT = 0;

            Queue<Process> readyQueue = new LinkedList<>();
            boolean[] isInQueue = new boolean[n];

            while (completed < n) {
                // Add processes that have arrived to the ready queue
                for (int i = 0; i < n; i++) {
                    Process p = processes.get(i);
                    if (!isInQueue[i] && p.arrivalTime <= time) {
                        readyQueue.add(p);
                        isInQueue[i] = true;
                    }
                }

                if (readyQueue.isEmpty()) {
                    time++;
                    continue;
                }

                Process current = readyQueue.poll();
                int idx = processes.indexOf(current);

                if (current.burstTime == current.remainingTime) {
                    current.responseTime = time - current.arrivalTime;
                }

                int runTime = Math.min(quantum, current.remainingTime);

                for (int j = 0; j < runTime; j++) {
                    current.remainingTime--;

                    JLabel label = new JLabel(current.name);
                    label.setPreferredSize(new Dimension(40, 40));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    
                    // Color logic
                    Color color = new Color((current.name.hashCode() * 997) | 0xFF000000);
                    label.setOpaque(true);
                    label.setBackground(color);

                    SwingUtilities.invokeLater(() -> {
                        ganttContainer.add(label);
                        ganttContainer.revalidate();
                        ganttContainer.repaint();
                    });

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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    time++;

                    // Check if new processes arrived during execution
                    for (int i = 0; i < n; i++) {
                        Process p = processes.get(i);
                        if (!isInQueue[i] && p.arrivalTime <= time) {
                            readyQueue.add(p);
                            isInQueue[i] = true;
                        }
                    }
                }

                if (current.remainingTime == 0) {
                    current.completionTime = time;
                    current.turnaroundTime = current.completionTime - current.arrivalTime;
                    current.waitingTime = current.turnaroundTime - current.burstTime;
                    totalTAT += current.turnaroundTime;
                    totalWT += current.waitingTime;
                    completed++;

                    SwingUtilities.invokeLater(() -> {
                        statusModel.setValueAt("Completed", idx, 1);
                        statusModel.setValueAt("100%", idx, 2);
                        statusModel.setValueAt(0, idx, 3);
                        statusModel.setValueAt(current.waitingTime, idx, 4);
                    });
                } else {
                    readyQueue.add(current); // Requeue unfinished process
                }
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
