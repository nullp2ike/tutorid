package requests;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Setup;

import static io.restassured.RestAssured.given;

public class Profile {

    public Response get(String nickname, CookieFilter cookieFilter){
        Response response = given().filter(cookieFilter)
                .contentType(ContentType.JSON)
                .get(Setup.usersUrl + "/" + nickname);
        response.then().statusCode(200);
        return response;
    }

}
