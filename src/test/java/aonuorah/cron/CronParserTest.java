package aonuorah.cron;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * CronParserTest class.
 *
 * @author Pulse Innovations Ltd
 */
class CronParserTest
{
    private static final CronParser PARSER = CronParser.getInstance();

    @Test
    void test_parser_with_valid_expression_number_only()
    {
        Map<String, String> values = PARSER.parse( "*/15 0 1,15 * 1-5 /usr/bin/find" );
        assertEquals( 6, values.keySet().size() );
        assertEquals( Set.of( "minutes", "hours", "day of month", "month", "day of week", "command" ),
            values.keySet() );

        assertEquals( "0 15 30 45", values.get( "minutes" ) );
        assertEquals( "0", values.get( "hours" ) );
        assertEquals( "1 15", values.get( "day of month" ) );
        assertEquals( "1 2 3 4 5 6 7 8 9 10 11 12", values.get( "month" ) );
        assertEquals( "1 2 3 4 5", values.get( "day of week" ) );
        assertEquals( "/usr/bin/find", values.get( "command" ) );
    }

    @Test
    void test_parser_with_valid_expression_having_text_params()
    {
        Map<String, String> values = PARSER.parse(
            "*/30 10 1,15 JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC SUN,MON,TUE,WED,THU,FRI,SAT " +
                "/usr/bin/find" );

        assertEquals( 6, values.keySet().size() );
        assertEquals( Set.of( "minutes", "hours", "day of month", "month", "day of week", "command" ),
            values.keySet() );

        assertEquals( "0 30", values.get( "minutes" ) );
        assertEquals( "10", values.get( "hours" ) );
        assertEquals( "1 15", values.get( "day of month" ) );
        assertEquals( "1 2 3 4 5 6 7 8 9 10 11 12", values.get( "month" ) );
        assertEquals( "0 1 2 3 4 5 6", values.get( "day of week" ) );
        assertEquals( "/usr/bin/find", values.get( "command" ) );
    }

    @Test
    void test_exception_thrown_with_expressions_length_less_than_required()
    {
        assertThrows( IllegalArgumentException.class, () -> PARSER.parse( "*/30 10 1,15 * /usr/bin/find" ) );
    }

    @Test
    void test_exception_thrown_with_expressions_length_more_than_required()
    {
        assertThrows( IllegalArgumentException.class, () -> PARSER.parse( "*/30 10 1,15 * * * /usr/bin/find" ) );
    }
}
