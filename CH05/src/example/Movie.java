package example;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public abstract class Movie {

    private final String title;
    private final Duration runningTime;
    private final Money fee;
    private final List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

    public Movie(String title, Duration runningTime, Money fee,
                 DiscountCondition... discountConditions) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountConditions = Arrays.asList(discountConditions);
    }

    public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }
        return fee;
    }

    private boolean isDiscountable(Screening screening) {
        return discountConditions.stream()
                .anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    abstract protected Money calculateDiscountAmount();

    protected Money getFee() {
        return fee;
    }

    public List<DiscountCondition> getDiscountConditions() {
        return discountConditions;
    }

    public String getTitle() {
        return title;
    }

    public Duration getRunningTime() {
        return runningTime;
    }

    public MovieType getMovieType() {
        return movieType;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
