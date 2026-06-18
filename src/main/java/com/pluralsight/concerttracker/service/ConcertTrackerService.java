package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.ArtistRepository;
import com.pluralsight.concerttracker.data.ConcertRepository;
import com.pluralsight.concerttracker.data.PromoterRepository;
import com.pluralsight.concerttracker.data.VenueRepository;
import com.pluralsight.concerttracker.model.Artist;
import com.pluralsight.concerttracker.model.Concert;
import com.pluralsight.concerttracker.model.Promoter;
import com.pluralsight.concerttracker.model.Venue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertTrackerService {

    private final VenueRepository venueRepository;
    private final ArtistRepository artistRepository;
    private final PromoterRepository promoterRepository;
    private final ConcertRepository concertRepository;

    public ConcertTrackerService(VenueRepository venueRepository,
                                 ArtistRepository artistRepository,
                                 PromoterRepository promoterRepository,
                                 ConcertRepository concertRepository) {
        this.venueRepository = venueRepository;
        this.artistRepository = artistRepository;
        this.promoterRepository = promoterRepository;
        this.concertRepository = concertRepository;
    }

    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    public Concert getConcertById(Long id) {
        return concertRepository.findById(id).orElse(null);
    }

    public Venue addVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public Artist addArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public Promoter addPromoter(Promoter promoter) {
        return promoterRepository.save(promoter);
    }

    public Concert addConcert(Concert concert) {
        return concertRepository.save(concert);
    }

    public List<Concert> findAllConcerts() {
        return concertRepository.findAll();
    }

    public Concert updateTicketPrice(long id, double price) {
        if (price < 0) return null;
        Concert concert = concertRepository.findById(id).orElse(null);
        if (concert == null) return null;
        concert.setTicketPrice(price);
        return concertRepository.save(concert);
    }

    public Concert updateTicketsSold(long id, int ticketsSold) {
        if (ticketsSold < 0) return null;
        Concert concert = concertRepository.findById(id).orElse(null);
        if (concert == null) return null;
        if (ticketsSold > concert.getVenue().getCapacity()) return null;
        concert.setTicketsSold(ticketsSold);
        return concertRepository.save(concert);
    }

    public void deleteConcert(long id) {
        concertRepository.deleteById(id);
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue updateVenueCapacity(long id, int capacity) {
        if (capacity < 0) return null;
        Venue venue = venueRepository.findById(id).orElse(null);
        if (venue == null) return null;
        venue.setCapacity(capacity);
        return venueRepository.save(venue);
    }

    public void deleteVenue(long id) {
        venueRepository.deleteById(id);
    }

    public List<Venue> findVenuesByCity(String city) {
        return venueRepository.findByCityIgnoreCase(city);
    }

    public List<Venue> findVenuesByName(String name) {
        return venueRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Venue> findVenuesByMinCapacity(int capacity) {
        return venueRepository.findByCapacityGreaterThanEqual(capacity);
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Artist updateArtistGenre(long id, String genre) {
        Artist artist = artistRepository.findById(id).orElse(null);
        if (artist == null) return null;
        artist.setGenre(genre);
        return artistRepository.save(artist);
    }

    public void deleteArtist(long id) {
        artistRepository.deleteById(id);
    }

    public List<Artist> findArtistsByGenre(String genre) {
        return artistRepository.findByGenreIgnoreCase(genre);
    }

    public List<Artist> findArtistsByName(String name) {
        return artistRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Promoter> getAllPromoters() {
        return promoterRepository.findAll();
    }

    public void deletePromoter(long id) {
        promoterRepository.deleteById(id);
    }

    public List<Promoter> findPromotersByName(String name) {
        return promoterRepository.findByNameContainingIgnoreCase(name);
    }

}
