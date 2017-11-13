package librararyOne;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Library {
    int visitors;
    int maxAmount;
    Random randomReading = new Random();


    public Library(int visitors, int maxAmount) {
        this.visitors = visitors;
        this.maxAmount = maxAmount;

    }

    public void libraryStart() {
        Semaphore semaphore = new Semaphore(maxAmount);

        Runnable runnable1 = () -> {


            System.out.println("подошел ко входу в библиотеку " + Thread.currentThread().getName());
            if (semaphore.availablePermits() == 0) {
                System.out.println(Thread.currentThread().getName() + " ждет входа в библиотеку");
            }
            try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            visitorWork();
            semaphore.release();


        };

        while (this.visitors != 0) {

            new Thread(runnable1).start();

            this.visitors--;
        }
    }

    private void visitorWork() {
        int randomTime = randomReading.nextInt(4000) + 1000;
        System.out.println("вошел в библиотеку " + Thread.currentThread().getName());
        try {
            System.out.println("читает книгу " + Thread.currentThread().getName());
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("вышел из библиотеки " + Thread.currentThread().getName());
    }
}
