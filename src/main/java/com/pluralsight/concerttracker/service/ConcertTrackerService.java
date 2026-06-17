package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.ArtistRepository;
import com.pluralsight.concerttracker.data.ConcertRepository;
import com.pluralsight.concerttracker.data.PromoterRepository;
import com.pluralsight.concerttracker.data.VenueRepository;
import com.pluralsight.concerttracker.model.Concert;
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
}
