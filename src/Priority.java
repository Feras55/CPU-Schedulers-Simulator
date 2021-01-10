import java.util.*;

public class Priority {
    ArrayList<Process> processesList = new ArrayList<Process>(); ///Info table of the processes
    ArrayList<Process> ganttChart = new ArrayList<Process>(); ///execution queue, (simulating Gantt chart)
    ArrayList<Process> queue = new ArrayList<Process>();
    int contextSwitchTime;
    boolean isItSwitchingQuestionMark;
    int remProcesses; ///starts as total number of processes, then acts as the remainder processes, where it decreases whenever a process terminates
    double averageWaitingTime =0.0;
    double averageTurnAroundTime=0.0;
    int timer=0;

    public static class sortByArrivalTime implements Comparator<Process> { ///sort the table by arrival time, then priority

        @Override
        public int compare(Process o1, Process o2) {  ///If arrival times are equal, favor queue 1 process
            if(o1.arrivalTime - o2.arrivalTime == 0){
                return o1.priority - o2.priority;
            }
            return o1.arrivalTime - o2.arrivalTime;

        }

    }


    void takeInputs(){  //Take processes, arranges them in queue according to their arrival time.
        Scanner sc = new Scanner(System.in);
        contextSwitchTime = 0;
        System.out.println("Enter number of processes");
        remProcesses = sc.nextInt();

        for (int i = 0; i < remProcesses; i++) {
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
            System.out.println("Enter priority (0 is the highest probability)");
            p.priority = sc.nextInt();
            processesList.add(p);
        }
        Collections.sort(processesList, new Priority.sortByArrivalTime());

    }

    //A process ages if it has been in the queue for multiples of 10 seconds
    void aging(){
        for (Process p : queue) {
            if ((timer - p.arrivalTime) % 10 == 0 && p.priority>0){
                p.priority--;
            }
        }

    }
    void executeProcess(){
        int contextSwitchTimer=0;
        isItSwitchingQuestionMark=false;
        remProcesses =processesList.size(); ///Starts as total size
        for (timer = 0; remProcesses >0 ; timer++) {
            if(timer%10==0){ //Checks for starvation every 10 seconds
                aging();
            }
            if(isItSwitchingQuestionMark==true){  ///to handle the case of contextSwitching time
                contextSwitchTimer++;
                if(contextSwitchTimer>=contextSwitchTime){
                    isItSwitchingQuestionMark=false;
                    contextSwitchTimer=0;
                }
                else{
                    contextSwitchTimer++;
                    continue;
                }
            }

            for (int i = 0; i < processesList.size(); i++) { ///if process arrived, add it to queue
                Process p = processesList.get(i);
                if (p.arrivalTime == timer) {
                    int j;
                    for (j = queue.size() - 1; j >= 0 && p.priority < queue.get(j).priority; j--)
                        ; //finds place for the process according to its priority
                    queue.add(j + 1, p);
                }

            }

            if (queue.size() > 0) {
                Process p = queue.get(0);
                p.curBurstTime--;
                if(ganttChart.size()==0 || ganttChart.get(ganttChart.size()-1)!=p){ //check if the same process is being executed
                    ganttChart.add(p);
                }
                if(p.curBurstTime==0){  //process ended
                    p.turnAroundTime=(timer+1) - p.arrivalTime;
                    p.waitingTime=p.turnAroundTime - p.burstTime;
                    if(queue.size()>1){
                        //There are more processes in the queue
                        if(contextSwitchTime>0){
                            isItSwitchingQuestionMark=true;
                        }
                    }
                    queue.remove(0);
                    remProcesses--;

                    continue;
                }


            }
        }

    }

    void print(){
        ///process Execution order
        System.out.print("Gantt Chart: ");
        for (int i = 0; i < ganttChart.size(); i++) {
            System.out.print(ganttChart.get(i).name + " ");
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
    void run(){
        takeInputs();
        executeProcess();
        print();

    }


}
