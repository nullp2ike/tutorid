package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.BookingRequest;
import utils.data_generation.Any;
import utils.data_generation.DefaultBookingData;
import utils.data_generation.StudentCreator;
import utils.data_generation.TutorCreator;
import utils.domain.Student;
import utils.domain.Tutor;
import utils.junit_extensions.PropertiesExtension;

import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PropertiesExtension.class)
public class BookingRequestChecks {

    @Test
    public void Tutor_RequestsBookingOnBehalfOfStudent_Success(){
        Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        Student student = new StudentCreator(Any.studentUsername()).create();

        Response response = new BookingRequest(DefaultBookingData.tutorScheduled(student.getNickname()))
                .asTutor(tutor, student);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }

    @Test
    public void Student_RequestsBookingViaTutorAvailableTimeSlot_Success(){
        Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        Student student = new StudentCreator(Any.studentUsername()).create();



        Response response = new BookingRequest().asStudent(tutor, student);
    }

}
