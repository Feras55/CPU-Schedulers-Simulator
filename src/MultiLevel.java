import java.util.*;

public class MultiLevel {
    int timeQuantum;
    int remProcesses;
    int contextSwitchTime;
    ArrayList<Process> processesList = new ArrayList<Process>(); ///Info table of the processes
    ArrayList<Process> ganttChart = new ArrayList<Process>(); ///execution queue, (simulating Gantt chart)
    Queue<Process>RRqueue = new LinkedList<Process>();
    Queue<Process>FCFSqueue = new LinkedList<Process>();
    boolean isItSwitchingQuestionMark;

    double averageWaitingTime =0.0;
    double averageTurnAroundTime=0.0;
    int timer=0;


    public static class sortByArrivalTime implements Comparator<Process> {

        @Override
        public int compare(Process o1, Process o2) {  ///If arrival times are equal, favor queue 1 process
            if(o1.arrivalTime - o2.arrivalTime == 0){
                return o1.queueNumber - o2.queueNumber;
            }
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
            System.out.println("Enter queue number (1. RR - 2.FCFS):");
            p.queueNumber = sc.nextInt();
            processesList.add(p);
        }
        Collections.sort(processesList, new MultiLevel.sortByArrivalTime());

    }
    void run(){
        takeInputs();
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
                    contextSwitchTimer++;
                continue;
                }
            }

            for (int i = 0; i < processesList.size(); i++) { ///if process arrived, add it to its queue
                    Process p = processesList.get(i);
                    if(p.arrivalTime==timer){
                        if(p.queueNumber==1){
                            RRqueue.add(p);
                        }else{
                            FCFSqueue.add(p);
                        }
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
                        if(RRqueue.size()>0 || FCFSqueue.size()>0){ ///check if there is a process waiting in any queue
                           if(contextSwitchTime>0){
                            isItSwitchingQuestionMark=true;
                           }
                        }
                        quantumTimer=0;
                        prevProcess = null;
                        RRqueue.poll();
                        remProcesses--;
                        continue;
                    }
                    //quantum time ended and process didn't end
                    else if (timer > 0 && quantumTimer % timeQuantum == 0) {
                        if(RRqueue.size()>1){ //It is not the only process in the queue
//                           RRqueue.add(p);
                            prevProcess = p;
                           RRqueue.poll();
                           isItSwitchingQuestionMark=true;
                           continue;
                       }

                    }



            }
            else if(FCFSqueue.size()>0){  ///else Fetch FCFS
                quantumTimer=0; //an FCFS process is under exeuction, reset the quantumTimer in case interruption happened
                Process p = FCFSqueue.peek();
                if(ganttChart.size()==0 || ganttChart.get(ganttChart.size()-1)!=p){
                ganttChart.add(p);
                }
                p.curBurstTime--;
                if(p.curBurstTime==0){  //process ended
                    p.turnAroundTime=(timer+1) - p.arrivalTime;
                    p.waitingTime=p.turnAroundTime - p.burstTime;
                    processesList.set(processesList.indexOf(p),p) ; ///update process p with waiting time and turn around time in the processList
                    if(RRqueue.size()>0 || FCFSqueue.size()>0){ ///check if there is a process waiting in any queue
                        if(contextSwitchTime>0){

                            isItSwitchingQuestionMark=true;
                        }

                    }
                    remProcesses--;
                    FCFSqueue.poll();
                }
                    continue;


            }

        }
    }

    void print(){
        ///process Execution order
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
