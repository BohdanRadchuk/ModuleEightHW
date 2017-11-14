import librararyOne.Library;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("enter people amount");
        int peopleCount = scanner.nextInt();
        System.out.println("enter Max people amount in library at the same time");
        int maxAmount = scanner.nextInt();
        Library library = new Library(peopleCount, maxAmount);
        library.libraryStart();
    }
}
