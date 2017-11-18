# Testing Methodology Notes from "Cracking the Coding Interview"

Interviewer Looking For:
    - big picture understanding:
        prioritization
    - Know how pieces fit together:
        understand how software works
        how it fits into the big picture
    - Organization:
        Structured approach, not what comes to mind, categories
    - Practicality

Testing in the real world
    <Steps>
    1. Who will use and why
    2. Use cases
    3. Bounds of use / specs
    4. Stress/failure conditions
    5. How perform testing / test cases
        Mirror/Simulate real world usage, practical to employ

Testing Piece of Software
    - Greater details on how to perform testing
    - 2 aspects
        Manual vs Automatic
        Black vs White box testing
    - Steps: same from real world + blackOrWhiteTesting()

Function Testing
    1. Defining Test Cases
        normal, extremes, illegal, strangeInput
    2. Define Expectations
        ie: on sort make sure original has the same state
    3. Write tests