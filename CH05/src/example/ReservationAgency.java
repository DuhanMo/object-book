package example;

public class ReservationAgency {

    private final Customer customer;
    private final Screening screening;
    private final Money calculateFee;
    private final int audienceCount;

    public ReservationAgency(Customer customer, Screening screening, Money calculateFee, int audienceCount) {
        this.customer = customer;
        this.screening = screening;
        this.calculateFee = calculateFee;
        this.audienceCount = audienceCount;
    }

    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        boolean discountable = checkDiscountable(screening);
        Money fee = calculateFee(screening, discountable, audienceCount);
        return createReservation(screening, customer, audienceCount, fee);
    }

    private boolean checkDiscountable(Screening screening) {
        return screening.getMovie()
                .getDiscountConditions()
                .stream()
                .anyMatch(condition -> condition.isDiscountable(screening));
    }

    private boolean isDiscountable(DiscountCondition condition, Screening screening) {
        if (condition.getType() == DiscountConditionType.PERIOD) {
            return isSatisfiedByPeriod(condition, screening);
        }
        return isSatisfiedBySequence(condition, screening);
    }

    private boolean isSatisfiedByPeriod(DiscountCondition condition, Screening screening) {
        return screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek()) &&
                condition.getStartTime().compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
                condition.getEndTime().compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
    }

    private boolean isSatisfiedBySequence(DiscountCondition condition, Screening screening) {
        return condition.getSequence() == screening.getSequence();
    }

    private Money calculateFee(Screening screening, boolean discountable, int audienceCount) {
        if (discountable) {
            return screening.getMovie().getFee()
                    .minus(calculateDiscountedFee(screening.getMovie()))
                    .times(audienceCount);
        }

        return screening.getMovie().getFee().times(audienceCount);
    }

    private Money calculateDiscountedFee(Movie movie) {
        switch (movie.getMovieType()) {
            case AMOUNT_DISCOUNT:
                return calculateAmountDiscountAmount(movie);
            case PERCENT_DISCOUNT:
                return calculatePercentDiscountAmount(movie);
            case NONE_DISCOUNT:
                return calculateNoneDiscountAmount(movie);
        }
        throw new IllegalStateException();
    }

    private Money calculateAmountDiscountAmount(Movie movie) {
        return movie.getDiscountAmount();
    }

    private Money calculatePercentDiscountAmount(Movie movie) {
        return movie.getFee().times(movie.getDiscountPercent());
    }

    private Money calculateNoneDiscountAmount(Movie movie) {
        return Money.ZERO;
    }

    private Reservation createReservation(Screening screening, Customer customer, int audienceCount, Money fee) {
        return new Reservation(customer, screening, fee, audienceCount);
    }
}
