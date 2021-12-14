/*
 * Copyright (c) 2018, SnakeYAML
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
package org.snakeyaml.engine.v2.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Tag("fast")
class ScalarResolverTupleTest {

    @Test
    @DisplayName("ResolverTuple.toString()")
    void resolveMap() {
        assertEquals("Tuple tag=tag:yaml.org,2002:str regexp=^(?:true|false)$", new ResolverTuple(Tag.STR, Pattern.compile("^(?:true|false)$")).toString());
    }
}
