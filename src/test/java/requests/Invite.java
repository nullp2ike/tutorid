package requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import utils.Setup;
import utils.domain.Student;
import utils.domain.Tutor;

import static io.restassured.RestAssured.given;

public class Invite {

    public Response send(Tutor tutor, String studentEmail){
        String invitePath = Setup.apiUrl +  "/users/" + tutor.getNickname() + "/invitations";
        Response response = given().filter(tutor.getCookieFilter())
                .contentType(ContentType.JSON)
                .body("[{\"email\":\"" + studentEmail + "\"," +
                        "\"emailValid\":true," +
                        "\"firstname\":\"FirstNameValue\"," +
                        "\"firstnameValid\":true," +
                        "\"lastname\":\"LastNameValue\"," +
                        "\"lastnameValid\":true}]")
                .when()
                .post(invitePath);
        response.then().statusCode(HttpStatus.SC_OK);
        return response;
    }

    public Response invitationLink(Student student){
        Response response = given().filter(student.getCookieFilter())
                .get(Setup.apiUrl +  "/services/invitations/invitationLink");
        response.then().statusCode(HttpStatus.SC_OK);
        return response;
    }

    public Response accept(int invitationId, Student student){
        Response response = given().filter(student.getCookieFilter())
                .post(Setup.apiUrl + "/services/invitations/acceptInvitation/" + invitationId);
        response.then().statusCode(HttpStatus.SC_OK);
        return response;
    }

}



