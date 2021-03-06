package org.treblereel.gwt.yaml.tests.primitive;

import java.io.IOException;
import java.util.Objects;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;
import org.treblereel.gwt.yaml.api.annotation.YAMLMapper;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel 4/22/20
 */
@J2clTestInput(IntTest.class)
public class IntTest {

    private static final String YAML_0 = "value: 0";
    private static final String YAML_17222 = "value: 17222";
    private static final String YAML__17222 = "value: \"-17222\"";

    private IntTest_IntType_MapperImpl mapper = IntTest_IntType_MapperImpl.INSTANCE;

    @Test
    public void testSerializeValue() throws IOException {
        IntType test = new IntType();
        assertEquals(YAML_0, mapper.write(test));
        test.setValue(17222);
        assertEquals(YAML_17222, mapper.write(test));
        assertEquals(test, mapper.read(mapper.write(test)));
        test.setValue(-17222);
        assertEquals(YAML__17222, mapper.write(test));
        assertEquals(test, mapper.read(mapper.write(test)));
    }

    @Test
    public void testDeserializeValue() throws IOException {
        assertEquals(0, mapper.read(YAML_0).getValue());
        assertEquals(17222, mapper.read(YAML_17222).getValue());
        assertEquals(-17222, mapper.read(YAML__17222).getValue());
    }

    @YAMLMapper
    public static class IntType {

        private int value;

        @Override
        public int hashCode() {
            return Objects.hash(getValue());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IntType)) {
                return false;
            }
            IntType intType = (IntType) o;
            return getValue() == intType.getValue();
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}


