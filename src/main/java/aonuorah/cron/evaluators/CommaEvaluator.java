package aonuorah.cron.evaluators;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aonuorah.cron.CronField;

/**
 * CronEvaluator to handle field expressions for special character "," (comma).
 *
 * @author Pulse Innovations Ltd
 */
public class CommaEvaluator extends CronEvaluator
{
    private static final Pattern PATTERN = Pattern.compile( "^([0-9]+(?:,[0-9]+)+|[a-zA-Z]+(?:,[a-zA-Z]+)+)$" );

    @Override
    protected Pattern getPattern()
    {
        return PATTERN;
    }

    @Override
    public List<Integer> getExecutionTimes( final String fieldExp, final CronField.FieldConstraints constraints )
    {
        return Stream.of( fieldExp.split( "," ) )
            .map( String::toUpperCase )
            .map( value -> checkValueIsValid( value, constraints ) )
            .collect( Collectors.toList() );
    }

    private Integer checkValueIsValid( final String value, final CronField.FieldConstraints fieldValue )
    {
        if ( isInteger( value ) )
        {
            var valueInt = Integer.parseInt( value );
            if ( fieldValue.getMinValue() <= valueInt && valueInt <= fieldValue.getMaxValue() )
            {
                return valueInt;
            }
            throw new IllegalArgumentException(
                String.format( "%s is not within the bounds of allowed values %s-%s", value, fieldValue.getMinValue(),
                    fieldValue.getMaxValue() ) );
        }
        else if ( !fieldValue.getNamedValues().isEmpty() )
        {
            if ( fieldValue.getNamedValues().containsKey( value ) )
            {
                return fieldValue.getNamedValues().get( value );
            }

            throw new IllegalArgumentException(
                String.format( "%s is not valid, expecting any of %s", value, fieldValue.getNamedValues().keySet() ) );
        }
        throw new IllegalArgumentException( String.format( "%s is not valid", value ) );
    }
}
