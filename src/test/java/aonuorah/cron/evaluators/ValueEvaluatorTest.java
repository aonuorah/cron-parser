package aonuorah.cron.evaluators;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aonuorah.cron.CronField;

/**
 * ValueEvaluatorTest class.
 *
 * @author Pulse Innovations Ltd
 */
class ValueEvaluatorTest
{
    private static final CronEvaluator EVALUATOR = new ValueEvaluator();
    private static final CronField FIELD =
        new CronField( "test", 0, 2, List.of( "zero", "One", "TWO" ), List.of( EVALUATOR ) );

    @Test
    void test_can_evaluate_with_asterisks()
    {
        assertTrue( EVALUATOR.canEvaluate( "*" ) );
    }

    @Test
    void test_can_evaluate_with_number_param()
    {
        assertTrue( EVALUATOR.canEvaluate( "11" ) );
    }

    @Test
    void test_can_evaluate_with_text_param_lower()
    {
        assertTrue( EVALUATOR.canEvaluate( "one" ) );
    }

    @Test
    void test_can_evaluate_with_text_param_upper()
    {
        assertTrue( EVALUATOR.canEvaluate( "TWO" ) );
    }

    @Test
    void test_can_not_evaluate_with_two_params()
    {
        assertFalse( EVALUATOR.canEvaluate( "1 2" ) );
    }

    @Test
    void test_can_not_evaluate_with_invalid_symbol_in_param_1()
    {
        assertFalse( EVALUATOR.canEvaluate( "ON-E" ) );
    }

    @Test
    void test_can_not_evaluate_with_invalid_symbol_in_param_2()
    {
        assertFalse( EVALUATOR.canEvaluate( "ON/E" ) );
    }

    @Test
    void test_get_values_for_asterisks()
    {
        assertEquals( FIELD.getExecutionTimes( "*" ), List.of( 0, 1, 2 ) );
    }

    @Test
    void test_get_values_for_number_param_in_range()
    {
        assertEquals( FIELD.getExecutionTimes( "1" ), List.of( 1 ) );
    }

    @Test
    void test_get_values_for_text_param_in_range()
    {
        assertEquals( FIELD.getExecutionTimes( "zero" ), List.of( 0 ) );
    }

    @Test
    void test_exception_thrown_for_number_param_not_in_range()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "3" ) );
    }

    @Test
    void test_exception_thrown_for_text_param_not_in_range()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "THREE" ) );
    }
}
