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

package org.treblereel.gwt.yaml.api;

import java.util.List;
import java.util.Optional;

import org.treblereel.gwt.yaml.api.exception.YAMLSerializationException;
import org.treblereel.gwt.yaml.api.stream.YAMLWriter;
import org.treblereel.gwt.yaml.api.utils.Pair;

/**
 * Base class for all the serializer. It handles null values and exceptions. The rest is delegated to implementations.
 * @author Nicolas Morel
 * @version $Id: $Id
 */
public abstract class YAMLSerializer<T> {

    protected String propertyName;

    protected YAMLSerializer parent;

    public YAMLSerializer<T> setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public YAMLSerializer setParent(YAMLSerializer parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Serializes an object into YAML output.
     * @param writer {@link YAMLWriter} used to write the serialized YAML
     * @param value Object to serialize
     * @param ctx Context for the full serialization process
     * @throws YAMLSerializationException if an error occurs during the serialization
     */
    public void serialize(YAMLWriter writer, T value, YAMLSerializationContext ctx) throws YAMLSerializationException {
        serialize(writer, value, ctx, ctx.defaultParameters());
    }

    /**
     * Serializes an object into YAML output.
     * @param writer {@link YAMLWriter} used to write the serialized YAML
     * @param value Object to serialize
     * @param ctx Context for the full serialization process
     * @param params Parameters for this serialization
     * @throws YAMLSerializationException if an error occurs during the serialization
     */
    public void serialize(YAMLWriter writer, T value, YAMLSerializationContext ctx, YAMLSerializerParameters params) throws
            YAMLSerializationException {
        serialize(writer, value, ctx, params, false);
    }

    /**
     * Serializes an object into YAML output.
     * @param writer {@link YAMLWriter} used to write the serialized YAML
     * @param value Object to serialize
     * @param ctx Context for the full serialization process
     * @param params Parameters for this serialization
     * @param isMapValue indicate if you're serializing a Map value
     * @throws YAMLSerializationException if an error occurs during the serialization
     */
    public void serialize(YAMLWriter writer, T value, YAMLSerializationContext ctx, YAMLSerializerParameters params, boolean isMapValue) throws
            YAMLSerializationException {
        if (null == value) {
            if (ctx.isSerializeNulls() || (isMapValue && ctx.isWriteNullMapValues())) {
                serializeNullValue(writer, ctx, params);
            } else {
                writer.nullValue();
            }
        } else {
            doSerialize(writer, value, ctx, params);
        }
    }

    /**
     * Serialize the null value. This method allows children to override the default behaviour.
     * @param writer {@link YAMLWriter} used to write the serialized YAML
     * @param ctx Context for the full serialization process
     * @param params Parameters for this serialization
     */
    protected void serializeNullValue(YAMLWriter writer, YAMLSerializationContext ctx, YAMLSerializerParameters params) {
        writer.nullValue();
    }

    /**
     * Serializes a non-null object into YAML output.
     * @param writer {@link YAMLWriter} used to write the serialized YAML
     * @param value Object to serialize
     * @param ctx Context for the full serialization process
     * @param params Parameters for this serialization
     */
    protected abstract void doSerialize(YAMLWriter writer, T value, YAMLSerializationContext ctx, YAMLSerializerParameters
            params);

    /**
     * <p>isEmpty.</p>
     * @param value the value
     * @return true if the value is empty
     */
    protected boolean isEmpty(T value) {
        return null == value;
    }
}
