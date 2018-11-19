package utils.data_generation;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.builders.BookingEventDeclineBuilder;
import utils.builders.BookingRequestBuilder;
import utils.builders.RescheduleBuilder;
import utils.domain.Student;
import utils.domain.Tutor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RescheduleData {

    private JSONObject location;

    public RescheduleBuilder rescheduledAt(final Tutor tutor, final Student student, int offset, int duration) {
        RescheduleBuilder rescheduleBuilder = new RescheduleBuilder();
        final BookingRequestBuilder builder = setupDefaultValues(tutor);
        this.location.put("connectedCount", 1);
        builder.events(createEventTimes(offset, duration));

        builder.location(this.location);
        builder.userInfo(new JSONObject().put("nickname", student.getNickname()));

        rescheduleBuilder.decline(setupDecline().getBookingEventDecline());
        rescheduleBuilder.newRequest(builder.getBookingRequest());

        return rescheduleBuilder;
    }

    private BookingRequestBuilder setupDefaultValues(final Tutor tutor) {
        final BookingRequestBuilder builder = new BookingRequestBuilder();
        builder.additionalInfo("additional_info");
        builder.level("level1");
        builder.subjectId(6201);
        builder.zoneId("Europe/Tallinn");
        builder.recurrenceRule("");
        builder.price(20);
        builder.paymentMethod("cash");
        setupDefaultPublicLocation(tutor);
        builder.locationId(String.valueOf(tutor.getPublicLocations().get(0).getId()));

        return builder;
    }

    private void setupDefaultPublicLocation(final Tutor tutor) {
        this.location = new JSONObject()
                .put("id", tutor.getPublicLocations().get(0).getId())
                .put("type", "publicLocation")
                .put("address", "Pärnu maantee 27, 10141 Tallinn, Estonia")
                .put("latitude", 59.4307923)
                .put("longitude", 24.74567980000006)
                .put("label", "Cocoa")
                .put("discount", 0)
                .put("concealed", false);
    }

    private BookingEventDeclineBuilder setupDecline() {
        final BookingEventDeclineBuilder bookingEventDeclineBuilder = new BookingEventDeclineBuilder();
        bookingEventDeclineBuilder.cancelAll(false);
        bookingEventDeclineBuilder.justification("justification");
        return bookingEventDeclineBuilder;
    }

    private JSONArray createEventTimes(int offset, int duration) {
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.plus(offset, ChronoUnit.HOURS).toString();
        final String endDateTime = tomorrow.plus(offset + duration, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));

        return bookingEvents;
    }
}
