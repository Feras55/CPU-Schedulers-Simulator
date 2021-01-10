import java.util.Collections;
import java.util.Scanner;

public class SJF {
    Priority sjfPriority = new Priority();


    void takeInputs(){  //Take processes, arranges them in queue according to their arrival time.
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes");
        sjfPriority.remProcesses = sc.nextInt();
        System.out.println("Enter context switching time");
        sjfPriority.contextSwitchTime = sc.nextInt();
        for (int i = 0; i < sjfPriority.remProcesses; i++) {
            String processName = "P"+(i+1);
            Process p = new Process();
            System.out.println(processName);
            p.name = processName;
            System.out.println("Enter Arrival Time");
            Integer n = sc.nextInt();
            p.arrivalTime=n;
            System.out.println("Enter Burst Time");
            p.curBurstTime = sc.nextInt();
            p.priority=p.burstTime=p.curBurstTime;
            sjfPriority.processesList.add(p);
        }
        Collections.sort(sjfPriority.processesList, new Priority.sortByArrivalTime());

    }
    void run(){
        takeInputs();
        sjfPriority.executeProcess();
        sjfPriority.print();

    }
}
