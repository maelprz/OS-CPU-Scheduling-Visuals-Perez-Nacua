import java.awt.Dimension;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List; 

import java.util.*;

public class MLFQ {
    public static void run(List<Process> processes, JPanel ganttContainer, DefaultTableModel statusModel,
                           JSlider speedSlider, JLabel avgTurnaroundLabel, JLabel avgWaitingLabel, JLabel totalExecLabel) {

        ganttContainer.removeAll();

        new Thread(() -> {
            int time = 0, completed = 0, n = processes.size();
            int totalTAT = 0, totalWT = 0;

            int[] quantum = {4, 8, 12};
            List<Queue<Process>> queues = new ArrayList<>();
            for (int i = 0; i < quantum.length; i++) queues.add(new LinkedList<>());

            boolean[] isInQueue = new boolean[n];

            while (completed < n) {
                for (int i = 0; i < n; i++) {
                    Process p = processes.get(i);
                    if (!isInQueue[i] && p.arrivalTime <= time) {
                        queues.get(0).add(p);
                        isInQueue[i] = true;
                    }
                }

                Process current = null;
                int level = -1;

                for (int i = 0; i < queues.size(); i++) {
                    if (!queues.get(i).isEmpty()) {
                        current = queues.get(i).poll();
                        level = i;
                        break;
                    }
                }

                if (current == null) {
                    time++;
                    continue;
                }

                final Process currentFinal = current;
                final int idx = processes.indexOf(current);
                final int levelFinal = level;

                if (current.remainingTime == current.burstTime)
                    current.responseTime = time - current.arrivalTime;

                int runTime = Math.min(quantum[level], current.remainingTime);

                for (int j = 0; j < runTime && current.remainingTime > 0; j++) {
                    current.remainingTime--;

                    final JLabel label = new JLabel(current.name);
                    label.setPreferredSize(new Dimension(40, 40));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    SwingUtilities.invokeLater(() -> {
                        ganttContainer.add(label);
                        ganttContainer.revalidate();
                        ganttContainer.repaint();
                    });

                    int progress = (int)(((double)(current.burstTime - current.remainingTime) / current.burstTime) * 100);
                    final int progressFinal = progress;
                    final int remaining = current.remainingTime;

                    SwingUtilities.invokeLater(() -> {
                        statusModel.setValueAt("Running (Q" + (levelFinal+1) + ")", idx, 1);
                        statusModel.setValueAt(progressFinal + "%", idx, 2);
                        statusModel.setValueAt(remaining, idx, 3);
                    });

                    try {
                        double speed = speedSlider.getValue() / 10.0;
                        Thread.sleep((long)(500 / speed));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    time++;

                    for (int i = 0; i < n; i++) {
                        Process p = processes.get(i);
                        if (!isInQueue[i] && p.arrivalTime <= time) {
                            queues.get(0).add(p);
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
                        statusModel.setValueAt(currentFinal.waitingTime, idx, 4);
                    });
                } else {
                    int nextLevel = Math.min(level + 1, quantum.length - 1);
                    queues.get(nextLevel).add(current);
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
