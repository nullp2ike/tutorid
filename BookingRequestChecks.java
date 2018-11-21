package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import requests.Availability;
import requests.BookingRequest;
import requests.Invite;
import utils.builders.BookingRequestBuilder;
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
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student))
                .asTutor(tutor, student);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }

    @Test
    public void Tutor_RequestsBookingOnBehalfOfStudentStudentLocation_Success(){
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduledStudentLocation(tutor, student))
                .asTutor(tutor, student);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
        System.out.println(response.getBody().asString());
    }

    @Test
    public void Tutor_RequestsBookingOnBehalfOfStudentTutorLocation_Success(){
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduledTutorLocation(tutor, student))
                .asTutor(tutor, student);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }

    @Test
    public void Tutor_RequestsBookingOnBehalfOfStudentPayWithCard_Success(){
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduledWithCard(tutor, student))
                .asTutor(tutor, student);

        response.then()

                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }

    @Test
    public void Student_RequestsBookingViaTutorAvailableTimeSlot_Success(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }

    @Test
    public void Student_RequestsBookingViaTutorAvailableTimeSlotStudentLocation_Success(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlotStudentLocation(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }

    @Test
    public void Student_RequestsBookingViaTutorAvailableTimeSlotTutorLocation_Success(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlotTutorLocation(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }

    @Test
    public void Student_RequestsBookingViaTutorAvailableTimeSlotOnlineLocation_Success(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().tutorScheduledOnlineLocation(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }

    @Test
    public void Student_RequestsBookingViaTutorAvailableTimeSlotWithCard_Fail(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));
        System.out.println(r.getBody().asString());

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlotWithCard(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Cannot confirm booking because the student doesn't have a card connected to their account"));
    }

    //8
    @Test
    public void Student_RequestsBookingViaTutorAvailableTimeSlot_Fail(){
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        sendInviteAndAcceptByStudent(tutor, student);

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        System.out.println(response.getBody().asString());
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Tutor policy does not allow requesting booking outside availability & booking request is outside tutor's availability or tutor already has a booking during that time"));
    }

    private void sendInviteAndAcceptByStudent(final Tutor tutor, final Student student) {
        new Invite().send(tutor, student.getEmail());
        final Response invitationLinkResponse = new Invite().invitationLink(student);
        final int invitationId = new JSONObject(invitationLinkResponse.getBody().asString()).getJSONObject("data").getInt("id");

        final Response acceptResponse = new Invite().accept(invitationId, student);
        acceptResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }
}
