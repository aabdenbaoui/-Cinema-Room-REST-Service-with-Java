package cinema.service;

import cinema.entities.Cinema;
import cinema.entities.Seat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CinemaService {
       List<Seat> seats;
        public Cinema getCinema(){
           Cinema cinema =  new Cinema();
           seats = cinema.getSeats();
           return cinema;
        }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
