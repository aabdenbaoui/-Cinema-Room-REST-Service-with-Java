package cinema.entities;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
       private int rows;
       private int columns;

    List<Seat> seats = new ArrayList<>();
        public Cinema(){
            this.rows = 9;
            this.columns = 9;
            for(int i = 1; i < rows + 1; i++){
                for(int j = 1; j < columns + 1; j++){
                    int k;
                    if(i > 0 && i < 5){
                        k = 10;
                    }else{
                        k = 8;
                    }
                    seats.add(new Seat(i,j, k));
                }
            }
        }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public List<Seat>  getSeats(){
            return seats;
        }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return
                "rows=" + rows +
                ", columns=" + columns +
                ", seats=" + seats +
                '}';
    }
}
