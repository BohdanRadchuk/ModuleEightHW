package librararyOne;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Library {
    int visitors;
    int maxAmount;
    Semaphore semaphore = new Semaphore(maxAmount);

    public Library(int visitors, int maxAmount) {
        this.visitors= visitors;
        this.maxAmount = maxAmount;
    }

    public void libraryStart (){


        while (this.visitors!=0){
            System.out.println("подошел ко входу в библиотеку");

        //for (int i = 0; i<visitors; i++) {

            new Thread(() -> visitorWork()).start();

            this.visitors--;
        }
    }

    private void visitorWork(){

        Random randomReading = new Random();
        try {
            semaphore.acquire(maxAmount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int randomTime = randomReading.nextInt(4000) + 1000;
        System.out.println("вошел в библиотеку" + Thread.currentThread().getName());
        try {
            System.out.println("читает книгу");
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("вышел из библиотеки");
        semaphore.release();

    }

}
