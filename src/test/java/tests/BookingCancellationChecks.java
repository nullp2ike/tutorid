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
public class BookingCancellationChecks {
    @Test
    public void tutor_cancelsBooking_success() {
        // Test to check if tutor can cancel a booking
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

        final Response cancel = new BookingRequest().decline(tutor.getCookieFilter(),tutor.getNickname(), eventId);
        System.out.println(cancel.getBody().asString());

        JSONObject json2  = new JSONObject(cancel.getBody().asString());
        JSONObject jsonNested  = json2.getJSONObject("data");
        JSONArray jsonarr2= jsonNested.getJSONArray("instances");
        String bookingId = jsonarr2.getJSONObject(0).getString("bookingEventStatus");

        assertThat(bookingId).isEqualTo("cancelledByTutor");

    }

    @Test
    public void student_cancelsBooking_success() {
        // Test to check if student can cancel a booking

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        JSONObject json  = new JSONObject(response.getBody().asString());
        JSONArray jsonarr= json.getJSONArray("data");
        int eventId = jsonarr.getJSONObject(0).getInt("id");

        final Response cancel = new BookingRequest().decline(student.getCookieFilter(),tutor.getNickname(), eventId);
        System.out.println(cancel.getBody().asString());

        JSONObject json2  = new JSONObject(cancel.getBody().asString());
        JSONObject jsonNested  = json2.getJSONObject("data");
        JSONArray jsonarr2= jsonNested.getJSONArray("instances");
        String bookingEventStatus = jsonarr2.getJSONObject(0).getString("bookingEventStatus");

        assertThat(bookingEventStatus).isEqualTo("cancelledByStudent");

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
    public void student_cancelsBookingByDifferentStudent_fail() {
        //Test for testing if a booking done by a student can be cancelled by another student --> Should fail

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        final Student student2 = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        JSONObject json  = new JSONObject(response.getBody().asString());
        JSONArray jsonarr= json.getJSONArray("data");
        int eventId = jsonarr.getJSONObject(0).getInt("id");

        final Response cancel = new BookingRequest().decline(student2.getCookieFilter(),tutor.getNickname(), eventId);
        System.out.println(cancel.getBody().asString());

        cancel.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("User does not have permissions for that action"));
    }

    @Test
    public void tutor_cancelsBookingByDifferentTutor_fail() {
        //Test for testing if a booking done by a tutor can be cancelled by another tutor --> should fail

        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();
        final Tutor tutor2 = new TutorCreator(Any.tutorUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student))
                .asTutor(tutor, student);

        JSONObject json  = new JSONObject(response.getBody().asString());
        JSONArray jsonarr= json.getJSONArray("data");
        int eventId = jsonarr.getJSONObject(0).getInt("id");

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        final Response cancel = new BookingRequest().decline(tutor2.getCookieFilter(),tutor.getNickname(), eventId);
        System.out.println(cancel.getBody().asString());

        cancel.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("User does not have permissions for that action"));

    }

    @Test
    public void student_cancelsBookingMadeByTutor_success(){

            final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
            final Student student = new StudentCreator(Any.studentUsername()).create();

            final Response response = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student))
                    .asTutor(tutor, student);

            response.then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("description", equalTo("Booking created"));

            JSONObject jsonresponse  = new JSONObject(response.getBody().asString());
            System.out.println(response.getBody().asString());
            JSONArray jsondata= jsonresponse .getJSONArray("data");
            int eventId = jsondata.getJSONObject(0).getInt("id");

            final Response acceptbooking = new BookingRequest().decline(student.getCookieFilter(),tutor.getNickname(), eventId);


            JSONObject confirmationjson  = new JSONObject(acceptbooking.getBody().asString());
            System.out.println(confirmationjson);
            JSONObject dataforbooking  = confirmationjson.getJSONObject("data");
            JSONArray instanceunderdata = dataforbooking.getJSONArray("instances");
            String booking = instanceunderdata.getJSONObject(0).getString("bookingEventStatus");
            assertThat(booking).isEqualTo("cancelledByStudentPending");
        }

    @Test
    public void tutor_cancelsBookingMadeBystudent_success(){

        // Test to check if tutor can confirm a booking made by student on his available dates

        //First we create a logged in tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        //Tutor adds his/her availability for tomorrow
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));
        // System.out.println(r.getBody().asString());

        //Tutor invites student to join his/her network
        sendInviteAndAcceptByStudent(tutor, student);

        //Stetup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        // System.out.println(response.getBody().asString());
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        JSONObject json  = new JSONObject(response.getBody().asString());
        JSONArray jsonarr= json.getJSONArray("data");
        int eventId = jsonarr.getJSONObject(0).getInt("id");
        final Response acceptbooking = new BookingRequest().decline(tutor.getCookieFilter(),tutor.getNickname(), eventId);

        JSONObject confirmationjson  = new JSONObject(acceptbooking.getBody().asString());
        System.out.println(confirmationjson);
        JSONObject dataforbooking  = confirmationjson.getJSONObject("data");
        JSONArray instanceunderdata = dataforbooking.getJSONArray("instances");
        String booking = instanceunderdata.getJSONObject(0).getString("bookingEventStatus");
        assertThat(booking).isEqualTo("cancelledByTutorPending");
    }
    }





