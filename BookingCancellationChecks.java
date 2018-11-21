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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PropertiesExtension.class)
public class BookingCancellationChecks {

    //1
    @Test
    public void Tutor_CancelsBooking_Success() {

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response bookingRequestResponse = new BookingRequest(new DefaultBookingData().
                tutorScheduled(tutor, student)).asTutor(tutor, student);

        bookingRequestResponse.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        final int requestId = new JSONObject(bookingRequestResponse.getBody().
                asString()).getJSONArray("data").getJSONObject(0).getInt("id");

        final Response cancelBookingResponse = new BookingRequest().decline(tutor.getCookieFilter(), tutor.getNickname(), requestId);

        final String bookingEventStatus = new JSONObject(cancelBookingResponse.getBody().asString()).getJSONObject("data").getJSONArray("instances").getJSONObject(0).getString("bookingEventStatus");

        assertThat(bookingEventStatus).isEqualTo("cancelledByTutor");
    }
    //2
    @Test
    void Student_CancelsBooking_Success() {

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response addAvailabilityResponse = new Availability().tomorrowFromNow1H().add(tutor);

        addAvailabilityResponse.then().statusCode(HttpStatus.SC_OK).body("description", equalTo("Availability created"));
        sendInviteAndAcceptByStudent(tutor, student);

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);
        final Response bookingRequestResponse = new BookingRequest(builder).asStudent(tutor, student);

        final int requestId = new JSONObject(bookingRequestResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");

        final Response cancelBookingResponse = new BookingRequest().decline(student.getCookieFilter(), tutor.getNickname(), requestId);

        final String bookingEventStatus = new JSONObject(cancelBookingResponse.getBody().asString()).getJSONObject("data").getJSONArray("instances").getJSONObject(0).getString("bookingEventStatus");

        assertThat(bookingEventStatus).isEqualTo("cancelledByStudent");
    }
    //3
    @Test
    void Student_DeclinesBooking_Success() {

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response addAvailabilityResponse = new Availability().tomorrowFromNow1H().add(tutor);

        addAvailabilityResponse.then().statusCode(HttpStatus.SC_OK).body("description", equalTo("Availability created"));

        final Response bookingRequestResponse = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student)).asTutor(tutor, student);

        bookingRequestResponse.then().statusCode(HttpStatus.SC_OK).body("description", equalTo("Booking created"));

        final BookingRequestBuilder builder = new DefaultBookingData().tutorScheduled(tutor, student);

        final int requestId = new JSONObject(bookingRequestResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        final Response declineBookingResponse = new BookingRequest(builder).decline(tutor.getCookieFilter(), tutor.getNickname(), requestId);

        final String bookingEventStatus = new JSONObject(declineBookingResponse.getBody().asString()).getJSONObject("data").getJSONArray("instances").getJSONObject(0).getString("bookingEventStatus");

        assertThat(bookingEventStatus.equals("declined"));
    }
    //4
    @Test
    void Tutor_DeclinesBooking_Success() {

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response bookingRequestResponse = new BookingRequest(new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student)).asStudent(tutor, student);

        bookingRequestResponse.then().statusCode(HttpStatus.SC_OK).body("description", equalTo("Booking created"));

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        final int requestId = new JSONObject(bookingRequestResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        final Response declineBookingResponse = new BookingRequest(builder).decline(student.getCookieFilter(), student.getNickname(), requestId);

        final String bookingEventStatus = new JSONObject(declineBookingResponse.getBody().asString()).getJSONObject("data").getJSONArray("instances").getJSONObject(0).getString("bookingEventStatus");

        assertThat(bookingEventStatus.equals("declined"));
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