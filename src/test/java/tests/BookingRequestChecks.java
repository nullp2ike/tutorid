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
    public void Student_RequestsBookingViaTutorAvailableTimeSlot_Success(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));
        System.out.println(r.getBody().asString());

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Setup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        System.out.println(response.getBody().asString());
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
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

    @Test
    public void Student_RequestsBooking_ThenCancels_TutorConfirms() {

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        sendInviteAndAcceptByStudent(tutor, student);

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);

        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        // Getting booking Id
        int bookingId = (int) new JSONObject(jsonObject.getJSONArray("data").get(0).toString()).get("id");

        // Cancelling booking from student  (A little strange to me that in request we use tutor nickname)
        final Response declineResponse = new BookingRequest(builder).decline(student.getCookieFilter(),tutor.getNickname(),bookingId);

        // Confirming booking from tutor
        final Response confirmResponse = new BookingRequest(builder).confirm(tutor.getCookieFilter(),tutor.getNickname(),bookingId);

        confirmResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(false));
    }

    @Test
    public void Student_RequestsBooking_TutorCancels() {

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        sendInviteAndAcceptByStudent(tutor, student);

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);

        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        // Getting booking Id
        int bookingId = (int) new JSONObject(jsonObject.getJSONArray("data").get(0).toString()).get("id");

        // Declining booking from tutor
        final Response declineResponse = new BookingRequest(builder).confirm(tutor.getCookieFilter(),tutor.getNickname(),bookingId);

        declineResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void StudentBooksLessonSameTimeDifferentTutors_FirstTutorAccepts(){
        final Tutor tutor1 = new TutorCreator(Any.tutorUsername()).create();
        final Tutor tutor2 = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        final Response response1 = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor1, student)).asTutor(tutor1, student);
        final Response response2 = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor2, student)).asTutor(tutor2, student);
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor1, student);

        response1.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        JSONObject jsonObject = new JSONObject(response1.getBody().asString());
        int bookingId = (int) new JSONObject(jsonObject.getJSONArray("data").get(0).toString()).get("id");
        final Response confirmResponse = new BookingRequest(builder).confirm(tutor1.getCookieFilter(),tutor1.getNickname(),bookingId);

        confirmResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));

        response2.then()
                .body("description", equalTo("Student already has an unavailability or booking during this time"))
                .body("success", equalTo(false));
    }


    @Test
    public void StudentBooksLessonSameTimeDifferentTutors(){
        final Tutor tutor1 = new TutorCreator(Any.tutorUsername()).create();
        final Tutor tutor2 = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        final Response response1 = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor1, student)).asTutor(tutor1, student);
        final Response response2 = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor2, student)).asTutor(tutor2, student);

        response1.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        response2.then()
                .body("description", equalTo("Student already has an unavailability or booking during this time"))
                .body("success", equalTo(false));
    }

    @Test
    public void StudentBooksLessonSameTimeDifferentTutors_FirstTutorCancels(){
        final Tutor tutor1 = new TutorCreator(Any.tutorUsername()).create();
        final Tutor tutor2 = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        final Response response1 = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor1, student)).asTutor(tutor1, student);
        final Response response2 = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor2, student)).asTutor(tutor2, student);
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor1, student);

        response1.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        JSONObject jsonObject = new JSONObject(response1.getBody().asString());
        int bookingId = (int) new JSONObject(jsonObject.getJSONArray("data").get(0).toString()).get("id");
        final Response declineResponse = new BookingRequest(builder).decline(tutor1.getCookieFilter(),tutor1.getNickname(),bookingId);

        declineResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));

        response2.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
    }



}
