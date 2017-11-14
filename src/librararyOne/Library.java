package librararyOne;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Library {
    private int visitors;
    private int maxAmount;
    private Random randomReading = new Random();
    private Semaphore semaphore;                                            //семофор для количества людей внутри библиотеки
    private Semaphore doorSemaphore = new Semaphore(1);             //семофор для двери
    private Calendar calendar;

    public Library(int visitors, int maxAmount) {
        this.visitors = visitors;
        this.maxAmount = maxAmount;
        semaphore = new Semaphore(maxAmount);
    }

    public void doorAction(boolean inout) {                                     //работа двери
        calendar = new GregorianCalendar();
        String inOutData;
        String insideOutside;

        if (inout == true) {                                                    //проверка откуда пришел человек
            inOutData = "с улицы";
            insideOutside = "внутрь";
        } else {
            inOutData = "изнутри";
            insideOutside = "наружу";
        }
        try {
            doorSemaphore.acquire();                                            //занимаем очередь на проход
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " подошел к двери " + inOutData + " sec:" + calendar.get(Calendar.SECOND));
        try {
            System.out.println(Thread.currentThread().getName() + " проходит через дверь " + insideOutside + " sec:" + calendar.get(Calendar.SECOND));
            Thread.sleep(500);                                              //время прохода половина секунды
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        calendar = new GregorianCalendar();                                         //обновляем часы
        System.out.println(Thread.currentThread().getName() + " прошел через дверь " + insideOutside + " sec:" + calendar.get(Calendar.SECOND));
        doorSemaphore.release();                                                    //отпускаем дверь
    }

    public void libraryWork() {
        System.out.println("подошел ко входу в библиотеку " + Thread.currentThread().getName());
        if (semaphore.availablePermits() == 0) {                                    //проверка на наличие свободных слотов в библиотеке
            System.out.println(Thread.currentThread().getName() + " ждет входа в библиотеку");
        }
        try {
            semaphore.acquire();                                                    //входим в библиотеку и занимаем 1 место
            doorAction(true);                                                 //проходим через дверь с улицы
            System.out.println("вошел в библиотеку " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readingBook();                                                              //читаем книгу
        doorAction(false);                                                    //выходим из библиотеки через дверь
        System.out.println("вышел из библиотеки " + Thread.currentThread().getName());
        semaphore.release();                                                        //освободили место в библиотеке
    }

    private void readingBook() {
        int randomTime = 1000 + randomReading.nextInt(4000);
        try {
            System.out.println("читает книгу " + Thread.currentThread().getName());
            Thread.sleep(randomTime);                                               //читаем книгу рандомно от 1000 до 5000
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void libraryStart() {
        while (this.visitors != 0) {
            new Thread(() -> libraryWork()).start();                               //запускаем людей в библиотеку
            this.visitors--;                                                        //уменьшаем количество посетителей
        }
    }
}
