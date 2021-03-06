package org.treblereel.gwt.yaml.tests.primitive;

import java.io.IOException;
import java.util.Objects;

import org.junit.Test;
import org.treblereel.gwt.yaml.api.annotation.YAMLMapper;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel 4/22/20
 */
//@J2clTestInput(CharTest.class) failed in htmlunit, works in browser
public class CharTest {
    private static final String YAML_0 = "value: \u0000";
    private static final String YAML_C = "value: c";

    private CharTest_CharType_MapperImpl mapper = CharTest_CharType_MapperImpl.INSTANCE;

    @Test
    public void testSerializeValue() throws IOException {
        CharType test = new CharType();
        //assertEquals(YAML_0, mapper.write(test));
        test.setValue('c');
        assertEquals(YAML_C, mapper.write(test));
    }

    @Test
    public void testDeserializeValue() throws IOException {
        assertEquals('c', mapper.read(YAML_C).getValue());
    }

    @YAMLMapper
    public static class CharType {

        private char value;

        @Override
        public int hashCode() {
            return Objects.hash(getValue());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CharType)) {
                return false;
            }
            CharType intType = (CharType) o;
            return getValue() == intType.getValue();
        }

        public char getValue() {
            return value;
        }

        public void setValue(char value) {
            this.value = value;
        }
    }
}
