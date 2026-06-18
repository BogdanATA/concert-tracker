package com.pluralsight.concerttracker.data;

import com.pluralsight.concerttracker.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c WHERE c.concertYear = :year")
    List<Concert> findByYear(@Param("year") int year);

    @Query("SELECT c FROM Concert c WHERE LOWER(c.artist.name) LIKE LOWER(:name)")
    List<Concert> findByArtistName(@Param("name") String name);

    @Query("SELECT c FROM Concert c WHERE LOWER(c.venue.name) LIKE LOWER(:name)")
    List<Concert> findByVenueName(@Param("name") String name);

    @Query("SELECT c FROM Concert c WHERE LOWER(c.venue.city) = LOWER(:city)")
    List<Concert> findByCity(@Param("city") String city);

    @Query("SELECT c FROM Concert c WHERE c.ticketPrice <= :maxPrice")
    List<Concert> findByMaxPrice(@Param("maxPrice") double maxPrice);

    @Query("SELECT c FROM Concert c WHERE c.ticketPrice >= :minPrice AND c.ticketPrice <= :maxPrice")
    List<Concert> findByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

    @Query("SELECT c FROM Concert c WHERE c.ticketPrice <= :maxPrice AND c.concertYear >= :minYear")
    List<Concert> findByMaxPriceAndMinYear(@Param("maxPrice") double maxPrice, @Param("minYear") int minYear);
}
