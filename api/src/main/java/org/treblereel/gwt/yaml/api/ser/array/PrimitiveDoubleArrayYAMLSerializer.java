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

package org.treblereel.gwt.yaml.api.ser.array;

import org.treblereel.gwt.yaml.api.YAMLSerializationContext;
import org.treblereel.gwt.yaml.api.YAMLSerializer;
import org.treblereel.gwt.yaml.api.YAMLSerializerParameters;
import org.treblereel.gwt.yaml.api.ser.BaseNumberYAMLSerializer;
import org.treblereel.gwt.yaml.api.stream.YAMLWriter;

/**
 * Default {@link YAMLSerializer} implementation for array of double.
 * @author Nicolas Morel
 * @version $Id: $
 */
public class PrimitiveDoubleArrayYAMLSerializer extends BasicArrayYAMLSerializer<double[]> {

    private static final PrimitiveDoubleArrayYAMLSerializer INSTANCE = new PrimitiveDoubleArrayYAMLSerializer();
    private BaseNumberYAMLSerializer.DoubleYAMLSerializer serializer = BaseNumberYAMLSerializer.DoubleYAMLSerializer.getInstance();

    private PrimitiveDoubleArrayYAMLSerializer() {
    }

    /**
     * <p>getInstance</p>
     * @return an instance of {@link PrimitiveDoubleArrayYAMLSerializer}
     */
    public static BasicArrayYAMLSerializer getInstance(String propertyName) {
        return INSTANCE.setPropertyName(propertyName);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmpty(double[] value) {
        return null == value || value.length == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSerialize(YAMLWriter writer, double[] values, YAMLSerializationContext ctx, YAMLSerializerParameters params) {
        if (!ctx.isWriteEmptyYAMLArrays() && values.length == 0) {
            writer.nullValue();
            return;
        }

        writer.beginObject(propertyName);
        for (double value : values) {
            serializer.doSerialize(writer, value, ctx, params);
        }
        writer.endObject();
    }
}
