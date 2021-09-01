package aonuorah.cron;

/**
 * A command line application which parses a cron string and expands each field to show the times at which it will run.
 *
 * @author Pulse Innovations Ltd
 */
public class CronParserApplication
{
    /**
     * The application's entry point
     *
     * @param args an array of command-line arguments for the application
     */
    public static void main( final String[] args )
    {
        if ( args.length != 1 )
        {
            System.out.println(
                String.format( "Invalid number of arguments supplied, 1 required but %s supplied", args.length ) );
            return;
        }

        try
        {
            CronParser.getInstance()
                .parse( args[ 0 ] )
                .forEach( ( key, value ) -> System.out.println( String.format( "%-14s %s", key, value ) ) );
        }
        catch ( IllegalArgumentException ex )
        {
            System.out.println( ex.getMessage() );
        }
    }
}
