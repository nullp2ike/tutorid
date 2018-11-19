package utils.builders;

import org.json.JSONObject;

public class RescheduleBuilder {

    private JSONObject reschedule = new JSONObject();

    public RescheduleBuilder decline(JSONObject decline) {
        reschedule.put("bookingEventDecline", decline);
        return this;
    }

    public RescheduleBuilder newRequest(JSONObject request) {
        reschedule.put("newBookingRequest", request);
        return this;
    }

    public String build() {
        return reschedule.toString();
    }
}
