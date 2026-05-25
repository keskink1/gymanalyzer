```
classDiagram
    class BaseEntity {
        <<Abstract>>
        +UUID uuid
        +LocalDateTime createdAt
        +String createdBy
        +LocalDateTime updatedAt
        +String updatedBy
        +LocalDateTime deletedAt
        +String deletedBy
        +boolean deleted
        +markAsDeleted(String deletedBy)
        +updateAudit(String updatedBy)
    }
    class User {
        +FullName fullName
        +Email email
        +Password password
        +Age age
        +Role role
        +createUser(String createdBy, FullName, Age, Email, Password) User
        +promoteToAdmin(String auditor)
        +deleteUser(String auditor)
        +updateFullName(FullName, String auditor)
        +updateAge(Age, String auditor)
        +updateEmail(Email, String auditor)
        +changePassword(Password, String auditor)
    }
    class Workout {
        +UUID userUuid
        +ExerciseType exerciseType
        +Weight weight
        +Reps reps
        +Sets sets
        +LocalDate loggedAt
        +logWorkout(String createdBy, UUID userUuid, ExerciseType, Weight, Reps, Sets, LocalDate) Workout
        +deleteWorkout(String auditor)
    }
    class Role {
        <<Enumeration>>
        USER
        ADMIN
    }
    class ExerciseType {
        <<Enumeration>>
        SQUAT
        BENCH_PRESS
        DEADLIFT
        OVERHEAD_PRESS
        BARBELL_ROW
    }
    BaseEntity <|-- User
    BaseEntity <|-- Workout
    User *-- Role
    Workout *-- ExerciseType
    User "1" --> "0..*" Workout : logs
```