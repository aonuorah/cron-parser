package aonuorah.cron.evaluators;

import java.util.List;
import java.util.regex.Pattern;

import aonuorah.cron.CronField;

/**
 * Abstract class to define common interfaces for evaluating cron field expressions.
 *
 * @author Pulse Innovations Ltd
 */
public abstract class CronEvaluator
{
    private static final Pattern NUMBER_PATTERN = Pattern.compile( "^\\d+$" );

    /**
     * Returns a boolean indicating the specified field expression can be evaluated by the implementing evaluator.
     *
     * @param fieldExp the field expression to check
     * @return boolean
     */
    public boolean canEvaluate( final String fieldExp )
    {
        return getPattern().matcher( fieldExp ).matches();
    }

    /**
     * Returns boolean indicating a String is an Integer or not.
     *
     * @param value the String to check
     * @return boolean
     */
    protected static boolean isInteger( final String value )
    {
        return NUMBER_PATTERN.matcher( value ).matches();
    }

    /**
     * Gets a list of execution times evaluated from the specified field expression and constraints.
     *
     * @param fieldExp    the cron field expression (e.g. "*" or "1-5")
     * @param constraints the field constraints
     * @return list of execution times
     */
    public abstract List<Integer> getExecutionTimes( final String fieldExp,
        final CronField.FieldConstraints constraints );

    /**
     * Gets the regex pattern describing the field expression format for the evaluator.
     *
     * @return Pattern
     */
    protected abstract Pattern getPattern();
}
