package cinema;

import cinema.entities.Ticket;

public class PurchaseDTO {
    private Ticket ticket;

    public PurchaseDTO(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
