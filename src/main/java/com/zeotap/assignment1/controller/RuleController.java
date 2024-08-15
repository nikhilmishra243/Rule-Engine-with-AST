package com.zeotap.assignment1.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeotap.assignment1.bo.Node;
import com.zeotap.assignment1.entity.Rule;
import com.zeotap.assignment1.service.RuleService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRule(@RequestBody String ruleString) throws Exception {

        Node root = ruleService.parseRule(ruleString);
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.ok(root.toJson(mapper));
    }

//    @PostMapping("/combine")
//    public ResponseEntity<Node> combineRules(@RequestBody List<String> ruleStrings) throws Exception {
//        Node root = ruleService.combineRules(ruleStrings);
//        return ResponseEntity.ok(root);
//    }
//
//    @PostMapping("/evaluate")
//    public boolean evaluateRule(@RequestBody JsonNode request) {
//        JsonNode ruleAST = request.get("rule");
//        JsonNode data = request.get("data");
//
//        return ruleService.evaluateRule(ruleAST, data);
//    }


    @PostMapping("/buildAST")
    public ResponseEntity<String> evaluateRule(@RequestBody String ASTString) throws Exception {
        Node root = ruleService.buildASTFromJson(ASTString);
        return ResponseEntity.ok(root.toString());
    }

}
