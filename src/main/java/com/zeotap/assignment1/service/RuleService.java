package com.zeotap.assignment1.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeotap.assignment1.bo.Node;
import com.zeotap.assignment1.entity.Rule;
import com.zeotap.assignment1.repository.RuleRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository, ObjectMapper objectMapper) {
        this.ruleRepository = ruleRepository;
    }

    // Define precedence and associativity
    private static final Map<String, Integer> precedence = new HashMap<>();
    private static final Map<String, Boolean> associativity = new HashMap<>();

    static {
        // Higher number indicates higher precedence
        precedence.put("AND", 1);
        precedence.put("OR", 0);

        // Associativity of operators: true for left-associative, false for right-associative
        associativity.put("AND", true);
        associativity.put("OR", true);
    }

    // Function to create the AST from a rule string
    public Node parseRule(String rule) {
        // Tokenize the rule string
        List<String> tokens = tokenize(rule);
        System.out.println(tokens);

        // Convert tokens to postfix notation (Reverse Polish Notation)
        List<String> postfix = infixToPostfix(tokens);

        System.out.println("POSTFIX");
        System.out.println(postfix.toString());

        Node root = constructAST(postfix);

        saveRule(rule, root);

        // Build the AST from postfix notation
//        return postfixToAST(postfix);
        return root;
    }




    // Function to tokenize the rule string
    private List<String> tokenize(String rule) {

        String modifiedInput = rule.replace("(", " ( ").replace(")", " ) ");
        String[] parts = modifiedInput.split("\\s+");

        // List to hold the tokens
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        for (String part : parts) {
            // Skip empty parts
            if (part.trim().isEmpty()) continue;

            if (part.equals("(") || part.equals(")") || part.equals("AND") || part.equals("OR")) {
                // Add any previously accumulated token before adding new logical/operator tokens
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString().trim());
                    currentToken.setLength(0); // Clear the StringBuilder
                }
                // Add the logical/operator tokens directly
                tokens.add(part);
            } else {
                // Accumulate parts of a condition
                if (currentToken.length() > 0) {
                    currentToken.append(" ");
                }
                currentToken.append(part);
            }
        }

        // Add any remaining token
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString().trim());
        }


        return cleanList(tokens);

    }

    public static List<String> cleanList(List<String> originalList) {
        List<String> cleanedList = new ArrayList<>();
        for (String item : originalList) {
            // Remove quotes and any extra spaces
            String cleanedItem = item.replaceAll("^\"|\"$", "").trim();
            if (!cleanedItem.isEmpty()) {
                cleanedList.add(cleanedItem);
            }
        }
        return cleanedList;
    }

    public static List<String> infixToPostfix(List<String> tokens) {
        List<String> postfix = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if(Objects.equals(token, "(") ){
                stack.push(token);
            }

            else if(Objects.equals(token, ")")){
                while(!stack.isEmpty()){
                    if(Objects.equals(stack.peek(), "(")){
                        stack.pop();
                        break;
                    }
                    postfix.add(stack.pop());
                }
            }

            else if(token.equals("AND") ){
                if(stack.isEmpty() || Objects.equals(stack.peek(), "OR") || Objects.equals(stack.peek(), "(") || Objects.equals(stack.peek(), ")")){
                    stack.push(token);
                }
                else {
                    while(!stack.isEmpty()){
                        if(Objects.equals(stack.peek(), "(")){
                            stack.pop();
                            break;
                        }
                        postfix.add(stack.pop());
                    }
                }
            }

            else if(token.equals("OR")){
                if(stack.isEmpty() || Objects.equals(stack.peek(), "(") || Objects.equals(stack.peek(), ")")){
                    stack.push(token);
                }
                else {
                    while(!stack.isEmpty()){
                        if(Objects.equals(stack.peek(), "(")){
                            stack.pop();
                            break;
                        }
                        postfix.add(stack.pop());
                    }
                }
            }

            else{
                postfix.add(token);
            }
        }

        while(!stack.isEmpty()){
            postfix.add(stack.pop());
        }

        return postfix;

    }


    public static Node constructAST(List<String> postfix) {
        Stack<Node> stack = new Stack<>();

        for (String token : postfix) {
            if (isOperator(token)) {
                Node right = stack.pop();
                Node left = stack.pop();
                Node operatorNode = new Node("operator", token, left, right);
                stack.push(operatorNode);
            } else {
                Node operandNode = new Node("operand", token);
                stack.push(operandNode);
            }
        }

        return stack.pop(); // The root of the AST
    }

    private static boolean isOperator(String token) {
        return token.equals("AND") || token.equals("OR");
    }


    public void saveRule(String ruleString, Node node) {

        try{
            Rule rule = new Rule();
            rule.setRuleString(ruleString);
            rule.setAst(node.toString());
            ruleRepository.save(rule);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public Node buildASTFromJson(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonString);
        return parseNode(rootNode);
    }

    private Node parseNode(JsonNode jsonNode) {
        String type = jsonNode.has("type") ? jsonNode.get("type").asText() : null;
        String value = jsonNode.has("value") ? jsonNode.get("value").asText() : null;
        Node left = jsonNode.has("left") ? parseNode(jsonNode.get("left")) : null;
        Node right = jsonNode.has("right") ? parseNode(jsonNode.get("right")) : null;

        return new Node(type, value, left, right);
    }









    public static boolean evaluateRule(String jsonString, String evaluationString) {
        JSONObject rule = new JSONObject(jsonString);
        JSONObject data = new JSONObject(evaluationString);
        return evaluate(rule, data);
    }

    private static boolean evaluate(JSONObject rule, JSONObject data) {
        String type = rule.getString("type");
        String value = rule.getString("value");

        if (type.equals("operand")) {
            return evaluateOperand(rule.getString("value"), data);
        } else if (type.equals("operator")) {
            boolean leftResult = evaluate(rule.getJSONObject("left"), data);
            boolean rightResult = evaluate(rule.getJSONObject("right"), data);
            return value.equals("AND") ? leftResult && rightResult : leftResult || rightResult;
        }
        return false;
    }

    private static boolean evaluateOperand(String expression, JSONObject data) {
        String[] parts = expression.split(" ");
        String field = parts[0];
        String operator = parts[1];
        String value = parts[2].replace("'", "");

        switch (operator) {
            case ">":
                return data.getInt(field) > Integer.parseInt(value);
            case "=":
                return data.getString(field).equals(value);
            default:
                return false;
        }
    }













}
