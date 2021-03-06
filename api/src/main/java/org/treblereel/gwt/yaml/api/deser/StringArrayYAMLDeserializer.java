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

package org.treblereel.gwt.yaml.api.deser;

import java.util.List;

import org.treblereel.gwt.yaml.api.YAMLDeserializationContext;
import org.treblereel.gwt.yaml.api.YAMLDeserializer;
import org.treblereel.gwt.yaml.api.YAMLDeserializerParameters;
import org.treblereel.gwt.yaml.api.deser.array.AbstractArrayYAMLDeserializer;
import org.treblereel.gwt.yaml.api.stream.YAMLReader;

/**
 * Default {@link YAMLDeserializer} implementation for array of {@link java.lang.String}.
 * <p>Not working in production mode, cast problem. Can maybe work with disableCastChecking</p>
 * @author Nicolas Morel
 * @version $Id: $
 */
public class StringArrayYAMLDeserializer extends AbstractArrayYAMLDeserializer<String[]> {

    private static final StringArrayYAMLDeserializer INSTANCE = new StringArrayYAMLDeserializer();

    private StringArrayYAMLDeserializer() {
    }

    /**
     * <p>getInstance</p>
     * @return an instance of {@link StringArrayYAMLDeserializer}
     */
    public static StringArrayYAMLDeserializer getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] doDeserializeArray(YAMLReader reader, YAMLDeserializationContext ctx, YAMLDeserializerParameters params) {
        List<String> list = deserializeIntoList(reader, ctx, StringYAMLDeserializer.getInstance(), params);
        return list.toArray(new String[list.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] doDeserializeSingleArray(YAMLReader reader, YAMLDeserializationContext ctx, YAMLDeserializerParameters params) {
        //return new String[]{reader.nextString()};
        throw new UnsupportedOperationException();
    }
}