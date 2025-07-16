public class Process {
    public int id;
    public String name;
    public int arrivalTime, burstTime, priority, remainingTime;
    public int completionTime, turnaroundTime, waitingTime, responseTime;

    public Process(int id, String name, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }
}
