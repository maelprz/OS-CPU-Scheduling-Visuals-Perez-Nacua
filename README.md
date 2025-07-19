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

## Instructions: How to Run the Simulation

1. Input the Number of Processes
 - After launching the program, the simulation GUI will appear. Enter the number of processes you want to simulate in the provided textbox.
2. Generate the Processes
 - Click the "Generate Process" button beside the textbox. This will add the specified number of processes to the table.
3. Select a Scheduling Algorithm
 - Use the "Select Algorithm" dropdown menu to choose one of the 5 available scheduling algorithms. Your selection will be displayed above under "Algorithm Selected".
4. Run the Simulation!
 - Once you’ve selected an algorithm and generated the processes, press the "Run Simulation" button below the dropdown to begin the simulation.
5. Optional Controls
 - To clear all processes and reset the simulation, click the "Clear Table" button below the "Run Simulation" button. (DONT PRESS this if you have already STARTED the simulation)
 - To delete only the most recently added process, click the "Delete Process" button. This removes the last process from the table only.

---

## Scheduling Algorithms Overview

This simulator implements the following CPU scheduling algorithms:

- **First-Come First-Served (FCFS)**
  - Processes are executed in the order they arrive. It is simple and non-preemptive, making it fair but not always efficient, especially when long processes block shorter ones.

- **Shortest Job First (SJF)**
  - Selects the process with the shortest burst time for execution next. It minimizes average waiting time but is non-preemptive and may cause starvation for longer jobs.

- **Shortest Remaining Time First (SRTF)**
  - A preemptive version of SJF. At every time unit, the process with the smallest remaining execution time is selected. It offers optimal average waiting time but is complex to implement and may lead to starvation.

- **Round Robin (RR)**
  - Each process is given a fixed time quantum in a cyclic order. It is fair and prevents starvation but may have higher turnaround times depending on the chosen quantum.

- **Multilevel Feedback Queue (MLFQ)**
  - Uses multiple queues with different priority levels. Processes can move between queues based on their behavior (e.g., CPU-bound vs I/O-bound). It aims to balance responsiveness and efficiency by dynamically adjusting scheduling decisions.

--- 

## Collaborators

- **Perez, Mel Stephen A.**  
- **Nacua, Raven Earl C.**


## Project Breakdown

  **Nacua, Raven:**
  - Designed the initial overall GUI layout
  - Gantt Chart
  - Simulation Results
  - Time Quantum
  - Simulation Speed
  - SJF
  - SRTF
  - MLFQ
  - Coded and optimized the algorithm logic
  - Debugged and resolved key issues to ensure full functionality and stability

  **Perez, Mel:**
  - Documentaion
  - Better visualization for the process
  - Manual input of number of processes
  - FIFO
  - RR
  - Organized and managed the overall file structure to ensure clarity and reduce confusion during development
  - Refactored codebase by separating scheduling algorithms into individual files for modularity and easier maintenance

  **Known Bugs/Limitations**
  - The simulation can be run again even after completion, which may cause unexpected behavior.
  - Column alignment issues: some text is hidden or misaligned due to improper column sizing.
