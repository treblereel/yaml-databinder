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
import org.treblereel.gwt.yaml.api.deser.CharacterYAMLDeserializer;
import org.treblereel.gwt.yaml.api.stream.YAMLReader;

/**
 * Default {@link YAMLDeserializer} implementation for array of char.
 * @author Nicolas Morel
 * @version $Id: $
 */
public class PrimitiveCharacterArrayYAMLDeserializer extends AbstractArrayYAMLDeserializer<char[]> {

    private static final PrimitiveCharacterArrayYAMLDeserializer INSTANCE = new PrimitiveCharacterArrayYAMLDeserializer();

    private PrimitiveCharacterArrayYAMLDeserializer() {
    }

    /**
     * <p>getInstance</p>
     * @return an instance of {@link PrimitiveCharacterArrayYAMLDeserializer}
     */
    public static PrimitiveCharacterArrayYAMLDeserializer getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char[] doDeserializeArray(YAMLReader reader, YAMLDeserializationContext ctx, YAMLDeserializerParameters params) {
        List<Character> list = deserializeIntoList(reader, ctx, CharacterYAMLDeserializer.getInstance(), params);

        char[] result = new char[list.size()];
        int i = 0;
        for (Character value : list) {
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
    protected char[] doDeserializeSingleArray(YAMLReader reader, YAMLDeserializationContext ctx, YAMLDeserializerParameters params) {
        return new char[]{CharacterYAMLDeserializer.getInstance().deserialize(reader, ctx, params)};
    }
}
