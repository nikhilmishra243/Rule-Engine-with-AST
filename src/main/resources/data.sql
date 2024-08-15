INSERT INTO rules (rule_string, ast) VALUES
    (
        '((age > 30 AND department = ''Sales'') OR (age < 25 AND department = ''Marketing'')) AND (salary > 50000 OR experience > 5)',
        '{"type": "operator", "left": {"type": "operator", "left": {"type": "operand", "value": "age > 30"}, "right": {"type": "operand", "value": "department = ''Sales''"}, "value": "AND"}, "right": {"type": "operator", "left": {"type": "operand", "value": "age < 25"}, "right": {"type": "operand", "value": "department = ''Marketing''"}, "value": "AND"}, "value": "OR"}, "right": {"type": "operator", "left": {"type": "operand", "value": "salary > 50000"}, "right": {"type": "operand", "value": "experience > 5"}, "value": "OR"}, "value": "AND"}'
    );

INSERT INTO rules (rule_string, ast) VALUES
    (
        '((age > 30 AND department = ''Marketing'')) AND (salary > 20000 OR experience > 5)',
        '{"type": "operator", "left": {"type": "operator", "left": {"type": "operand", "value": "age > 30"}, "right": {"type": "operand", "value": "department = ''Marketing''"}, "value": "AND"}, "right": {"type": "operator", "left": {"type": "operand", "value": "salary > 20000"}, "right": {"type": "operand", "value": "experience > 5"}, "value": "OR"}, "value": "AND"}'
    );
