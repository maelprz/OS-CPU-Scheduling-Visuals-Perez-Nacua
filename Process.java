import java.awt.Color;

public class Process {
    public int id;
    public String name;
    public int arrivalTime, burstTime, priority, remainingTime;
    public int completionTime, turnaroundTime, waitingTime, responseTime, startTime;
    public Color color;
    public int tableIndex; // Added for accurate status table row referencing

    public Process(int id, String name, int arrivalTime, int burstTime, int priority, int tableIndex) {
        this.id = id;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.startTime = 0;
        this.color = new Color((int)(Math.random() * 0x1000000));
        this.tableIndex = tableIndex;
    }
}
