package TopBrainsException_FileHandling;

import java.io.*;
import java.util.*;

public class DigitalLibraryQuotes {

    private static final String file_name = "D:\\Java Training\\FileHandling\\F1\\quotes.txt";
    private static Scanner sc  = new Scanner(System.in);

    public static void main(String[] args) {
        createFileIfNotExists();

        while (true) {
            System.out.println("\n--- Digital Library for Quotes & Sayings ---");
            System.out.println("1. Add a New Quote");
            System.out.println("2. View All Quotes");
            System.out.println("3. Search Quotes by Keyword");
            System.out.println("4. Sort Quotes Alphabetically");
            System.out.println("5. Quote of the Day");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addQuote();
                    break;

                case 2:
                    viewAllQuotes();
                    break;

                case 3:
                    searchQuotes();
                    break;

                case 4:
                    sortQuotes();
                    break;

                case 5:
                    quoteOfTheDay();
                    break;

                case 6:
                    System.out.println("Thank you for using the Digital Library!");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createFileIfNotExists() {
        try {
            File file = new File(file_name);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file.");
        }
    }

    private static void addQuote() {
        System.out.print("Enter quote: ");
        String quote = sc.nextLine();

        try (FileWriter writer = new FileWriter(file_name, true)) {
            writer.write(quote + System.lineSeparator());
            System.out.println("Quote added successfully!");
        } catch (IOException e) {
            System.out.println("Error saving the quote.");
        }
    }

    private static void viewAllQuotes() {
        System.out.println("\n---- All Quotes ----");

        try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
            String line;
            boolean empty = true;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                empty = false;
            }

            if (empty) {
                System.out.println("No quotes available.");
            }
        } catch (IOException e) {
            System.out.println("Error reading quotes.");
        }
    }

    private static void searchQuotes() {
        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine().toLowerCase();

        System.out.println("\n---- Search Results ----");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No results found.");
            }
        } catch (IOException e) {
            System.out.println("Error searching quotes.");
        }
    }

    private static void sortQuotes() {
        List<String> quotes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                quotes.add(line);
            }

            if (quotes.isEmpty()) {
                System.out.println("No quotes to sort.");
                return;
            }

            Collections.sort(quotes);

            System.out.println("\n---- Sorted Quotes ----");
            for (String quote : quotes) {
                System.out.println(quote);
            }

            try (FileWriter writer = new FileWriter(file_name)) {
                for (String quote : quotes) {
                    writer.write(quote + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            System.out.println("Error sorting quotes.");
        }
    }

    private static void quoteOfTheDay() {
        List<String> quotes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                quotes.add(line);
            }

            if (quotes.isEmpty()) {
                System.out.println("No quotes available.");
                return;
            }

            Random random = new Random();
            String randomQuote = quotes.get(random.nextInt(quotes.size()));

            System.out.println("\n---- Quote of the Day ----");
            System.out.println(randomQuote);

        } catch (IOException e) {
            System.out.println("Error generating Quote of the Day.");
        }
    }
}