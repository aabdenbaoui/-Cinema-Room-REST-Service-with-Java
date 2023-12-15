package cinema.entities;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Purchase {
    private String token;
    private Ticket ticket;

    public Purchase(Ticket ticket) {
        this.ticket = ticket;
        token =randomUUID().toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
