import java.util.*;

public class RR2 {
    int timeQuantum;
    int remProcesses;
    int contextSwitchTime;
    ArrayList<Process> processesList = new ArrayList<Process>(); ///Info table of the processes
    ArrayList<Process> ganttChart = new ArrayList<Process>(); ///execution queue, (simulating Gantt chart)
    Queue<Process> RRqueue = new LinkedList<Process>();
    boolean isItSwitchingQuestionMark;
    double averageWaitingTime =0.0;
    double averageTurnAroundTime=0.0;
    int timer=0;

    public static class sortByArrivalTime implements Comparator<Process> {

        @Override
        public int compare(Process o1, Process o2) {
            return o1.arrivalTime - o2.arrivalTime;

        }
    }

    void takeInputs(){  //Take processes, arranges them in queue according to their arrival time.
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes");
        remProcesses = sc.nextInt();
        System.out.println("Enter Time Quantum");
        timeQuantum = sc.nextInt();
        System.out.println("Enter context switching time");
        contextSwitchTime = sc.nextInt();

        for (int i = 0; i < remProcesses; i++) { //remProcesses is queueSize at thee beginning
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
        Collections.sort(processesList, new RR.sortByArrivalTime());

    }

    void run(){
        takeInputs();
        //take process and run it, all processes came at same time
        executeProcess();
        print();
    }
    void executeProcess(){
        ///start process, look at processTable w get
        int contextSwitchTimer=0;
        int quantumTimer=0;
        Process prevProcess = null; ///A process to placed to the back of the queue if its quantum ended,
        // it will not be directly added, so we can check if new process arrived at that same moment
        remProcesses =processesList.size();
        for (timer = 0; remProcesses >0 ; timer++) {
            if(isItSwitchingQuestionMark==true){  ///to handle the case of contextSwitching time
                contextSwitchTimer++;
                if(contextSwitchTimer>=contextSwitchTime){
                    isItSwitchingQuestionMark=false;
                    contextSwitchTimer=0;
                }
                else{
                    continue;
                }
            }

            for (int i = 0; i < processesList.size(); i++) { ///if process arrived, add it to queue
                Process p = processesList.get(i);
                if(p.arrivalTime==timer){
                        RRqueue.add(p);
                }
            }
            if(prevProcess!=null){
                RRqueue.add(prevProcess);
                prevProcess=null;
            }
            if(RRqueue.size()>0) {
                ///fetch process
                Process p = RRqueue.peek();
                if(ganttChart.size()==0 || ganttChart.get(ganttChart.size()-1)!=p){ //check if the same process is being executed
                    ganttChart.add(p);
                }
                p.curBurstTime--;
                quantumTimer++;
                if(p.curBurstTime==0){  //process ended
                    p.turnAroundTime=(timer+1) - p.arrivalTime;
                    p.waitingTime=p.turnAroundTime - p.burstTime;
                    if(RRqueue.size()>0){ ///check if there is a process waiting in any queue
                        if(contextSwitchTime>0){
                            isItSwitchingQuestionMark=true;
                        }
                    }
                    quantumTimer=0;
                    RRqueue.poll();
                    prevProcess = null;
                    remProcesses--;
                    continue;
                }
                //quantum time ended and process didn't end
                else if (timer > 0 && quantumTimer % timeQuantum == 0) {
                    if(RRqueue.size()>1){ //It is not the only process in the queue
//                        RRqueue.add(p);
                        prevProcess = p;
                        RRqueue.poll();
                        isItSwitchingQuestionMark=true;
                        continue;
                    }
                }
            }
                continue;
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



}
