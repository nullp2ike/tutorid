package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.Availability;
import requests.BookingRequest;
import utils.builders.BookingEventDeclineBuilder;
import utils.builders.BookingRequestBuilder;
import utils.builders.RescheduleBuilder;
import utils.data_generation.*;
import utils.domain.Student;
import utils.domain.Tutor;
import utils.junit_extensions.PropertiesExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PropertiesExtension.class)
public class BookingReschedulingChecks {
    @Test
    public void RescheduleNegativeDurationLesson() {
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Creates new availability for tutor, tomorrow and 1H in length
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        //Creates the request body builder for the booking request
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Creates the actual request(puts the body inside it using the builder)
        //Response contains the created booking request ID, which is necessary for confirming the booking
        BookingRequest bookingRequest = new BookingRequest(builder);
        Response bookingResponse = bookingRequest.asStudent(tutor, student);

        //Confirms the booking
        int bookingId = new JSONObject(bookingResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response bookingConfirmationResponse = bookingRequest.confirm(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        bookingConfirmationResponse
                .then()
                .statusCode(HttpStatus.SC_OK);

        //Similar to the booking request
        final RescheduleBuilder rescheduleBuilder = new RescheduleData().rescheduledAt(tutor, student, 0, -1);
        final Response rescheduleResponse = new BookingRequest().reschedule(tutor.getCookieFilter(), rescheduleBuilder, tutor.getNickname(), bookingId);
        //Checks if the response is correct, and in this case that the success message equals false
        rescheduleResponse
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(false));
    }

    //Currently is allowed
    @Test
    public void RescheduleZeroDurationLesson() {
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        BookingRequest bookingRequest = new BookingRequest(builder);
        Response bookingResponse = bookingRequest.asStudent(tutor, student);

        int bookingId = new JSONObject(bookingResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response bookingConfirmationResponse = bookingRequest.confirm(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        bookingConfirmationResponse
                .then()
                .statusCode(HttpStatus.SC_OK);

        final RescheduleBuilder rescheduleBuilder = new RescheduleData().rescheduledAt(tutor, student, 0, 0);
        final Response rescheduleResponse = new BookingRequest().reschedule(tutor.getCookieFilter(), rescheduleBuilder, tutor.getNickname(), bookingId);
        rescheduleResponse
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void SimpleReschedule() {
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        BookingRequest bookingRequest = new BookingRequest(builder);
        Response bookingResponse = bookingRequest.asStudent(tutor, student);

        int bookingId = new JSONObject(bookingResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response bookingConfirmationResponse = bookingRequest.confirm(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        bookingConfirmationResponse
                .then()
                .statusCode(HttpStatus.SC_OK);

        final RescheduleBuilder rescheduleBuilder = new RescheduleData().rescheduledAt(tutor, student, 0, 1);
        final Response rescheduleResponse = new BookingRequest().reschedule(tutor.getCookieFilter(), rescheduleBuilder, tutor.getNickname(), bookingId);
        rescheduleResponse
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void RescheduleTooLongDurationLesson() {
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        BookingRequest bookingRequest = new BookingRequest(builder);
        Response bookingResponse = bookingRequest.asStudent(tutor, student);

        int bookingId = new JSONObject(bookingResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response bookingConfirmationResponse = bookingRequest.confirm(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        bookingConfirmationResponse
                .then()
                .statusCode(HttpStatus.SC_OK);

        final RescheduleBuilder rescheduleBuilder = new RescheduleData().rescheduledAt(tutor, student, 0, 99);
        final Response rescheduleResponse = new BookingRequest().reschedule(tutor.getCookieFilter(), rescheduleBuilder, tutor.getNickname(), bookingId);
        rescheduleResponse
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(false));
    }

    @Test
    public void StudentReschedules_TutorDeclines() {
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        BookingRequest bookingRequest = new BookingRequest(builder);
        Response bookingResponse = bookingRequest.asStudent(tutor, student);

        int bookingId = new JSONObject(bookingResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response bookingConfirmationResponse = bookingRequest.confirm(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        bookingConfirmationResponse
                .then()
                .statusCode(HttpStatus.SC_OK);

        final RescheduleBuilder rescheduleBuilder = new RescheduleData().rescheduledAt(tutor, student, 0, 1);
        final Response declineResponse = new BookingRequest().decline(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        declineResponse
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void Tutor_RequestsBookingOnBehalfOfStudent_StudentReschedules_Success(){
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student))
                .asTutor(tutor, student);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
        final BookingRequestBuilder builder = new DefaultBookingData().tutorScheduled(tutor, student);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        // Getting booking Id
        int bookingId = (int) new JSONObject(jsonObject.getJSONArray("data").get(0).toString()).get("id");

        // Confirming booking from tutor
        final Response declineResponse = new BookingRequest(builder).decline(tutor.getCookieFilter(),tutor.getNickname(),bookingId);

        // Formating JSON
        JSONObject declineResponseJson = new JSONObject(declineResponse.getBody().asString());
        String instances = (String) new JSONObject(declineResponseJson.getJSONObject("data")
                .toString())
                .getJSONArray("instances")
                .get(0)
                .toString();
        String bookingEventStatus = new JSONObject(instances).getString("bookingEventStatus");

        final RescheduleBuilder rescheduleBuilder = new RescheduleData().rescheduledAt(tutor, student, 0, 99);
        final Response rescheduleResponse = new BookingRequest().reschedule(tutor.getCookieFilter(), rescheduleBuilder, tutor.getNickname(), bookingId);
        rescheduleResponse
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));

        // Checking for success
        assertThat(bookingEventStatus.equals("rescheduled"));

    }

}
