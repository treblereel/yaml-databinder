/*
 * Copyright 2013 Nicolas Morel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treblereel.gwt.yaml.api.deser.array;

import java.util.List;

import org.treblereel.gwt.yaml.api.YAMLDeserializationContext;
import org.treblereel.gwt.yaml.api.YAMLDeserializer;
import org.treblereel.gwt.yaml.api.YAMLDeserializerParameters;
import org.treblereel.gwt.yaml.api.deser.BaseNumberYAMLDeserializer;
import org.treblereel.gwt.yaml.api.stream.YAMLReader;

/**
 * Default {@link YAMLDeserializer} implementation for array of short.
 * @author Nicolas Morel
 * @version $Id: $
 */
public class PrimitiveShortArrayYAMLDeserializer extends AbstractArrayYAMLDeserializer<short[]> {

    private static final PrimitiveShortArrayYAMLDeserializer INSTANCE = new PrimitiveShortArrayYAMLDeserializer();

    private PrimitiveShortArrayYAMLDeserializer() {
    }

    /**
     * <p>getInstance</p>
     * @return an instance of {@link PrimitiveShortArrayYAMLDeserializer}
     */
    public static PrimitiveShortArrayYAMLDeserializer getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short[] doDeserializeArray(YAMLReader reader, YAMLDeserializationContext ctx, YAMLDeserializerParameters params) {
        List<Short> list = deserializeIntoList(reader, ctx, BaseNumberYAMLDeserializer.ShortYAMLDeserializer.getInstance(), params);

        short[] result = new short[list.size()];
        int i = 0;
        for (Short value : list) {
            if (null != value) {
                result[i] = value;
            }
            i++;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected short[] doDeserializeSingleArray(YAMLReader reader, YAMLDeserializationContext ctx, YAMLDeserializerParameters params) {
        return new short[]{BaseNumberYAMLDeserializer.ShortYAMLDeserializer.getInstance().deserialize(reader, ctx, params)};
    }
}
