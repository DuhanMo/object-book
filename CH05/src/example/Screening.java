package example;

import java.time.LocalDateTime;

public class Screening {

    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public ReservationAgency reserve(Customer customer, int audienceCount) {
        return new ReservationAgency(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public int getSequence() {
        return sequence;
    }

    public LocalDateTime getWhenScreened() {
        return whenScreened;
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public Movie getMovie() {
        return this.movie;
    }
}
