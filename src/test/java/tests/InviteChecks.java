package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.Invite;
import utils.data_generation.Any;
import utils.data_generation.StudentCreator;
import utils.data_generation.TutorCreator;
import utils.domain.Student;
import utils.domain.Tutor;
import utils.junit_extensions.PropertiesExtension;

import static com.sun.javafx.fxml.expression.Expression.equalTo;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PropertiesExtension.class)
public class InviteChecks {

    @Test
    public void TutorWithoutConnectedStudents_InvitesStudent_StudentAccepts_Success(){
        Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        Student student = new StudentCreator(Any.studentUsername()).create();

        new Invite().send(tutor, student.getEmail());
        Response invitationLinkResponse = new Invite().invitationLink(student);
        int invitationId = new JSONObject(invitationLinkResponse.getBody().asString()).getJSONObject("data").getInt("id");

        Response acceptResponse = new Invite().accept(invitationId, student);
        System.out.println(acceptResponse.getBody().asString());
        acceptResponse.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

}
