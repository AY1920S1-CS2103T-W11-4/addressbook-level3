package calofit.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;
import java.util.OptionalInt;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * Represents a historical record of calorie budget settings.
 */
public class CalorieBudget {
    private NavigableMap<LocalDate, Integer> budgetHistory;
    private ObservableMap<LocalDate, Integer> observableHistory;
    private DoubleExpression currentBudget;

    private SimpleObjectProperty<LocalDate> todayProperty = new SimpleObjectProperty<>(LocalDate.now());

    /**
     * Construct an empty {@link CalorieBudget}.
     */
    public CalorieBudget() {
        this.budgetHistory = new TreeMap<>();
        this.observableHistory = FXCollections.observableMap(budgetHistory);
        this.currentBudget = Bindings.createDoubleBinding(() ->
            budgetHistory.isEmpty()
                ? Double.POSITIVE_INFINITY
                : (double) budgetHistory.floorEntry(todayProperty.get()).getValue(),
            observableHistory, todayProperty);
    }

    /**
     * Utility method to extract an {@link OptionalInt} from a map entry.
     * @param entry Map entry to extract from.
     * @return Value, or {@link OptionalInt::empty} if null.
     */
    private OptionalInt toOptional(Map.Entry<LocalDate, Integer> entry) {
        if (entry == null || entry.getValue() == null) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(entry.getValue());
        }
    }

    /**
     * Get the current budget set by the user.
     * @return Budget set by the user, or {@link OptionalInt::empty} if unset.
     */
    public OptionalInt getCurrentBudget() {
        return toOptional(budgetHistory.lastEntry());
    }

    /**
     * Get the budget set by the user, set at the given date.
     * @return Budget set by the user, or {@link OptionalInt::empty} if unset.
     */
    public OptionalInt getBudgetAt(LocalDate date) {
        return toOptional(budgetHistory.floorEntry(date));
    }

    /**
     * Sets a new budget value, taking effect from today onwards.
     * @param budget New budget value
     */
    public void setCurrentBudget(int budget) {
        observableHistory.put(LocalDate.now(), budget);
    }

    /**
     * Provides a map of the budget history set by the user in the current month.
     *
     * @return a SortedMap of budgets set in the current month.
     */
    public SortedMap<LocalDate, Integer> getCurrentMonthBudgetHistory() {
        LocalDate today = todayProperty.get();
        int lastDate = today.lengthOfMonth();
        return this.budgetHistory.subMap(today.withDayOfMonth(1), today.withDayOfMonth(lastDate));
    }

    public DoubleExpression currentBudget() {
        return currentBudget;
    }

    public ObjectProperty<LocalDate> todayProperty() {
        return todayProperty;
    }
}
