package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.Availability;
import requests.BookingRequest;
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
public class BookingCancellationChecks {


    @Test
    public void Tutor_RequestBookingAnd_Decline_Success() {
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student))
                .asTutor(tutor, student);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        System.out.println(response.getBody().asString());

        int bookingId = new JSONObject(response.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response res = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student)).decline(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        String bookingEventStatus = new JSONObject(res.getBody().asString())
                .getJSONObject("data").getJSONArray("instances").getJSONObject(0)
                .getString("bookingEventStatus");
        System.out.println(res.getBody().asString());
        System.out.println(bookingEventStatus);

        assertEquals(bookingEventStatus, "cancelledByTutorPending");
    }

    @Test
    public void Tutor_RequestBookingAnd_ConfirmAndDecline_Fail() {
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        BookingRequest bookingRequest = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student));
        final Response response = bookingRequest.asTutor(tutor, student);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        //System.out.println(response.getBody().asString());

        int bookingId = new JSONObject(response.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response confirm = bookingRequest.confirm(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        Response confirmDecline = bookingRequest.confirmDecline(tutor.getCookieFilter(), tutor.getNickname(), bookingId);
        String bookingEventStatus = new JSONObject(confirmDecline.getBody().asString())
                .getJSONObject("data").getJSONArray("instances").getJSONObject(0)
                .getString("bookingEventStatus");
        System.out.println(confirmDecline.getBody().asString());
        System.out.println(bookingId);
        //  System.out.println(bookingEventStatus);
        assertEquals(bookingEventStatus, "cancelledByTutorConfirmed");

    }


    @Test
    public void Student_RequestBookingAnd_Decline_Fail() {
        final Tutor tutor = new TutorCreator().create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
        int bookingId = new JSONObject(response.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
        Response res = new BookingRequest(builder).decline(student.getCookieFilter(), student.getNickname(), bookingId);
        System.out.println(res.getBody().asString());

        String bookingEventStatus = new JSONObject(res.getBody().asString())
                .getJSONObject("data").getJSONArray("instances").getJSONObject(0)
                .getString("bookingEventStatus");
        System.out.println(bookingEventStatus);
        assertEquals(bookingEventStatus, "cancelledByStudentPending");

    }


}
