package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.ArtistRepository;
import com.pluralsight.concerttracker.data.ConcertRepository;
import com.pluralsight.concerttracker.data.PromoterRepository;
import com.pluralsight.concerttracker.data.VenueRepository;
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

    private final VenueRepository venueRepository;
    private final ArtistRepository artistRepository;
    private final PromoterRepository promoterRepository;
    private final ConcertRepository concertRepository;
    private final ConcertTrackerService service;
    private final Scanner scanner = new Scanner(System.in);

    public StartupRunner(VenueRepository venueRepository,
                         ArtistRepository artistRepository,
                         PromoterRepository promoterRepository,
                         ConcertRepository concertRepository,
                         ConcertTrackerService service) {
        this.venueRepository = venueRepository;
        this.artistRepository = artistRepository;
        this.promoterRepository = promoterRepository;
        this.concertRepository = concertRepository;
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
                case 1 -> listAllConcerts();
                case 0 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
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
        if (concertRepository.count() > 0) return;

        Venue msg = venueRepository.save(new Venue("Madison Square Garden", "New York", 20000));
        Venue ryman = venueRepository.save(new Venue("Ryman Auditorium", "Nashville", 2400));
        Venue hollywood = venueRepository.save(new Venue("Hollywood Bowl", "Los Angeles", 17500));
        Venue unitedCenter = venueRepository.save(new Venue("United Center", "Chicago", 23500));
        Venue barclays = venueRepository.save(new Venue("Barclays Center", "New York", 19000));

        Artist taylor = artistRepository.save(new Artist("Taylor Swift", "pop"));
        Artist metallica = artistRepository.save(new Artist("Metallica", "rock"));
        Artist kendrick = artistRepository.save(new Artist("Kendrick Lamar", "hip-hop"));
        Artist boogie = artistRepository.save(new Artist("A Boogie Wit da Hoodie", "hip-hop"));
        Artist billie = artistRepository.save(new Artist("Billie Eilish", "pop"));
        Artist acdc = artistRepository.save(new Artist("AC/DC", "rock"));

        Promoter liveNation = promoterRepository.save(new Promoter("Live Nation"));
        Promoter aeg = promoterRepository.save(new Promoter("AEG Presents"));
        Promoter roc = promoterRepository.save(new Promoter("Roc Nation"));

        concertRepository.save(new Concert(2023, 150.00, 19000, taylor, msg, liveNation));
        concertRepository.save(new Concert(2023, 95.00, 2400, metallica, ryman, aeg));
        concertRepository.save(new Concert(2024, 120.00, 17000, kendrick, hollywood, liveNation));
        concertRepository.save(new Concert(2024, 200.00, 17000, taylor, hollywood, aeg));
        concertRepository.save(new Concert(2022, 80.00, 2000, metallica, ryman, liveNation));
        concertRepository.save(new Concert(2023, 75.00, 18000, boogie, barclays, roc));
        concertRepository.save(new Concert(2024, 90.00, 15000, boogie, msg, roc));
        concertRepository.save(new Concert(2022, 110.00, 22000, acdc, unitedCenter, aeg));
        concertRepository.save(new Concert(2023, 130.00, 19000, billie, barclays, liveNation));
        concertRepository.save(new Concert(2024, 145.00, 17500, billie, hollywood, aeg));
        concertRepository.save(new Concert(2022, 85.00, 2300, kendrick, ryman, roc));
        concertRepository.save(new Concert(2023, 160.00, 23000, acdc, unitedCenter, liveNation));
    }
}
