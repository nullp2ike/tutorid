package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PropertiesExtension.class)
public class BookingReschedulingChecks {

    @Test
    //THIS TEST DOES NOT WORK...We have tried to perform rescheduling but we could not succeed.
    public void StudentBooksAndTutorReschdule_pass() {
        //reshedule booking made by tutor.

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student))
                .asTutor(tutor, student);

        JSONObject json  = new JSONObject(response.getBody().asString());
        JSONArray jsonarr= json.getJSONArray("data");
        int eventId = jsonarr.getJSONObject(0).getInt("id");

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        final Response acceptbooking2 = new BookingRequest().confirm(student.getCookieFilter(),tutor.getNickname(), eventId);
        JSONObject confirmationjson2  = new JSONObject(acceptbooking2.getBody().asString());
        System.out.println(confirmationjson2);

        final Response acceptbooking = new BookingRequest().reschedule(tutor.getCookieFilter(),tutor.getNickname(), eventId);
        JSONObject confirmationjson  = new JSONObject(acceptbooking.getBody().asString());
        //System.out.println(confirmationjson);
        final Response acceptbooking3 = new BookingRequest().confirm(student.getCookieFilter(),tutor.getNickname(), eventId);
        JSONObject confirmationjson3  = new JSONObject(acceptbooking3.getBody().asString());
        System.out.println(confirmationjson3);
        /*JSONObject json2  = new JSONObject(acceptbooking.getBody().asString());
        JSONObject jsonNested  = json2.getJSONObject("data");
        JSONArray jsonarr2= jsonNested.getJSONArray("instances");
        String bookingId = jsonarr2.getJSONObject(0).getString("bookingEventStatus");

        assertThat(bookingId).isEqualTo("cancelledByTutor");*/



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




























/*        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
//
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));
        final Response r1 = new Availability().tomorrowFromNow2H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);
        //System.out.println(builder.toString());

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);

        //System.out.println(response.getBody().asString());
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));


        //reschduling


       // final BookingRequestBuilder builder2 = new DefaultBookingData().bookedByStudentViaTimeSlot2(tutor, student);

        //Send booking request
       // final Response response2 = new BookingRequest(builder2).asTutor(tutor, student);
  *//*      final Response response2 = new BookingRequest(new DefaultBookingData().tutorScheduled2(tutor,student)).asTutor(tutor, student);
        System.out.println(response2.getBody().asString());
        response2.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));*//*


    }

    private void sendInviteAndAcceptByStudent(final Tutor tutor, final Student student) {
        new Invite().send(tutor, student.getEmail());
        final Response invitationLinkResponse = new Invite().invitationLink(student);
        final int invitationId = new JSONObject(invitationLinkResponse.getBody().asString()).getJSONObject("data").getInt("id");

        final Response acceptResponse = new Invite().accept(invitationId, student);
        acceptResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }*/





