package aonuorah.cron;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aonuorah.cron.evaluators.CronEvaluator;

/**
 * Class to define attributes of a cron field, specifying the name, constraints and supported evaluators.
 *
 * @author Pulse Innovations Ltd
 */
public class CronField
{
    private final String name;
    private final FieldConstraints constraints;
    private final List<CronEvaluator> evaluators;

    public CronField( final String name, final int minValue, final int maxValue,
        final List<CronEvaluator> supportedEvaluators )
    {
        this.name = name;
        this.constraints = new FieldConstraints( minValue, maxValue );
        this.evaluators = Collections.unmodifiableList( supportedEvaluators );
    }

    public CronField( final String name, final int minValue, final int maxValue, final List<String> namedValues,
        final List<CronEvaluator> evaluators )
    {
        // build map of named values to their respective integers in same order
        var valueMap = IntStream.rangeClosed( minValue, maxValue )
            .boxed()
            .collect(
                Collectors.toMap( value -> namedValues.get( value - minValue ).toUpperCase(), Function.identity() ) );

        this.name = name;
        this.constraints = new FieldConstraints( minValue, maxValue, valueMap );
        this.evaluators = Collections.unmodifiableList( evaluators );
    }

    /**
     * Gets a list of execution times for the specified field expression.
     *
     * @param fieldExp the field expression
     * @return list of execution times
     * @throws IllegalArgumentException if an error occurs during evaluation, or if none of the
     *                                  supported evaluators is able to handle the field expression.
     */
    public List<Integer> getExecutionTimes( final String fieldExp )
    {
        // loops through all supported evaluators, and only executes the first evaluator that can handle the specified
        // field expression
        return evaluators.stream()
            .filter( evaluator -> evaluator.canEvaluate( fieldExp ) )
            .findFirst()
            .map( evaluator -> evaluator.getExecutionTimes( fieldExp, constraints ) )
            .filter( values -> !values.isEmpty() )
            .orElseThrow( () -> new IllegalArgumentException( String.format( "%s cannot be evaluated", fieldExp ) ) );
    }

    public String getName()
    {
        return name;
    }

    /**
     * Defines value constraints for a cron field.
     */
    public static class FieldConstraints
    {
        private final Map<String, Integer> namedValues;
        private final int minValue;
        private final int maxValue;

        public FieldConstraints( final int minValue, final int maxValue )
        {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.namedValues = Map.of();
        }

        public FieldConstraints( final int minValue, final int maxValue, final Map<String, Integer> namedValues )
        {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.namedValues = Collections.unmodifiableMap( namedValues );
        }

        public Map<String, Integer> getNamedValues()
        {
            return namedValues;
        }

        public int getMinValue()
        {
            return minValue;
        }

        public int getMaxValue()
        {
            return maxValue;
        }
    }
}
