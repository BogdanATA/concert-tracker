package com.pluralsight.concerttracker.data;

import com.pluralsight.concerttracker.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
