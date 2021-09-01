package aonuorah.cron.evaluators;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aonuorah.cron.CronField;

/**
 * CommaEvaluatorTest class.
 *
 * @author Pulse Innovations Ltd
 */
class CommaEvaluatorTest
{
    private static final CronEvaluator EVALUATOR = new CommaEvaluator();
    private static final CronField FIELD =
        new CronField( "test", 1, 3, List.of( "one", "TWO", "Three" ), List.of( EVALUATOR ) );

    @Test
    void test_can_evaluate_with_two_number_params()
    {
        assertTrue( EVALUATOR.canEvaluate( "1,2" ) );
    }

    @Test
    void test_can_evaluate_with_multiple_number_params()
    {
        assertTrue( EVALUATOR.canEvaluate( "11,12,13,14" ) );
    }

    @Test
    void test_can_evaluate_with_two_text_params()
    {
        assertTrue( EVALUATOR.canEvaluate( "one,two" ) );
    }

    @Test
    void test_can_evaluate_with_multiple_text_params()
    {
        assertTrue( EVALUATOR.canEvaluate( "ONE,TWO,THREE,FOUR,FIVE,SIX" ) );
    }

    @Test
    void test_can_not_evaluate_number_param_with_leading_comma()
    {
        assertFalse( EVALUATOR.canEvaluate( ",1,2" ) );
    }

    @Test
    void test_can_not_evaluate_number_param_with_trailing_comma()
    {
        assertFalse( EVALUATOR.canEvaluate( "1,2,3,4," ) );
    }

    @Test
    void test_can_not_evaluate_text_param_with_leading_comma()
    {
        assertFalse( EVALUATOR.canEvaluate( ",one,two,three" ) );
    }

    @Test
    void test_can_not_evaluate_text_param_with_trailing_comma()
    {
        assertFalse( EVALUATOR.canEvaluate( "one,two," ) );
    }

    @Test
    void test_get_values_for_number_param_in_range()
    {
        assertEquals( FIELD.getExecutionTimes( "1,2,3" ), List.of( 1, 2, 3 ) );
    }

    @Test
    void test_get_values_for_text_param_in_range()
    {
        assertEquals( FIELD.getExecutionTimes( "two,THREE,One" ), List.of( 2, 3, 1 ) );
    }

    @Test
    void test_exception_thrown_for_number_param_not_in_range()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "1,2,3,4" ) );
    }

    @Test
    void test_exception_thrown_for_text_param_not_in_range()
    {
        assertThrows( IllegalArgumentException.class, () -> FIELD.getExecutionTimes( "ONE,TWO,FOUR" ) );
    }
}
