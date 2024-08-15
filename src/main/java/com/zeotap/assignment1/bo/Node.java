package com.zeotap.assignment1.bo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    String type;   // "operator" or "operand"
    Node left;     // Left child
    Node right;    // Right child
    String value;  // Value for operand nodes or operator nodes

    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Constructor for operator nodes
    public Node(String type, String value, Node left, Node right) {
        this.type = type;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    // Recursive function to print the node and its descendants
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"type\": \"").append(type).append("\"");
        sb.append(", \"value\": \"").append(value).append("\"}");

        if (left != null) {
            sb.append(", \"left\": ").append(left.toString());
        }

        if (right != null) {
            sb.append(", \"right\": ").append(right.toString());
        }

        return sb.toString();
    }

    public String toJson(ObjectMapper mapper) {

        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("type", this.getType());
        jsonNode.put("value", this.getValue());

        if (this.getLeft() != null) {
            jsonNode.set("left", mapper.convertValue(this.getLeft(), ObjectNode.class));
        }

        if (this.getRight() != null) {
            jsonNode.set("right", mapper.convertValue(this.getRight(), ObjectNode.class));
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}