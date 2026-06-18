package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.model.Artist;
import com.pluralsight.concerttracker.model.Concert;
import com.pluralsight.concerttracker.model.Promoter;
import com.pluralsight.concerttracker.model.Venue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class StartupRunner implements CommandLineRunner {

    private final ConcertTrackerService service;
    private final Scanner scanner = new Scanner(System.in);

    public StartupRunner(ConcertTrackerService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        userInterface();
    }

    private void userInterface() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Concert Tracker ===");
            System.out.println("1) List All Concerts");
            System.out.println("0) Quit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> concertsMenu();
                case 0 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void concertsMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Concerts ===");
            System.out.println("1) List all concerts");
            System.out.println("2) View concert by id");
            System.out.println("3) Add a concert");
            System.out.println("4) Update ticket price");
            System.out.println("5) Update tickets sold");
            System.out.println("6) Delete a concert");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> listAllConcerts();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }



    private void listAllConcerts() {
        List<Concert> concerts = service.getAllConcerts();
        System.out.println("\n=== All Concerts ===");
        for (Concert c : concerts) {
            System.out.println(c.getId() + " | " + c.getArtist().getName() + " | " + c.getVenue().getName() + " | " + c.getConcertYear() + " | $" + c.getTicketPrice());
        }
    }



    private void seedData() {
        if (!service.getAllConcerts().isEmpty()) return;

        Venue msg = service.addVenue(new Venue("Madison Square Garden", "New York", 20000));
        Venue ryman = service.addVenue(new Venue("Ryman Auditorium", "Nashville", 2400));
        Venue hollywood = service.addVenue(new Venue("Hollywood Bowl", "Los Angeles", 17500));
        Venue unitedCenter = service.addVenue(new Venue("United Center", "Chicago", 23500));
        Venue barclays = service.addVenue(new Venue("Barclays Center", "New York", 19000));

        Artist taylor = service.addArtist(new Artist("Taylor Swift", "pop"));
        Artist metallica = service.addArtist(new Artist("Metallica", "rock"));
        Artist kendrick = service.addArtist(new Artist("Kendrick Lamar", "hip-hop"));
        Artist boogie = service.addArtist(new Artist("A Boogie Wit da Hoodie", "hip-hop"));
        Artist billie = service.addArtist(new Artist("Billie Eilish", "pop"));
        Artist acdc = service.addArtist(new Artist("AC/DC", "rock"));

        Promoter liveNation = service.addPromoter(new Promoter("Live Nation"));
        Promoter aeg = service.addPromoter(new Promoter("AEG Presents"));
        Promoter roc = service.addPromoter(new Promoter("Roc Nation"));

        service.addConcert(new Concert(2023, 150.00, 19000, taylor, msg, liveNation));
        service.addConcert(new Concert(2023, 95.00, 2400, metallica, ryman, aeg));
        service.addConcert(new Concert(2024, 120.00, 17000, kendrick, hollywood, liveNation));
        service.addConcert(new Concert(2024, 200.00, 17000, taylor, hollywood, aeg));
        service.addConcert(new Concert(2022, 80.00, 2000, metallica, ryman, liveNation));
        service.addConcert(new Concert(2023, 75.00, 18000, boogie, barclays, roc));
        service.addConcert(new Concert(2024, 90.00, 15000, boogie, msg, roc));
        service.addConcert(new Concert(2022, 110.00, 22000, acdc, unitedCenter, aeg));
        service.addConcert(new Concert(2023, 130.00, 19000, billie, barclays, liveNation));
        service.addConcert(new Concert(2024, 145.00, 17500, billie, hollywood, aeg));
        service.addConcert(new Concert(2022, 85.00, 2300, kendrick, ryman, roc));
        service.addConcert(new Concert(2023, 160.00, 23000, acdc, unitedCenter, liveNation));
    }
}
