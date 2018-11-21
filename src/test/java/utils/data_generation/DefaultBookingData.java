package utils.data_generation;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.builders.BookingRequestBuilder;
import utils.domain.Student;
import utils.domain.Tutor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DefaultBookingData {

    private JSONObject location;

    public BookingRequestBuilder tutorScheduled(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValues(tutor);
        this.location.put("connectedCount", 1);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("nickname", student.getNickname()));
        return builder;
    }

    public BookingRequestBuilder tutorScheduled2(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValues2(tutor);
        this.location.put("connectedCount", 1);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("nickname", student.getNickname()));
        return builder;
    }

    public BookingRequestBuilder bookedByStudentViaTimeSlot(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValues(tutor);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("email", student.getEmail())
                          .put("firstName",student.getFirstName())
                          .put("lastName",student.getLastName())
                          .put("language","en")
                          .put("nickname",student.getNickname())
        );
        return builder;
    }

    public BookingRequestBuilder bookedByStudentViaTimeSlot2(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValues2(tutor);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("email", student.getEmail())
                .put("firstName",student.getFirstName())
                .put("lastName",student.getLastName())
                .put("language","en")
                .put("nickname",student.getNickname())
        );
        return builder;
    }

    public BookingRequestBuilder bookedByStudentViaTimeSlot3(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValues3(tutor);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("email", student.getEmail())
                .put("firstName",student.getFirstName())
                .put("lastName",student.getLastName())
                .put("language","en")
                .put("nickname",student.getNickname())
        );
        return builder;
    }

    private BookingRequestBuilder setupDefaultValues(final Tutor tutor){
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
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.toString();
        final String endDateTime = tomorrow.plus(1, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        return builder;
    }

    private BookingRequestBuilder setupDefaultValues2(final Tutor tutor){
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
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(2, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.toString();
        final String endDateTime = tomorrow.plus(2, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        return builder;
    }

    private BookingRequestBuilder setupDefaultValues3(final Tutor tutor){
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
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(3, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.toString();
        final String endDateTime = tomorrow.plus(10, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        return builder;
    }

    private void setupDefaultPublicLocation(final Tutor tutor){
        this.location = new JSONObject()
                .put("id", tutor.getPublicLocations().get(0).getId())
                .put("type","publicLocation")
                .put("address", "Pärnu maantee 27, 10141 Tallinn, Estonia")
                .put("latitude", 59.4307923)
                .put("longitude", 24.74567980000006)
                .put("label", "Cocoa")
                .put("discount", 0)
                .put("concealed", false);
    }
}
