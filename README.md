# Tutor.id public API tests

A project for students of University of Tartu's software testing students for learning to created automated integration tests using public REST APIs.

## Getting Started

### Prerequisites

Install Java 8 JDK and preferably download an IDE of your choice. We use Intellij IDEA.

## Running the tests

Run the tests from your IDE (make sure its version is not too old so it supports JUnit 5)
or execute the gradle test task from command line.

## Group info

1. Hina Anwar

2. Khaled Charkie

3. Mehwish Peter

4. Hanzla Sajid

## Tests made

1. Tutor makes a booking on behalf of Student and cancels it booking himself

2. Student makes a booking on tutor available time and cancels it himself

3. One Student books a lesson and another student try to cancel that booking and fails  

4. One Tutor books a lesson on behalf of a student and another tutor try to cancel this lesson 

5. Student makes a booking on available tutor date and tutor cancels booking

6. Tutor makes a booking on behalf of student and student cancels booking

7. Student requests booking outside tutor available time slot and fails

8. Student makes a booking on available tutor date and tutor confirms booking 

9. Tutor books on behalf of student and student confirms it 

10. Student makes a booking on available tutor date and students cancels the booking and then  tutor try to confirm the same cancelled booking but fails

11. Tutor makes a booking on behalf of a student, tutor cancels the booking himself but then student try to confirm an already cancelled booking

12. Tutor Cancels an already cancelled booking.

13. Tutor make a booking on behalf of student and tutor reschedules the booking and student confirms  (Could not complete tests)

## License

This project is licensed under the MIT License
