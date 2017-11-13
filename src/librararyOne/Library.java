package librararyOne;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Library {
    int visitors;
    int maxAmount;
    Random randomReading = new Random();
    Semaphore semaphore;
    Semaphore doorSemaphore = new Semaphore(1);
    Calendar calendar;


    public Library(int visitors, int maxAmount) {
        this.visitors = visitors;
        this.maxAmount = maxAmount;
        semaphore = new Semaphore(maxAmount);
    }

    public void doorAction(boolean inout) {
        calendar = new GregorianCalendar();
        String inOutData;
        String insideOutside;

        if (inout == true) {
            inOutData = "с улицы";
            insideOutside = "внутрь";
        } else {
            inOutData = "изнутри";
            insideOutside = "наружу";
        }
        try {
            doorSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " подошел к двери " + inOutData + " sec:" + calendar.get(Calendar.SECOND));
        try {
            System.out.println(Thread.currentThread().getName() + " проходит через дверь " + insideOutside + " sec:" + calendar.get(Calendar.SECOND));
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        calendar = new GregorianCalendar();
        System.out.println(Thread.currentThread().getName() + " прошел через дверь " + insideOutside + " sec:" + calendar.get(Calendar.SECOND));
        doorSemaphore.release();
    }

    public void libraryWork() {
        System.out.println("подошел ко входу в библиотеку " + Thread.currentThread().getName());
        if (semaphore.availablePermits() == 0) {
            System.out.println(Thread.currentThread().getName() + " ждет входа в библиотеку");
        }
        try {
            semaphore.acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doorAction(true);
        System.out.println("вошел в библиотеку " + Thread.currentThread().getName());
        readingBook();
        doorAction(false);
        semaphore.release();
        System.out.println("вышел из библиотеки " + Thread.currentThread().getName());
    }

    private void readingBook() {
        int randomTime = randomReading.nextInt(4000) + 1000;
        try {
            System.out.println("читает книгу " + Thread.currentThread().getName());
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void libraryStart() {

        while (this.visitors != 0) {

            new Thread(() ->libraryWork()).start();

            this.visitors--;
        }
    }

}
