-- V38: fix sw-eng-app-foundations module title and sort_order
--
-- The variables_intro.md file originally used moduleTitle "Module 1: Foundations
-- of Computation" with sortOrder 1, creating a duplicate "Module 1" in the
-- Software Engineering pathway alongside the main se-app-m1 module.
-- Updated to a distinct title and pushed to sort position 50.

UPDATE modules
SET    title      = 'Variables and State',
       sort_order = 50
WHERE  id         = 'sw-eng-app-foundations';
