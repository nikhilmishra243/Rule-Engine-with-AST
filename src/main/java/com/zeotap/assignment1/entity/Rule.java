package com.zeotap.assignment1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_string", nullable = false)
    private String ruleString;

    @Lob
    @Column(name = "ast", nullable = false, columnDefinition = "CLOB")
    private String ast; // Store the AST as a JSON string

    // Constructors, Getters, and Setters
    public Rule() {}

    public Rule(String ruleString, String ast) {
        this.ruleString = ruleString;
        this.ast = ast;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuleString() { return ruleString; }
    public void setRuleString(String ruleString) { this.ruleString = ruleString; }

    public String getAst() { return ast; }
    public void setAst(String ast) { this.ast = ast; }
}
