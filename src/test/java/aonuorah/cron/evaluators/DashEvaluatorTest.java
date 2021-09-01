package aonuorah.cron.evaluators;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aonuorah.cron.CronField;

/**
 * DashEvaluatorTest class.
 *
 * @author Pulse Innovations Ltd
 */
class DashEvaluatorTest
{
    private static final CronEvaluator EVALUATOR = new DashEvaluator();
    private static final CronField FIELD = new CronField( "test", 2, 9, List.of( EVALUATOR ) );

    @Test
    void test_can_evaluate_with_number_params()
    {
        assertTrue( EVALUATOR.canEvaluate( "1-2" ) );
    }

    @Test
    void test_can_not_evaluate_number_param_with_leading_dash()
    {
        assertFalse( EVALUATOR.canEvaluate( "-1-2" ) );
    }

    @Test
    void test_can_not_evaluate_number_param_with_trailing_dash()
    {
        assertFalse( EVALUATOR.canEvaluate( "1,2,3,4-" ) );
    }

    @Test
    void test_get_values_for_number_param_in_range_upper_bound()
    {
        assertEquals( FIELD.getExecutionTimes( "4-9" ), List.of( 4, 5, 6, 7, 8, 9 ) );
    }

    @Test
    void test_get_values_for_number_param_in_range_lower_bound()
    {
        assertEquals( FIELD.getExecutionTimes( "2-5" ), List.of( 2, 3, 4, 5 ) );
    }

    @Test
    void test_get_values_for_number_param_in_range_middle_bound()
    {
        assertEquals( FIELD.getExecutionTimes( "5-7" ), List.of( 5, 6, 7 ) );
    }

    @Test
    void test_get_values_for_number_param_in_range_single_value()
    {
        assertEquals( FIELD.getExecutionTimes( "3-3" ), List.of( 3 ) );
    }

    @Test
    void test_exception_thrown_for_number_param_not_in_range_upper_bound()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "7-10" ) );
    }

    @Test
    void test_exception_thrown_for_number_param_not_in_range_lower_bound()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "1-5" ) );
    }

    @Test
    void test_exception_thrown_for_number_param_in_range_and_descending()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "7-4" ) );
    }
}
