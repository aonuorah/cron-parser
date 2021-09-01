package aonuorah.cron;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aonuorah.cron.evaluators.CommaEvaluator;
import aonuorah.cron.evaluators.CronEvaluator;
import aonuorah.cron.evaluators.DashEvaluator;
import aonuorah.cron.evaluators.StepEvaluator;
import aonuorah.cron.evaluators.ValueEvaluator;

/**
 * Class to build and define formats for parsing cron expressions.
 *
 * @author Pulse Innovations Ltd
 */
public class CronParser
{
    private static final List<CronEvaluator> DEFAULT_EVALUATORS =
        List.of( new CommaEvaluator(), new DashEvaluator(), new StepEvaluator(), new ValueEvaluator() );
    private static final List<String> WEEKS = List.of( "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" );
    private static final List<String> MONTHS =
        List.of( "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" );

    private final List<CronField> cronFields;
    private final int maxExpressionLength;

    private CronParser( final List<CronField> cronFields )
    {
        this.cronFields = cronFields;
        this.maxExpressionLength = this.cronFields.size() + 1;
    }

    /**
     * Builds an instance of {@link CronParser} with standard cron format.
     *
     * @return {@link CronParser}
     */
    public static CronParser getInstance()
    {
        List<CronField> fields = new ArrayList<>();
        fields.add( new CronField( "minutes", 0, 59, DEFAULT_EVALUATORS ) );
        fields.add( new CronField( "hours", 0, 23, DEFAULT_EVALUATORS ) );
        fields.add( new CronField( "day of month", 1, 31, DEFAULT_EVALUATORS ) );
        fields.add( new CronField( "month", 1, 12, MONTHS, DEFAULT_EVALUATORS ) );
        fields.add( new CronField( "day of week", 0, 6, WEEKS, DEFAULT_EVALUATORS ) );

        return new CronParser( fields );
    }

    /**
     * Parses a cron expression and returns a map of each field name and their respective values/execution times.
     *
     * @param expressions the cron expression
     * @return parsed expression
     * @throws IllegalArgumentException if an error occurs while attempting to parse expression
     */
    public Map<String, String> parse( final String expressions )
    {
        var cronParams = expressions.split( " " );
        var cronParamsLength = cronParams.length;
        if ( cronParamsLength != maxExpressionLength )
        {
            // fail fast if number of params in expression is not valid
            throw new IllegalArgumentException(
                String.format( "Invalid cron string, %s parameters expected but %s supplied", maxExpressionLength,
                    cronParamsLength ) );
        }

        // loop through available cron fields and generate a map of field name to cron execution times
        // collect map as LinkedHashMap to maintain order of loop
        var values = IntStream.range( 0, cronFields.size() ).boxed().map( i ->
        {
            var field = cronFields.get( i );
            var fieldExp = cronParams[ i ];
            String fieldValues = field.getExecutionTimes( fieldExp )
                .stream()
                .map( String::valueOf )
                .collect( Collectors.joining( " " ) );

            return Map.entry( field.getName(), fieldValues );
        } ).collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue, ( a, b ) -> b, LinkedHashMap::new ) );

        // add last parameter as the command field
        values.put( "command", cronParams[ cronParamsLength - 1 ] );
        return values;
    }
}
