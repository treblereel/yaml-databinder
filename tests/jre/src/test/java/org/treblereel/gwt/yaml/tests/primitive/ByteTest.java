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
@J2clTestInput(ByteTest.class)
public class ByteTest {

    private static final String YAML_0 = "check: 0";
    private static final String YAML_123 = "check: 123";
    private static final String YAML_22 = "check: -22";

    private ByteTest_ByteType_MapperImpl mapper = ByteTest_ByteType_MapperImpl.INSTANCE;

    @Test
    public void testSerializeValue() throws IOException {
        ByteType test = new ByteType();
        assertEquals(YAML_0, mapper.write(test));
        test.setCheck((byte) 123);
        assertEquals(YAML_123, mapper.write(test));
        assertEquals(test, mapper.read(mapper.write(test)));
        test.setCheck((byte) -22);
        assertEquals(test, mapper.read(mapper.write(test)));
    }

    @Test
    public void testDeserializeValue() throws IOException {
        assertEquals(0, mapper.read(YAML_0).getCheck());
        assertEquals(123, mapper.read(YAML_123).getCheck());
        assertEquals(-22, mapper.read(YAML_22).getCheck());
    }

    @YAMLMapper
    public static class ByteType {

        private byte check;

        @Override
        public int hashCode() {
            return Objects.hash(getCheck());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ByteType)) {
                return false;
            }
            ByteType byteType = (ByteType) o;
            return getCheck() == byteType.getCheck();
        }

        public byte getCheck() {
            return check;
        }

        public void setCheck(byte check) {
            this.check = check;
        }
    }
}

