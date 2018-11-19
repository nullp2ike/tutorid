package utils.builders;

import org.json.JSONObject;

public class BookingEventDeclineBuilder {

    private JSONObject bookingEventDecline = new JSONObject();

    public BookingEventDeclineBuilder cancelAll(boolean bool) {
        bookingEventDecline.put("cancelAll", String.valueOf(bool));
        return this;
    }

    public BookingEventDeclineBuilder justification(String justification) {
        bookingEventDecline.put("justification", justification);
        return this;
    }

    public JSONObject getBookingEventDecline() {
        return bookingEventDecline;
    }

    public String build() {
        return bookingEventDecline.toString();
    }
}
