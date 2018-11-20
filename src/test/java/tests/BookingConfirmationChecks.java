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
public class BookingConfirmationChecks {

    @Test
    public void Student_RequestsBookingOutsideTutorAvailableTimeSlot_fail() {
        //Test to check if student fails to book a lesson outside tutor hours
        // Test to check if tutor can cancel a booking

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot2(tutor, student);

        //Send booking request
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
