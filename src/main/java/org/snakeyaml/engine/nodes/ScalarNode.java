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
package org.snakeyaml.engine.nodes;

import org.snakeyaml.engine.common.ScalarStyle;
import org.snakeyaml.engine.exceptions.Mark;

/**
 * Represents a scalar node.
 * <p>
 * Scalar nodes form the leaves in the node graph.
 * </p>
 */
public class ScalarNode extends Node {
    private ScalarStyle style;
    private String value;

    public ScalarNode(Tag tag, String value, ScalarStyle style, Mark startMark, Mark endMark) {
        this(tag, true, value, style, startMark, endMark);
    }

    public ScalarNode(Tag tag, boolean resolved, String value, ScalarStyle style, Mark startMark, Mark endMark) {
        super(tag, startMark, endMark);
        if (value == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = value;
        if (style == null) throw new NullPointerException("Scalar style must be provided.");
        this.style = style;
        this.resolved = resolved;
    }

    /**
     * Get scalar style of this node.
     *
     * @see org.snakeyaml.engine.events.ScalarEvent
     * @see <a href="http://yaml.org/spec/1.1/#id903915">Chapter 9. Scalar
     *      Styles</a>
     * @return style of this scalar node
     */
    public ScalarStyle getStyle() {
        return style;
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.scalar;
    }

    /**
     * Value of this scalar.
     *
     * @return Scalar's value.
     */
    public String getValue() {
        return value;
    }

    public String toString() {
        return "<" + this.getClass().getName() + " (tag=" + getTag() + ", value=" + getValue()
                + ")>";
    }

    public boolean isPlain() {
        return style == ScalarStyle.PLAIN;
    }
}