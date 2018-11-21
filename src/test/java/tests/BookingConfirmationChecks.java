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
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PropertiesExtension.class)
public class BookingConfirmationChecks {


    @Test
    public void Tutor_RequestsBookingOnBehalfOfStudentAndConfirm_Success(){
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student))
                .asTutor(tutor, student);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        System.out.println(response.getBody().asString());

        int bookingId = new JSONObject(response.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response res = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student)).confirm(tutor.getCookieFilter(),tutor.getNickname(), bookingId);
        String bookingEventStatus = new JSONObject(res.getBody().asString())
                .getJSONObject("data").getJSONArray("instances").getJSONObject(0)
                .getString("bookingEventStatus");
        System.out.println(res.getBody().asString());
        System.out.println(bookingEventStatus);

        assertEquals(bookingEventStatus,"confirmed");
    }
    @Test
    public void Student_RequestsBookingAndConfirmViaTutorAvailableTimeSlot_Success(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));
        //  System.out.println(r.getBody().asString());
        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);
        //  System.out.println(builder.build());
        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        //  System.out.println(response.getBody().asString());
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
        int bookingId = new JSONObject(response.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response res = new BookingRequest(builder).confirm(tutor.getCookieFilter(),tutor.getNickname(), bookingId);
        String bookingEventStatus = new JSONObject(res.getBody().asString())
                .getJSONObject("data").getJSONArray("instances").getJSONObject(0)
                .getString("bookingEventStatus");
        System.out.println(res.getBody().asString());
        System.out.println(bookingEventStatus);
        assertEquals(bookingEventStatus,"confirmed");

    }

    @Test
    public void Tutor_RequestBooking_ConfirmAlreadyCancelled_Success(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));
        //  System.out.println(r.getBody().asString());
        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);
        //  System.out.println(builder.build());
        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        //  System.out.println(response.getBody().asString());
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
        int bookingId = new JSONObject(response.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response decline = new BookingRequest(builder).decline(tutor.getCookieFilter(),tutor.getNickname(), bookingId);
        Response confirm = new BookingRequest(builder).confirm(tutor.getCookieFilter(),tutor.getNickname(), bookingId);
        String bookingEventStatus = new JSONObject(confirm.getBody().asString())
                .getJSONObject("data").getJSONArray("instances").getJSONObject(0)
                .getString("bookingEventStatus");
        System.out.println(confirm.getBody().asString());
        assertEquals(bookingEventStatus,"Action not allowed on the selected booking event");

    }

    @Test
    public void Tutor_ConfirmTwoBookingForSameTime_Fail(){
        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        final Student student2 = new StudentCreator(Any.studentUsername()).create();
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);
        final BookingRequestBuilder builder2 = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student2);
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        final Response response2 = new BookingRequest(builder).asStudent(tutor, student2);
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
        response2.then().statusCode(HttpStatus.SC_OK)
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


}
