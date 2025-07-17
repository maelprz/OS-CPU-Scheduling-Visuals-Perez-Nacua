## Project Overview: CPU Scheduling Visualization Simulator

This project, developed for our Operating Systems class under **Engr. Violdan E. Bayocot**, is a Java-based simulation tool that visually demonstrates various CPU scheduling algorithms. The simulator aims to help users understand how scheduling strategies affect process execution, turnaround time, and waiting time.

The system provides an interactive graphical interface using **Java Swing**, where users can:

- Add or generate random processes with arrival time, execution time, and priority
- Choose from common scheduling algorithms:
  - First-Come First-Served (FCFS)
  - Shortest Job First (SJF)
  - Shortest Remaining Time First (SRTF)
  - Round Robin (RR)
  - Multilevel Feedback Queue (MLFQ)
- Visualize execution through a dynamic Gantt chart
- Monitor process status, completion, and performance metrics in real time

This educational tool aims to enhance the understanding of CPU scheduling behavior through hands-on experimentation and immediate visual feedback.

---

## Objectives

- Illustrate how different CPU scheduling algorithms work
- Strengthen understanding of OS concepts like turnaround time, waiting time, and context switching
- Provide a user-friendly, visual tool for teaching and experimentation

---

## Features

- Java Swing GUI with real-time visualization
- Supports multiple scheduling algorithms:
  - FCFS, SJF, SRTF, Round Robin (with configurable quantum), MLFQ 
- Live Gantt chart updates and process status tracking
- Adjustable simulation speed
- Random or manual process generation
- Displays average turnaround time, waiting time, and total execution time

---

## Scheduling Algorithms Overview

This simulator implements the following CPU scheduling algorithms:

- First-Come First-Served (FCFS)
Processes are executed in the order they arrive. It is simple and non-preemptive, making it fair but not always efficient, especially when long processes block shorter ones.

- Shortest Job First (SJF)
Selects the process with the shortest burst time for execution next. It minimizes average waiting time but is non-preemptive and may cause starvation for longer jobs.

- Shortest Remaining Time First (SRTF)
A preemptive version of SJF. At every time unit, the process with the smallest remaining execution time is selected. It offers optimal average waiting time but is complex to implement and may lead to starvation.

- Round Robin (RR)
Each process is given a fixed time quantum in a cyclic order. It is fair and prevents starvation but may have higher turnaround times depending on the chosen quantum.

- Multilevel Feedback Queue (MLFQ)
Uses multiple queues with different priority levels. Processes can move between queues based on their behavior (e.g., CPU-bound vs I/O-bound). It aims to balance responsiveness and efficiency by dynamically adjusting scheduling decisions.

## Collaborators

- **Perez, Mel Stephen A.**  
- **Nacua, Raven Earl C.**




Project Breakdown

  Nacua, Raven:
  Initial GUI
  SJF
  SRTF
  MLFQ
  Resolved issues in the algorithm to make it fully functional and stable.

  Perez, Mel:
  Documentaion
  FIFO
  RR
  Organized and managed the overall file structure to ensure clarity and reduce confusion during development
  Refactored codebase by separating scheduling algorithms into individual files for modularity and easier maintenance