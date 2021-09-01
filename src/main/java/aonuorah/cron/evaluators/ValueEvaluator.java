package aonuorah.cron.evaluators;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aonuorah.cron.CronField;

/**
 * CronEvaluator to handle field expressions for special character "*" (asterisks) and single value expressions
 * e.g. "12" or "WED"
 *
 * @author Pulse Innovations Ltd
 */
public class ValueEvaluator extends CronEvaluator
{
    private static final Pattern PATTERN = Pattern.compile( "^(\\*|[0-9]+|[a-zA-Z]+)$" );

    @Override
    protected Pattern getPattern()
    {
        return PATTERN;
    }

    @Override
    public List<Integer> getExecutionTimes( final String fieldExp, final CronField.FieldConstraints constraints )
    {
        if ( "*".equals( fieldExp ) )
        {
            return IntStream.rangeClosed( constraints.getMinValue(), constraints.getMaxValue() )
                .boxed()
                .collect( Collectors.toList() );
        }
        else if ( isInteger( fieldExp ) )
        {
            var value = Integer.parseInt( fieldExp );
            if ( constraints.getMinValue() <= value && value <= constraints.getMaxValue() )
            {
                return List.of( value );
            }
            throw new IllegalArgumentException(
                String.format( "%s value is not within the bounds of allowed values %s-%s", value,
                    constraints.getMinValue(), constraints.getMaxValue() ) );
        }
        else if ( !constraints.getNamedValues().isEmpty() )
        {
            String fieldExpUpper = fieldExp.toUpperCase();
            if ( constraints.getNamedValues().containsKey( fieldExpUpper ) )
            {
                return List.of( constraints.getNamedValues().get( fieldExpUpper ) );
            }

            throw new IllegalArgumentException( String.format( "%s is not valid, expecting any of %s", fieldExp,
                constraints.getNamedValues().keySet() ) );
        }
        throw new IllegalArgumentException( String.format( "%s is not valid", fieldExp ) );
    }
}
