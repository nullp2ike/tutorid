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

import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PropertiesExtension.class)
public class BookingReschedulingChecks {
}
 /*   @Test
    public void StudentBooksAndTutorReschdule_pass() {*/

/*
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
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





