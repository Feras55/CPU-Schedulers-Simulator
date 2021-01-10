import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean work = true;
        Scanner sc = new Scanner(System.in);
        while(work){
        System.out.println("Choose the desired algorithm to run\n" +
                "1. Shortest-Job First (SJF)\n " +
                "2. Round Robin (RR)\n"+
                "3. Priority Scheduling\n"+
                "4. Multi level\n"+
                "5. Exit");
        int choice = sc.nextInt();
        switch (choice){
            case 1:
                SJF scheduler1 = new SJF();
               // scheduler1.run();
                break;
            case 2:
                RR2 scheduler2 = new RR2();
                scheduler2.run();
                break;
            case 3:
                Priority scheduler3 = new Priority();
                scheduler3.run();
                break;
            case 4:
                MultiLevel scheduler4 = new MultiLevel();
                scheduler4.run();
                break;
            case 5:
                work = false;
                break;
            default:
                System.out.println("Choose 1-5 based on desired algorithm");
            }
        }
    }
}
