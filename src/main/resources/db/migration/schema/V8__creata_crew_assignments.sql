-- Table that assigns crew members to specific flights
CREATE TABLE IF NOT EXISTS `crew_assignments` (
    `assignment_id` INT PRIMARY KEY AUTO_INCREMENT,
    `flight_id` INT NOT NULL,
    `crew_id` INT NOT NULL,
    CONSTRAINT FK_CrewAssignmentFlight FOREIGN KEY (`flight_id`) REFERENCES `flights`(`flight_id`),
    CONSTRAINT FK_CrewAssignmentCrew FOREIGN KEY (`crew_id`) REFERENCES `crew`(`crew_id`)
);
