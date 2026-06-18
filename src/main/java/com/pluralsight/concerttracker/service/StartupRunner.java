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
            System.out.println("1) Concert Menu");
            System.out.println("2) Search Concerts");
            System.out.println("3) Artists");
            System.out.println("4) Venues");
            System.out.println("5) Promoters");
            System.out.println("6) Reports");
            System.out.println("0) Quit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> concertsMenu();
                case 2 -> searchConcertsMenu();
                case 3 -> artistsMenu();
                case 4 -> venuesMenu();
                case 5 -> promotersMenu();
                case 6 -> reportsMenu();
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
                case 2 -> viewConcertById();
                case 3 -> addConcert();
                case 4 -> updateTicketPrice();
                case 5 -> updateTicketsSold();
                case 6 -> deleteConcert();
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

    private void viewConcertById() {
        System.out.print("Enter concert id: ");
        long id = Long.parseLong(scanner.nextLine());
        Concert c = service.getConcertById(id);
        if (c == null) {
            System.out.println("No concert with that id.");
        } else {
            System.out.println(c.getId() + " | " + c.getArtist().getName() + " | " + c.getVenue().getName() + " | " +
                    c.getPromoter().getName() + " | " + c.getConcertYear() + " | $" + c.getTicketPrice() + " | Sold: " + c.getTicketsSold());
        }
    }

    private void addConcert() {
        System.out.println("\nAvailable Artists:");
        for (Artist a : service.getAllArtists()) {
            System.out.println(a.getId() + ") " + a.getName());
        }
        System.out.print("Choose artist id: ");
        long artistId = Long.parseLong(scanner.nextLine());

        System.out.println("\nAvailable Venues:");
        for (Venue v : service.getAllVenues()) {
            System.out.println(v.getId() + ") " + v.getName() + " - " + v.getCity() + " (capacity: " + v.getCapacity() + ")");
        }
        System.out.print("Choose venue id: ");
        long venueId = Long.parseLong(scanner.nextLine());

        System.out.println("\nAvailable Promoters:");
        for (Promoter p : service.getAllPromoters()) {
            System.out.println(p.getId() + ") " + p.getName());
        }
        System.out.print("Choose promoter id: ");
        long promoterId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter ticket price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter tickets sold: ");
        int ticketsSold = Integer.parseInt(scanner.nextLine());

        Artist artist = service.getAllArtists().stream()
                .filter(a -> a.getId() == artistId)
                .findFirst()
                .orElse(null);
        Venue venue = service.getAllVenues().stream()
                .filter(v -> v.getId() == venueId)
                .findFirst()
                .orElse(null);
        Promoter promoter = service.getAllPromoters().stream()
                .filter(p -> p.getId() == promoterId)
                .findFirst()
                .orElse(null);

        if (artist == null || venue == null || promoter == null) {
            System.out.println("Invalid artist, venue, or promoter id.");
            return;
        }
        if (price < 0 || ticketsSold < 0) {
            System.out.println("Price and tickets sold cannot be negative.");
            return;
        }
        if (ticketsSold > venue.getCapacity()) {
            System.out.println("Tickets sold cannot exceed venue capacity of " + venue.getCapacity() + ".");
            return;
        }

        Concert c = service.addConcert(new Concert(year, price, ticketsSold, artist, venue, promoter));
        System.out.println("Concert added with id " + c.getId() + ".");
    }

    private void updateTicketPrice() {
        System.out.print("Enter concert id: ");
        long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new ticket price: ");
        double price = Double.parseDouble(scanner.nextLine());
        Concert c = service.updateTicketPrice(id, price);
        if (c == null) {
            System.out.println("Could not update. Check id exists and price is not negative.");
        } else {
            System.out.println("Ticket price updated.");
        }
    }

    private void updateTicketsSold() {
        System.out.print("Enter concert id: ");
        long id = Long.parseLong(scanner.nextLine());
        System.out.print("Enter new tickets sold: ");
        int ticketsSold = Integer.parseInt(scanner.nextLine());
        Concert c = service.updateTicketsSold(id, ticketsSold);
        if (c == null) {
            System.out.println("Could not update. Check id exists, count is not negative, and does not exceed venue capacity.");
        } else {
            System.out.println("Tickets sold updated.");
        }
    }

    private void deleteConcert() {
        System.out.print("Enter concert id to delete: ");
        long id = Long.parseLong(scanner.nextLine());
        Concert c = service.getConcertById(id);
        if (c == null) {
            System.out.println("No concert with that id.");
        } else {
            service.deleteConcert(id);
            System.out.println("Concert deleted.");
        }
    }

    private void venuesMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Venues ===");
            System.out.println("1) List all venues");
            System.out.println("2) Add a venue");
            System.out.println("3) Find by city");
            System.out.println("4) Find by name");
            System.out.println("5) Find by minimum capacity");
            System.out.println("6) Update capacity");
            System.out.println("7) Delete a venue");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    for (Venue v : service.getAllVenues()) {
                        System.out.println(v.getId() + " | " + v.getName() + " | " + v.getCity() + " | capacity: " + v.getCapacity());
                    }
                }
                case 2 -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("City: ");
                    String city = scanner.nextLine();
                    System.out.print("Capacity: ");
                    int cap = Integer.parseInt(scanner.nextLine());
                    service.addVenue(new Venue(name, city, cap));
                    System.out.println("Venue added.");
                }
                case 3 -> {
                    System.out.print("City: ");
                    String city = scanner.nextLine();
                    List<Venue> results = service.findVenuesByCity(city);
                    if (results.isEmpty()) {
                        System.out.println("No venues found.");
                    } else {
                        for (Venue v : results) {
                            System.out.println(v.getId() + " | " + v.getName() + " | " + v.getCity());
                        }
                    }
                }
                case 4 -> {
                    System.out.print("Name contains: ");
                    String name = scanner.nextLine();
                    List<Venue> results = service.findVenuesByName(name);
                    if (results.isEmpty()) {
                        System.out.println("No venues found.");
                    } else {
                        for (Venue v : results) {
                            System.out.println(v.getId() + " | " + v.getName() + " | " + v.getCity());
                        }
                    }
                }
                case 5 -> {
                    System.out.print("Min capacity: ");
                    int cap = Integer.parseInt(scanner.nextLine());
                    List<Venue> results = service.findVenuesByMinCapacity(cap);
                    if (results.isEmpty()) {
                        System.out.println("No venues found.");
                    } else {
                        for (Venue v : results) {
                            System.out.println(v.getId() + " | " + v.getName() + " | capacity: " + v.getCapacity());
                        }
                    }
                }
                case 6 -> {
                    System.out.print("Venue id: ");
                    long id = Long.parseLong(scanner.nextLine());
                    System.out.print("New capacity: ");
                    int cap = Integer.parseInt(scanner.nextLine());
                    Venue v = service.updateVenueCapacity(id, cap);
                    if (v == null) {
                        System.out.println("Not found or invalid.");
                    } else {
                        System.out.println("Updated.");
                    }
                }
                case 7 -> {
                    System.out.print("Venue id: ");
                    long id = Long.parseLong(scanner.nextLine());
                    Venue v = service.getAllVenues().stream()
                            .filter(x -> x.getId() == id)
                            .findFirst()
                            .orElse(null);
                    if (v == null) {
                        System.out.println("Not found.");
                    } else {
                        service.deleteVenue(id);
                        System.out.println("Deleted.");
                    }
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void artistsMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Artists ===");
            System.out.println("1) List all artists");
            System.out.println("2) Add an artist");
            System.out.println("3) Find by genre");
            System.out.println("4) Find by name");
            System.out.println("5) Update genre");
            System.out.println("6) Delete an artist");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    for (Artist a : service.getAllArtists()) {
                        System.out.println(a.getId() + " | " + a.getName() + " | " + a.getGenre());
                    }
                }
                case 2 -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();
                    service.addArtist(new Artist(name, genre));
                    System.out.println("Artist added.");
                }
                case 3 -> {
                    System.out.print("Genre: ");
                    String genre = scanner.nextLine();
                    List<Artist> results = service.findArtistsByGenre(genre);
                    if (results.isEmpty()) {
                        System.out.println("No artists found.");
                    } else {
                        for (Artist a : results) {
                            System.out.println(a.getId() + " | " + a.getName() + " | " + a.getGenre());
                        }
                    }
                }
                case 4 -> {
                    System.out.print("Name contains: ");
                    String name = scanner.nextLine();
                    List<Artist> results = service.findArtistsByName(name);
                    if (results.isEmpty()) {
                        System.out.println("No artists found.");
                    } else {
                        for (Artist a : results) {
                            System.out.println(a.getId() + " | " + a.getName() + " | " + a.getGenre());
                        }
                    }
                }
                case 5 -> {
                    System.out.print("Artist id: ");
                    long id = Long.parseLong(scanner.nextLine());
                    System.out.print("New genre: ");
                    String genre = scanner.nextLine();
                    Artist a = service.updateArtistGenre(id, genre);
                    if (a == null) {
                        System.out.println("Not found.");
                    } else {
                        System.out.println("Updated.");
                    }
                }
                case 6 -> {
                    System.out.print("Artist id: ");
                    long id = Long.parseLong(scanner.nextLine());
                    Artist a = service.getAllArtists().stream()
                            .filter(x -> x.getId() == id)
                            .findFirst()
                            .orElse(null);
                    if (a == null) {
                        System.out.println("Not found.");
                    } else {
                        service.deleteArtist(id);
                        System.out.println("Deleted.");
                    }
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void promotersMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Promoters ===");
            System.out.println("1) List all promoters");
            System.out.println("2) Add a promoter");
            System.out.println("3) Find by name");
            System.out.println("4) Delete a promoter");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    for (Promoter p : service.getAllPromoters()) {
                        System.out.println(p.getId() + " | " + p.getName());
                    }
                }
                case 2 -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    service.addPromoter(new Promoter(name));
                    System.out.println("Promoter added.");
                }
                case 3 -> {
                    System.out.print("Name contains: ");
                    String name = scanner.nextLine();
                    List<Promoter> results = service.findPromotersByName(name);
                    if (results.isEmpty()) {
                        System.out.println("No promoters found.");
                    } else {
                        for (Promoter p : results) {
                            System.out.println(p.getId() + " | " + p.getName());
                        }
                    }
                }
                case 4 -> {
                    System.out.print("Promoter id: ");
                    long id = Long.parseLong(scanner.nextLine());
                    Promoter p = service.getAllPromoters().stream()
                            .filter(x -> x.getId() == id)
                            .findFirst()
                            .orElse(null);
                    if (p == null) {
                        System.out.println("Not found.");
                    } else {
                        service.deletePromoter(id);
                        System.out.println("Deleted.");
                    }
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void searchConcertsMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Search Concerts ===");
            System.out.println("1) By year");
            System.out.println("2) By artist");
            System.out.println("3) By venue");
            System.out.println("4) By city");
            System.out.println("5) By maximum price");
            System.out.println("6) By price range");
            System.out.println("7) By max price + earliest year");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter year: ");
                    int year = Integer.parseInt(scanner.nextLine());
                    List<Concert> results = service.findConcertsByYear(year);
                    if (results.isEmpty()) {
                        System.out.println("No concerts found.");
                    } else {
                        for (Concert c : results) {
                            printConcert(c);
                        }
                    }
                }
                case 2 -> {
                    System.out.print("Artist name contains: ");
                    String name = scanner.nextLine();
                    List<Concert> results = service.findConcertsByArtistName(name);
                    if (results.isEmpty()) {
                        System.out.println("No concerts found.");
                    } else {
                        for (Concert c : results) {
                            printConcert(c);
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Venue name contains: ");
                    String name = scanner.nextLine();
                    List<Concert> results = service.findConcertsByVenueName(name);
                    if (results.isEmpty()) {
                        System.out.println("No concerts found.");
                    } else {
                        for (Concert c : results) {
                            printConcert(c);
                        }
                    }
                }
                case 4 -> {
                    System.out.print("Enter city: ");
                    String city = scanner.nextLine();
                    List<Concert> results = service.findConcertsByCity(city);
                    if (results.isEmpty()) {
                        System.out.println("No concerts found.");
                    } else {
                        for (Concert c : results) {
                            printConcert(c);
                        }
                    }
                }
                case 5 -> {
                    System.out.print("Enter max price: ");
                    double maxPrice = Double.parseDouble(scanner.nextLine());
                    List<Concert> results = service.findConcertsByMaxPrice(maxPrice);
                    if (results.isEmpty()) {
                        System.out.println("No concerts found.");
                    } else {
                        for (Concert c : results) {
                            printConcert(c);
                        }
                    }
                }
                case 6 -> {
                    System.out.print("Enter min price: ");
                    double minPrice = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter max price: ");
                    double maxPrice = Double.parseDouble(scanner.nextLine());
                    List<Concert> results = service.findConcertsByPriceRange(minPrice, maxPrice);
                    if (results.isEmpty()) {
                        System.out.println("No concerts found.");
                    } else {
                        for (Concert c : results) {
                            printConcert(c);
                        }
                    }
                }
                case 7 -> {
                    System.out.print("Enter max price: ");
                    double maxPrice = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter earliest year: ");
                    int minYear = Integer.parseInt(scanner.nextLine());
                    List<Concert> results = service.findConcertsByMaxPriceAndMinYear(maxPrice, minYear);
                    if (results.isEmpty()) {
                        System.out.println("No concerts found.");
                    } else {
                        for (Concert c : results) {
                            printConcert(c);
                        }
                    }
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void reportsMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Reports ===");
            System.out.println("1) Revenue per venue");
            System.out.println("2) Busiest venue and artist");
            System.out.println("3) Average ticket price by year");
            System.out.println("4) Capacity report");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    List<Object[]> results = service.getRevenuePerVenue();
                    if (results.isEmpty()) {
                        System.out.println("No data.");
                    } else {
                        System.out.println("\n=== Revenue Per Venue ===");
                        for (Object[] row : results) {
                            String venueName = (String) row[0];
                            double revenue = ((Number) row[1]).doubleValue(); // cast to Number to be safe
                            System.out.println(venueName + " | $" + revenue);
                        }
                    }
                }
                case 2 -> {
                    System.out.println("\n=== Busiest Venue and Artist ===");
                    List<Object[]> venueResults = service.getBusiestVenue();
                    if (!venueResults.isEmpty()) {
                        Object[] row = venueResults.get(0);
                        String venueName = (String) row[0];
                        long concertCount = ((Number) row[1]).longValue();
                        System.out.println("Busiest Venue: " + venueName + " | " + concertCount + " concerts");
                    }
                    List<Object[]> artistResults = service.getBusiestArtist();
                    if (!artistResults.isEmpty()) {
                        Object[] row = artistResults.get(0);
                        String artistName = (String) row[0];
                        long concertCount = ((Number) row[1]).longValue();
                        System.out.println("Busiest Artist: " + artistName + " | " + concertCount + " concerts");
                    }
                }
                case 3 -> {
                    List<Object[]> results = service.getAvgPricePerYear();
                    if (results.isEmpty()) {
                        System.out.println("No data.");
                    } else {
                        System.out.println("\n=== Average Ticket Price By Year ===");
                        for (Object[] row : results) {
                            int year = ((Number) row[0]).intValue();
                            double avgPrice = ((Number) row[1]).doubleValue();
                            System.out.println(year + " | $" + avgPrice);
                        }
                    }
                }
                case 4 -> {
                    List<Object[]> results = service.getCapacityReport();
                    if (results.isEmpty()) {
                        System.out.println("No data.");
                    } else {
                        System.out.println("\n=== Capacity Report ===");
                        for (Object[] row : results) {
                            String artistName = (String) row[0];
                            String venueName = (String) row[1];
                            int sold = ((Number) row[2]).intValue();
                            int capacity = ((Number) row[3]).intValue();
                            double pct = (sold * 100.0) / capacity;
                            String soldOut = pct >= 100 ? " | SOLD OUT" : "";
                            System.out.println(artistName + " | " + venueName + " | " + sold + "/" + capacity + " | " + pct + "%" + soldOut);
                        }
                    }
                }
                case 0 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void printConcert(Concert c) {
        System.out.println(c.getId() + " | " + c.getArtist().getName() + " | " + c.getVenue().getName() + " | " + c.getVenue().getCity() + " | " + c.getConcertYear() + " | $" + c.getTicketPrice());
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
