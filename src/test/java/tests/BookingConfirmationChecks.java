package tests;

import org.junit.jupiter.api.extension.ExtendWith;
import utils.junit_extensions.PropertiesExtension;

@ExtendWith(PropertiesExtension.class)
public class BookingConfirmationChecks {
  
@Test
    public void Student_confirmbookingmadebytutor_success() {
        // Test to check if student can confirm a booking done by tutor on his behalf

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

        final Response acceptbooking = new BookingRequest().confirm(tutor.getCookieFilter(),tutor.getNickname(), eventId);



       JSONObject confirmationjson  = new JSONObject(acceptbooking.getBody().asString());
        System.out.println(confirmationjson);
        JSONObject dataforbooking  = confirmationjson.getJSONObject("data");
        JSONArray instanceunderdata = dataforbooking.getJSONArray("instances");
        String booking = instanceunderdata.getJSONObject(0).getString("bookingEventStatus");
        assertThat(booking).isEqualTo("confirmed");
    }

    @Test
    public void tutor_confirmbookingmadebystudent_success() {
        // Test to check if tutor can confirm a booking made by student on his available dates

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
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        System.out.println(response.getBody().asString());
        response.then().statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));

        JSONObject json  = new JSONObject(response.getBody().asString());
        JSONArray jsonarr= json.getJSONArray("data");
        int eventId = jsonarr.getJSONObject(0).getInt("id");
        final Response acceptbooking = new BookingRequest().confirm(tutor.getCookieFilter(),tutor.getNickname(), eventId);

        JSONObject confirmationjson  = new JSONObject(acceptbooking.getBody().asString());
        System.out.println(confirmationjson);
        JSONObject dataforbooking  = confirmationjson.getJSONObject("data");
        JSONArray instanceunderdata = dataforbooking.getJSONArray("instances");
        String booking = instanceunderdata.getJSONObject(0).getString("bookingEventStatus");
        assertThat(booking).isEqualTo("confirmed");

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
    public void student_ConfirmAlreadycancelledBooking_fail() {
        //Test for testing confirming an already cancelled booking  ==== should see "Action not allowed on the selected booking event"
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
        JSONObject jsonbody  = new JSONObject(response.getBody().asString());
        JSONArray jsonbodydata = jsonbody.getJSONArray("data");
        int eventId = jsonbodydata.getJSONObject(0).getInt("id");
        final Response cancel = new BookingRequest().decline(student.getCookieFilter(),tutor.getNickname(), eventId);
        System.out.println(cancel.getBody().asString());
        JSONObject jsonforcancel  = new JSONObject(cancel.getBody().asString());
        JSONObject jsoncancelbody  = jsonforcancel.getJSONObject("data");
        JSONArray jsonainstance= jsoncancelbody.getJSONArray("instances");
        String bookingEventStatus = jsonainstance.getJSONObject(0).getString("bookingEventStatus");
        assertThat(bookingEventStatus).isEqualTo("cancelledByStudent");

        final Response accpetcancelledbooking = new BookingRequest().confirm(student.getCookieFilter(),tutor.getNickname(), eventId);
        System.out.println(accpetcancelledbooking.getBody().asString());
    }

}
