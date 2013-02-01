package com.vityuk.ginger;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLocalizationTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Localization localization;

    @Mock
    private LocalizationProvider localizationProvider;

    @Before
    public void setUp() throws Exception {
        localization = new DefaultLocalization(localizationProvider);
    }

    @Test
    public void testStringConstant() {
        String testValue = "test value";
        when(localizationProvider.getString("test.string")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        String result = constants.testString();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testStringConstantWithNullValue() {
        when(localizationProvider.getString("test.string")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        String result = constants.testString();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testStringConstantWithException() {
        when(localizationProvider.getString("test.string")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testString();
    }

    @Test
    public void testBooleanConstant() {
        Boolean testValue = Boolean.TRUE;
        when(localizationProvider.getBoolean("test.boolean")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Boolean result = constants.testBoolean();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testBooleanConstantWithNullValue() {
        when(localizationProvider.getBoolean("test.boolean")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Boolean result = constants.testBoolean();

        assertThat(result).isNull();
    }


    @Test(expected = RuntimeException.class)
    public void testBooleanConstantWithException() {
        when(localizationProvider.getBoolean("test.boolean")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testBoolean();
    }

    @Test
    public void testIntegerConstant() {
        Integer testValue = Integer.MAX_VALUE;
        when(localizationProvider.getInteger("test.int")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Integer result = constants.testInt();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testIntegerConstantWithNullValue() {
        when(localizationProvider.getInteger("test.int")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Integer result = constants.testInt();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testIntegerConstantWithException() {
        when(localizationProvider.getInteger("test.int")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testInt();
    }

    @Test
    public void testLongConstant() {
        Long testValue = Long.MIN_VALUE;
        when(localizationProvider.getLong("test.long")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Long result = constants.testLong();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testLongConstantWithNullValue() {
        when(localizationProvider.getLong("test.long")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Long result = constants.testLong();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testLongConstantWithException() {
        when(localizationProvider.getLong("test.long")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testLong();
    }

    @Test
    public void testFloatConstant() {
        Float testValue = Float.MAX_VALUE;
        when(localizationProvider.getFloat("test.float")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Float result = constants.testFloat();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testFloatConstantWithNullValue() {
        when(localizationProvider.getFloat("test.float")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Float result = constants.testFloat();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testFloatConstantWithException() {
        when(localizationProvider.getFloat("test.float")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testFloat();
    }

    @Test
    public void testDoubleConstant() {
        Double testValue = Double.MIN_VALUE;
        when(localizationProvider.getDouble("test.double")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Double result = constants.testDouble();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testDoubleConstantWithNullValue() {
        when(localizationProvider.getDouble("test.double")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Double result = constants.testDouble();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testDoubleConstantWithException() {
        when(localizationProvider.getDouble("test.double")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testDouble();
    }

    @Test
    public void testStringListConstant() {
        List<String> testValue = Arrays.asList("a", "b", "c");
        when(localizationProvider.getStringList("test.string.list")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        List<String> result = constants.testStringList();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testStringListConstantWithNullValue() {
        when(localizationProvider.getStringList("test.string.list")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        List<String> result = constants.testStringList();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testStringListConstantWithException() {
        when(localizationProvider.getStringList("test.string.list")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testStringList();
    }

    @Test
    public void testStringMapConstant() {
        Map<String, String> testValue = ImmutableMap.of("a", "1", "b", "2");
        when(localizationProvider.getStringMap("test.string.map")).thenReturn(testValue);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Map<String, String> result = constants.testStringMap();

        assertThat(result).isNotNull().isEqualTo(testValue);
    }

    @Test
    public void testStringMapConstantWithNullValue() {
        when(localizationProvider.getStringMap("test.string.map")).thenReturn(null);
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        Map<String, String> result = constants.testStringMap();

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testStringMapConstantWithException() {
        when(localizationProvider.getStringMap("test.string.map")).thenThrow(new RuntimeException());
        TestConstants constants = localization.getLocalizable(TestConstants.class);

        constants.testStringMap();
    }

    @Test
    public void testConstantWithInvalidReturnTypeCharacter() {
        thrown.expect(InvalidReturnTypeException.class);
        String expectedMessage = "Invalid return type: java.lang.Character for method: testChar in " +
                TestConstantsWithChar.class.getName();
        thrown.expectMessage(expectedMessage);

        localization.getLocalizable(TestConstantsWithChar.class);

        verifyZeroInteractions(localizationProvider);
    }


    @Test
    public void testConstantWithInvalidReturnTypePrimitive() {
        thrown.expect(InvalidReturnTypeException.class);
        String expectedMessage = "Invalid return type: boolean for method: testBoolean in " +
                TestConstantsWithPrimitiveBoolean.class.getName();
        thrown.expectMessage(expectedMessage);

        localization.getLocalizable(TestConstantsWithPrimitiveBoolean.class);

        verifyZeroInteractions(localizationProvider);
    }

    @Test
    public void testSingleStringArgumentMessage() {
        String arg = "test arg";
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.string.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleStringArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleBooleanArgumentMessage() {
        Boolean arg = Boolean.TRUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.boolean.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleBooleanArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleIntegerArgumentMessage() {
        Integer arg = Integer.MIN_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.integer.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleIntegerArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleLongArgumentMessage() {
        Long arg = Long.MAX_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.long.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleLongArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleFloatArgumentMessage() {
        Float arg = Float.MAX_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.float.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleFloatArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSingleDoubleArgumentMessage() {
        Double arg = Double.MIN_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.double.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleDoubleArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testSinglePrimitiveArgumentMessage() {
        int arg = Integer.MIN_VALUE;
        String message = "Hello, " + arg;
        when(localizationProvider.getMessage("single.primitive.argument", arg)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singlePrimitiveArgument(arg);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testMultipleArgumentsMessage() {
        String arg0 = "test";
        boolean arg1 = false;
        Integer arg2 = 547;
        long arg3 = 23534534;
        Float arg4 = 423543.0533F;
        double arg5 = -1.0432522;

        String message = "Test " + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + ", " + arg4 + ", " + arg5;
        when(localizationProvider.getMessage("multiple.arguments",
                arg0, arg1, arg2, arg3, arg4, arg5)).thenReturn(message);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.multipleArguments(arg0, arg1, arg2, arg3, arg4, arg5);

        assertThat(result).isNotNull().isEqualTo(message);
    }

    @Test
    public void testMessageWithNullMessage() {
        String arg = "test";
        when(localizationProvider.getMessage("single.string.argument", arg)).thenReturn(null);
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        String result = messages.singleStringArgument(arg);

        assertThat(result).isNull();
    }

    @Test(expected = RuntimeException.class)
    public void testMessageWithException() {
        String arg = "test";
        when(localizationProvider.getMessage("single.string.argument", arg)).thenThrow(new RuntimeException());
        TestMessages messages = localization.getLocalizable(TestMessages.class);

        messages.singleStringArgument(arg);
    }

    @Test
    public void testMessageWithInvalidReturnType() {
        thrown.expect(InvalidReturnTypeException.class);
        String expectedMessage = "Invalid return type: java.lang.Boolean for method: message in " +
                TestMessagesWithIncorrectReturnType.class.getName();
        thrown.expectMessage(expectedMessage);


        localization.getLocalizable(TestMessagesWithIncorrectReturnType.class);

        verifyZeroInteractions(localizationProvider);
    }

    interface TestConstants extends Localizable {
        String testString();

        Boolean testBoolean();

        Integer testInt();

        Long testLong();

        Float testFloat();

        Double testDouble();

        List<String> testStringList();

        Map<String, String> testStringMap();
    }

    interface TestConstantsWithChar extends Localizable {
        Character testChar();
    }

    interface TestConstantsWithPrimitiveBoolean extends Localizable {
        boolean testBoolean();
    }

    interface TestMessages extends Localizable {
        String singleStringArgument(String arg);

        String singleBooleanArgument(Boolean arg);

        String singleIntegerArgument(Integer arg);

        String singleLongArgument(Long arg);

        String singleFloatArgument(Float arg);

        String singleDoubleArgument(Double arg);

        String singlePrimitiveArgument(int arg);

        String multipleArguments(String arg0, boolean arg1, Integer arg2, long arg3, Float arg4, double arg5);
    }


    interface TestMessagesWithIncorrectReturnType extends Localizable {
        Boolean message(Boolean arg);
    }
}