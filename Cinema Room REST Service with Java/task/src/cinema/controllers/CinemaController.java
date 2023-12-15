package cinema.controllers;

import cinema.PurchaseDTO;
import cinema.User;
import cinema.entities.*;
import cinema.entities.Error;
import cinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class CinemaController {
    @Autowired
    CinemaService cinemaService;
    Map<String, PurchaseDTO> purchases = new HashMap<>();
    Map<String, Seat> ticketSeatMap = new HashMap<>();
    List<Seat> seats;
    private int income;
    private int total = 81;
    private int available;
    private int purchased;
    @GetMapping("/seats")
    public Cinema getSeats(){
        seats =  cinemaService.getCinema().getSeats();
       return cinemaService.getCinema();
    }

    @GetMapping("/availableSeats")
    public List<Seat> getAvailableSeats(){
        for(Seat seat : seats){
            if(seat.isAvailable()){
                ++available;
            }
        }
        return seats;
    }
    @PostMapping("/purchase")
    public ResponseEntity<?> makePurchase(@RequestBody PurchaseRequest purchaseRequest ){
        Purchase purchase = null;
        Seat seat = new Seat(purchaseRequest.getRow(), purchaseRequest.getColumn());
        for(Seat seat1: seats){
            if (seat.getRow() > cinemaService.getCinema().getRows()  || seat.getRow() <= 0 || seat.getColumn() > cinemaService.getCinema().getColumns()  || seat.getColumn() <= 0){
//                System.out.println("error: " +  "The number of a row or a column is out of bounds!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(new Error("The number of a row or a column is out of bounds!"));

            } else if(seat1.getRow() == seat.getRow() && seat1.getColumn() ==  seat.getColumn() && seat1.isAvailable()){
                int index = seats.indexOf(seat1);
                seats.remove(seat1);
                seat.setPrice(seat1.getPrice());
                Seat tempSeat = new Seat(purchaseRequest.getRow(), purchaseRequest.getColumn(), seat1.getPrice(), false );
                seats.set(index,  tempSeat);
                purchase = new Purchase(new Ticket(tempSeat.getRow(), tempSeat.getColumn(), tempSeat.getPrice()));
                ticketSeatMap.put(purchase.getToken(), tempSeat);
                purchases.put(purchase.getToken(), new PurchaseDTO(purchase.getTicket()));
                purchased = purchases.size();
                income += tempSeat.getPrice();
                break;
            }else if(seat1.getRow() == seat.getRow() && seat1.getColumn() ==  seat.getColumn() && !seat1.isAvailable()){
                System.out.println("error: "+"The ticket has been already purchased!");
               return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Error("The ticket has been already purchased!"));
            }
        }

         return ResponseEntity.status(HttpStatus.OK)
                .body(purchase);
    }
    @PostMapping("/return")
    public ResponseEntity<?>  returnThePurchase(@RequestBody  TokenEntity tokenEntity){
            if(purchases.containsKey(tokenEntity.getToken())){
                System.out.println("token found");
                PurchaseDTO purchaseDTOtemp = purchases.get(tokenEntity.getToken());
                purchases.remove(tokenEntity.getToken());
                int index = seats.indexOf(ticketSeatMap.get(tokenEntity.getToken()));
                income -= seats.get(index).getPrice();
                seats.set(index, new Seat(ticketSeatMap.get(tokenEntity.getToken()).getRow(), ticketSeatMap.get(tokenEntity.getToken()).getColumn(), ticketSeatMap.get(tokenEntity.getToken()).getPrice(), true ));
                --purchased;
                return ResponseEntity.status(HttpStatus.OK)
                        .body(purchaseDTOtemp);
            }else{
                System.out.println("token not found");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Error("Wrong token!"));
            }
    }
    @GetMapping("/stats")
//    public ResponseEntity<?> getStats(@RequestBody User user){
    public ResponseEntity<?> getStats(@RequestParam(value = "password", required = false)
                                          User user){
        String password="super_secret";
        Stats stats;
        if (user == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Error("The password is wrong!"));
        }


        if(user.getPassword().equals(password) || user.getPassword() == "super_secret"){
            available = total - purchased;
            stats = new Stats(income,available,purchased);
        }else{
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Error("The password is wrong!"));
        }
        return  ResponseEntity.status(HttpStatus.OK)
                .body(stats);
    }


}
