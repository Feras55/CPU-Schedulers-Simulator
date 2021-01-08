import java.util.*;

public class RR {

    int timeQuantum;
    int queueSize;
    int contextSwitchTime;
    ArrayList<Process> processesList = new ArrayList<Process>(); ///Info table of the processes
    ArrayList<Process> queue = new ArrayList<Process>(); ///execution queue, (simulating Gantt chart)
    double averageWaitingTime =0.0;
    double averageTurnAroundTime=0.0;
    int timer=0;


    public static class sortByArrivalTime implements Comparator<Process>{

        @Override
        public int compare(Process o1, Process o2) {
            return o1.arrivalTime - o2.arrivalTime;

        }
    }



    void takeInputs(){  //Take processes, arranges them in queue according to their arrival time.
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes");
        queueSize = sc.nextInt();
        System.out.println("Enter Time Quantum");
        timeQuantum = sc.nextInt();
        System.out.println("Enter context switching time");
        contextSwitchTime = sc.nextInt();

        for (int i = 0; i < queueSize; i++) {
            String processName = "P"+(i+1);
            Process p = new Process();
            System.out.println(processName);
            p.name = processName;
            System.out.println("Enter Arrival Time");
            Integer n = sc.nextInt();
            p.arrivalTime=n;
            System.out.println("Enter Burst Time");
            p.curBurstTime = sc.nextInt();
            p.burstTime=p.curBurstTime;

            processesList.add(p);
        }
        Collections.sort(processesList, new sortByArrivalTime());

    }


    void run(){
        takeInputs();
        //take process and run it, all processes came at same time
       executeProcess();
        print();
    }
    void executeProcess(){
        queue.add(processesList.get(0)); ///Add first process to queue
        int processIdx = 1; ///process pointer at second process
        for (int i = 0; i < queue.size() ; i++)
        {
            Process p = queue.get(i);
            ///if P's burst time is <= quantumTime, calculate its exit time, turnAroundTime , and waiting time.
            if(p.curBurstTime <= timeQuantum){
                timer+=p.curBurstTime;
                p.exitTime=timer;
                p.turnAroundTime = p.exitTime - p.arrivalTime;
                p.waitingTime = p.turnAroundTime - p.burstTime;
//                p.curBurstTime = processesList.get(processesList.indexOf(p)).curBurstTime;
//                processesList.set(processesList.indexOf(p),p) ; ///update process p in the processList
                queueSize--; //process is terminated
                if(queueSize>0 && queue.size()==i+1){
                    queue.add(processesList.get(processIdx));
                    processIdx++;
                }

            }else{ ///P's burst time is>quantumTime, add it to the queue with new burstTime
                timer+=timeQuantum;
                p.curBurstTime -=timeQuantum;
                ///check the arrival times of next processes
                while (processIdx < processesList.size() ){
                    if(processesList.get(processIdx).arrivalTime <= timer){ ///if new processes arrived at the time of execution of the current process
                        queue.add(processesList.get(processIdx));
                        processIdx++;
                    }
                    else{
                        break;
                    }
                }
                queue.add(p);
            }
            timer+=contextSwitchTime;
        }
    }

    void print(){
        ///process Execution order
        for (int i = 0; i < queue.size(); i++) {
            System.out.print(queue.get(i).name + " ");
        }
        System.out.println();

        ///Waiting Time for each Process
        System.out.println("Process Name   Waiting Time   Turnaround Time");
        for (int i = 0; i < processesList.size(); i++) {
            Process p = processesList.get(i);
            averageTurnAroundTime+=p.turnAroundTime;
            averageWaitingTime+=p.waitingTime;
            System.out.println(" "+p.name+"\t\t\t\t\t"+p.waitingTime+"\t\t\t\t"+p.turnAroundTime);
        }

        ///calculate Average turn around time and average waiting time
        averageTurnAroundTime = averageTurnAroundTime / processesList.size();
        averageWaitingTime = averageWaitingTime / processesList.size();

        System.out.println("Average Turnaround Time is: " + averageTurnAroundTime);
        System.out.println("Average WaitingTime is: " + averageWaitingTime);
    }


}
