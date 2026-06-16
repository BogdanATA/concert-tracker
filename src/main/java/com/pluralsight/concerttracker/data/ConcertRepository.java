package com.pluralsight.concerttracker.data;

import com.pluralsight.concerttracker.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
