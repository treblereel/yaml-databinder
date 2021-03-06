/*
 * Copyright 2014 Nicolas Morel
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

package org.treblereel.gwt.yaml.api.ser.bean;

import org.treblereel.gwt.yaml.api.YAMLSerializationContext;
import org.treblereel.gwt.yaml.api.YAMLSerializerParameters;
import org.treblereel.gwt.yaml.api.stream.YAMLWriter;

/**
 * Interface hiding the actual implementation doing the bean serialization.
 *
 * @author Nicolas Morel.
 */
interface InternalSerializer<T> {

    /**
     * <p>serializeInternally</p>
     *
     * @param writer              a {@link YAMLWriter} object.
     * @param value               a T object.
     * @param ctx                 a {@link YAMLSerializationContext} object.
     * @param params              a {@link YAMLSerializerParameters} object.
     * @param defaultTypeInfo     a {@link TypeSerializationInfo} object.
     */
    void serializeInternally(YAMLWriter writer, T value, YAMLSerializationContext ctx, YAMLSerializerParameters params,
                             TypeSerializationInfo<T> defaultTypeInfo);

}

