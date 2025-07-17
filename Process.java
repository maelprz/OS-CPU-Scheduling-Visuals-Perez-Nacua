public class Process {
    public int id;
    public String name;
    public int arrivalTime, burstTime, priority, remainingTime;
    public int completionTime, turnaroundTime, waitingTime, responseTime, startTime;

    public Process(int id, String name, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.name = name;
        this.arrivalTime = 0;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.startTime = 0;
    }
}
