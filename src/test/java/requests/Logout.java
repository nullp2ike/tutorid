package requests;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import utils.Setup;

import static io.restassured.RestAssured.given;

public class Logout {

    public Response logout(CookieFilter cookieFilter) {
        Response response = given().filter(cookieFilter)
                .contentType(ContentType.JSON)
                .when()
                .post( Setup.apiUrl + "/logout");
        response.then().statusCode(HttpStatus.SC_OK);
        return response;
    }

}
