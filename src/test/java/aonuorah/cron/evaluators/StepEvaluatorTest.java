package aonuorah.cron.evaluators;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aonuorah.cron.CronField;

/**
 * StepEvaluatorTest class.
 *
 * @author Pulse Innovations Ltd
 */
class StepEvaluatorTest
{
    private static final CronEvaluator EVALUATOR = new StepEvaluator();
    private static final CronField FIELD = new CronField( "test", 0, 9, List.of( EVALUATOR ) );

    @Test
    void test_can_evaluate_with_number_params()
    {
        assertTrue( EVALUATOR.canEvaluate( "*/5" ) );
    }

    @Test
    void test_can_not_evaluate_number_param_without_leading_asterisks_1()
    {
        assertFalse( EVALUATOR.canEvaluate( "0/4" ) );
    }

    @Test
    void test_can_not_evaluate_number_param_without_leading_asterisks_2()
    {
        assertFalse( EVALUATOR.canEvaluate( "/4" ) );
    }

    @Test
    void test_get_values_for_number_param_in_range_1()
    {
        assertEquals( FIELD.getExecutionTimes( "*/4" ), List.of( 0, 4, 8 ) );
    }

    @Test
    void test_get_values_for_number_param_in_range_2()
    {
        assertEquals( FIELD.getExecutionTimes( "*/3" ), List.of( 0, 3, 6, 9 ) );
    }

    @Test
    void test_get_values_for_number_param_in_range_3()
    {
        assertEquals( FIELD.getExecutionTimes( "*/9" ), List.of( 0, 9 ) );
    }

    @Test
    void test_exception_thrown_for_number_param_not_in_range_1()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "*/0" ) );
    }

    @Test
    void test_exception_thrown_for_number_param_not_in_range_2()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "*/10" ) );
    }
}
