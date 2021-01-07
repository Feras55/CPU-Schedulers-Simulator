public  class Process implements Comparable<Process> {

    String name;
    int arrivalTime;
    int curBurstTime;
    int burstTime;
    int exitTime;
    int turnAroundTime;
    int waitingTime;

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    @Override
    public int compareTo(Process o) {
        {
            if(this.arrivalTime < o.arrivalTime){
                return  1;
            }
            else if(this.arrivalTime == o.arrivalTime)
            {
                return  0;
            }
            else{
                return -1;
            }
        }
    }



    public Process() {

    }
}
