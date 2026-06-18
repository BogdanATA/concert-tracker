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


    // row[0] = venue name, row[1] = total revenue
    @Query("SELECT c.venue.name, SUM(c.ticketPrice * c.ticketsSold) FROM Concert c GROUP BY c.venue.name")
    List<Object[]> revenuePerVenue();

    // row[0] = venue name, row[1] = concert count
    @Query("SELECT c.venue.name, COUNT(c) FROM Concert c GROUP BY c.venue.name ORDER BY COUNT(c) DESC")
    List<Object[]> concertCountPerVenue();

    // row[0] = artist name, row[1] = concert count
    @Query("SELECT c.artist.name, COUNT(c) FROM Concert c GROUP BY c.artist.name ORDER BY COUNT(c) DESC")
    List<Object[]> concertCountPerArtist();

    // row[0] = year, row[1] = average price
    @Query("SELECT c.concertYear, AVG(c.ticketPrice) FROM Concert c GROUP BY c.concertYear ORDER BY c.concertYear")
    List<Object[]> avgPricePerYear();

    // row[0] = artist name, row[1] = venue name, row[2] = tickets sold, row[3] = capacity
    @Query("SELECT c.artist.name, c.venue.name, c.ticketsSold, c.venue.capacity FROM Concert c ORDER BY c.venue.name")
    List<Object[]> capacityReport();
}
