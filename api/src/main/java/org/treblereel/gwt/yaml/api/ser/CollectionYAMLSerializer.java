/*
 * Copyright 2015 Nicolas Morel
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

package org.treblereel.gwt.yaml.api.ser;

import java.util.Collection;

import org.treblereel.gwt.yaml.api.YAMLSerializationContext;
import org.treblereel.gwt.yaml.api.YAMLSerializer;
import org.treblereel.gwt.yaml.api.YAMLSerializerParameters;
import org.treblereel.gwt.yaml.api.stream.YAMLWriter;

/**
 * Default {@link YAMLSerializer} implementation for {@link Collection}.
 * @param <T> Type of the elements inside the {@link Collection}
 * @author Nicolas Morel
 * @version $Id: $
 */
public class CollectionYAMLSerializer<C extends Collection<T>, T> extends YAMLSerializer<C> {

    protected final YAMLSerializer<T> serializer;
    protected final String propertyName;

    /**
     * <p>Constructor for CollectionYAMLSerializer.</p>
     * @param serializer {@link YAMLSerializer} used to serialize the objects inside the {@link Collection}.
     */
    protected CollectionYAMLSerializer(YAMLSerializer<T> serializer, String propertyName) {
        if (null == serializer) {
            throw new IllegalArgumentException("serializer cannot be null");
        }
        if (null == propertyName) {
            throw new IllegalArgumentException("propertyName cannot be null");
        }
        this.serializer = serializer;
        this.propertyName = propertyName;
    }

    /**
     * <p>newInstance</p>
     * @param serializer {@link YAMLSerializer} used to serialize the objects inside the {@link Collection}.
     * @param <C> Type of the {@link Collection}
     * @return a new instance of {@link CollectionYAMLSerializer}
     */
    public static <C extends Collection<?>> CollectionYAMLSerializer<C, ?> newInstance(YAMLSerializer<?> serializer, String propertyName) {
        return new CollectionYAMLSerializer(serializer, propertyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSerialize(YAMLWriter writer, C values, YAMLSerializationContext ctx, YAMLSerializerParameters params) {
        if (values.isEmpty()) {
            if (ctx.isWriteEmptyYAMLArrays()) {
                writer.beginArray();
                writer.endArray();
            } else {
                writer.nullValue();
            }
            return;
        }
        if (ctx.isWrapCollections()) {
            writer.beginObject(propertyName);
        }

        for (T value : values) {
            serializer.setParent(this).setPropertyName(propertyName).serialize(writer, value, ctx, params);
        }
        if (ctx.isWrapCollections()) {
            writer.endObject();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmpty(C value) {
        return null == value || value.isEmpty();
    }
}
