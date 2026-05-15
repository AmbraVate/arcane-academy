-- Add DISCRIMINATION to the questions type check constraint.
-- Hibernate 6 generates this constraint from QuestionType enum values;
-- dropping and recreating it is the only way to extend the allowed set.
ALTER TABLE questions DROP CONSTRAINT IF EXISTS questions_type_check;
ALTER TABLE questions ADD CONSTRAINT questions_type_check
    CHECK (type IN (
        'MCQ', 'MULTIPLE_CHOICE', 'FILL_BLANK', 'TRUE_FALSE',
        'CODE_COMPLETION', 'WHATS_THE_OUTPUT', 'DEBUGGING',
        'SCENARIO', 'COMPARE_CONTRAST', 'APPLICATION', 'DISCRIMINATION'
    ));
