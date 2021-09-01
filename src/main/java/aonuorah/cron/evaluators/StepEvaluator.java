package aonuorah.cron.evaluators;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aonuorah.cron.CronField;

/**
 * CronEvaluator to handle field expressions for special character "/" (slash).
 *
 * @author Pulse Innovations Ltd
 */
public class StepEvaluator extends CronEvaluator
{
    private static final Pattern PATTERN = Pattern.compile( "^\\*/[0-9]+$" );

    @Override
    protected Pattern getPattern()
    {
        return PATTERN;
    }

    @Override
    public List<Integer> getExecutionTimes( final String fieldExp, final CronField.FieldConstraints constraints )
    {
        var expSplit = fieldExp.split( "/" );
        var stepValue = Integer.parseInt( expSplit[ 1 ] );

        if ( stepValue > 0 && constraints.getMinValue() <= stepValue && stepValue <= constraints.getMaxValue() )
        {
            return IntStream.rangeClosed( constraints.getMinValue(), constraints.getMaxValue() )
                .filter( v -> v % stepValue == 0 )
                .boxed()
                .collect( Collectors.toList() );
        }

        throw new IllegalArgumentException(
            String.format( "%s step is either '0' or is not within the bounds of allowed values %s-%s", stepValue,
                constraints.getMinValue(), constraints.getMaxValue() ) );
    }
}
