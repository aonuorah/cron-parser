package aonuorah.cron.evaluators;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aonuorah.cron.CronField;

/**
 * CronEvaluator to handle field expressions for special character "-" (dash).
 *
 * @author Pulse Innovations Ltd
 */
public class DashEvaluator extends CronEvaluator
{
    private static final Pattern PATTERN = Pattern.compile( "^[0-9]+-[0-9]+$" );

    @Override
    protected Pattern getPattern()
    {
        return PATTERN;
    }

    @Override
    public List<Integer> getExecutionTimes( final String fieldExp, final CronField.FieldConstraints constraints )
    {
        var expSplit = fieldExp.split( "-" );
        var rangeMin = Integer.parseInt( expSplit[ 0 ] );
        var rangeMax = Integer.parseInt( expSplit[ 1 ] );

        if ( rangeMin <= rangeMax && constraints.getMinValue() <= rangeMin && rangeMax <= constraints.getMaxValue() )
        {
            return IntStream.rangeClosed( rangeMin, rangeMax ).boxed().collect( Collectors.toList() );
        }

        throw new IllegalArgumentException(
            String.format( "%s-%s is not within the bounds of allowed values %s-%s, or might be in descending order",
                rangeMin, rangeMax, constraints.getMinValue(), constraints.getMaxValue() ) );
    }
}
