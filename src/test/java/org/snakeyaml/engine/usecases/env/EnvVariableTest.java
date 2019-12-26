/**
 * Copyright (c) 2018, http://www.snakeyaml.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.snakeyaml.engine.usecases.env;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.env.EnvConfig;
import org.snakeyaml.engine.v2.exceptions.MissingEnvironmentVariableException;
import org.snakeyaml.engine.v2.utils.TestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@org.junit.jupiter.api.Tag("fast")
class EnvVariableTest {
    // the variables EnvironmentKey1 and EnvironmentEmpty are set by Maven
    private static final String KEY1 = "EnvironmentKey1";
    private static final String EMPTY = "EnvironmentEmpty";
    private static final String VALUE1 = "EnvironmentValue1";

    private String load(String template) {
        Load loader = new Load(LoadSettings.builder()
                .setEnvConfig(Optional.of(new EnvConfig(new HashMap<>())))
                .build());
        String loaded = (String) loader.loadFromString(template);
        return loaded;
    }

    @Test
    public void testEnvironmentSet() {
        assertEquals(VALUE1, System.getenv(KEY1), "Surefire plugin must set the variable.");
        assertEquals("", System.getenv(EMPTY), "Surefire plugin must set the variable.");
    }

    @Test
    @DisplayName("Parsing ENV variables must be explicitly enabled")
    public void testNoEnvConstructor() {
        Load loader = new Load(LoadSettings.builder().build());
        String loaded = (String) loader.loadFromString("${EnvironmentKey1}");
        assertEquals("${EnvironmentKey1}", loaded);
    }

    @Test
    @DisplayName("Parsing ENV variable which is defined and not empty")
    public void testEnvConstructor() {
        assertEquals(VALUE1, load("${EnvironmentKey1}"));
        assertEquals(VALUE1, load("${EnvironmentKey1-any}"));
        assertEquals(VALUE1, load("${EnvironmentKey1:-any}"));
        assertEquals(VALUE1, load("${EnvironmentKey1:?any}"));
        assertEquals(VALUE1, load("${EnvironmentKey1?any}"));
    }

    @Test
    @DisplayName("Parsing ENV variable which is defined as empty")
    public void testEnvConstructorForEmpty() {
        assertEquals("", load("${EnvironmentEmpty}"));
        assertEquals("", load("${EnvironmentEmpty?}"));
        assertEquals("detected", load("${EnvironmentEmpty:-detected}"));
        assertEquals("", load("${EnvironmentEmpty-detected}"));
        assertEquals("", load("${EnvironmentEmpty?detectedError}"));
        try {
            load("${EnvironmentEmpty:?detectedError}");
        } catch (MissingEnvironmentVariableException e) {
            assertEquals("Empty mandatory variable EnvironmentEmpty: detectedError", e.getMessage());
        }
    }

    @Test
    @DisplayName("Parsing ENV variable which is not set")
    public void testEnvConstructorForUnset() {
        assertEquals("", load("${EnvironmentUnset}"));
        assertEquals("", load("${EnvironmentUnset:- }"));
        assertEquals("detected", load("${EnvironmentUnset:-detected}"));
        assertEquals("detected", load("${EnvironmentUnset-detected}"));
        try {
            load("${EnvironmentUnset:?detectedError}");
        } catch (MissingEnvironmentVariableException e) {
            assertEquals("Missing mandatory variable EnvironmentUnset: detectedError", e.getMessage());
        }
        try {
            load("${EnvironmentUnset?detectedError}");
        } catch (MissingEnvironmentVariableException e) {
            assertEquals("Missing mandatory variable EnvironmentUnset: detectedError", e.getMessage());
        }
    }

    @Test
    @DisplayName("Parse docker-compose.yaml example")
    public void testDockerCompose() {
        Load loader = new Load(LoadSettings.builder()
                .setEnvConfig(Optional.of(new EnvConfig(new HashMap<>())))
                .build());
        String resource = TestUtils.getResource("env/docker-compose.yaml");
        Map<String, Object> compose = (Map<String, Object>) loader.loadFromString(resource);
        String output = compose.toString();
        assertTrue(output.endsWith("environment={URL1=EnvironmentValue1, URL2=, URL3=server3, URL4=, URL5=server5, URL6=server6}}}}"), output);
    }
}