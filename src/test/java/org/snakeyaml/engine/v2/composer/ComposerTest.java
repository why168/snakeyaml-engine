/*
 * Copyright (c) 2018, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.snakeyaml.engine.v2.composer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.lowlevel.Compose;
import org.snakeyaml.engine.v2.exceptions.ComposerException;
import org.snakeyaml.engine.v2.nodes.Node;

@Tag("fast")
class ComposerTest {

  @Test
  @DisplayName("Fail to Compose one document when more documents are provided.")
  void composeOne() {
    ComposerException exception = assertThrows(ComposerException.class,
        () -> new Compose(LoadSettings.builder().build()).composeString("a\n---\nb\n"));
    assertTrue(exception.getMessage().contains("expected a single document in the stream"));
    assertTrue(exception.getMessage().contains("but found another document"));
  }

  @Test
  void failToComposeUnknownAlias() {
    ComposerException exception = assertThrows(ComposerException.class,
        () -> new Compose(LoadSettings.builder().build()).composeString("[a, *id b]"));
    assertTrue(exception.getMessage().contains("found undefined alias id"), exception.getMessage());
  }

  @Test
  void composeAnchor() {
    String data = "--- &113\n{name: Bill, age: 18}";
    Compose compose = new Compose(LoadSettings.builder().build());
    Optional<Node> optionalNode = compose.composeString(data);
    assertTrue(optionalNode.isPresent());
    Node node = optionalNode.get();
    assertEquals("113", node.getAnchor().get().getValue());
  }
}
