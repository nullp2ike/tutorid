package requests;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import utils.Setup;
import utils.domain.Student;
import utils.domain.Tutor;

import static io.restassured.RestAssured.given;

public class Login {

    private CookieFilter cookieFilter;
    private String loginPath = "/login";

    public Login(){
        cookieFilter = new CookieFilter();
    }

    public Student student(String username, String password){
        Response response = login(username, password);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        Response profileResponse = new Profile().get(username, cookieFilter);
        return new Student(profileResponse, cookieFilter);
    }

    public Tutor tutor(String username, String password){
        Response response = login(username, password);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        Response profileResponse = new Profile().get(username, cookieFilter);
        return new Tutor(profileResponse, cookieFilter);
    }

    public Response failedLogin(String username, String password){
        Response response = login(username, password);
        return response;
    }

    private Response login(String username, String password) {

        Response response = given().filter(cookieFilter)
                .contentType(ContentType.URLENC)
                .formParam("username", username)
                .formParam("password", password)
                .formParam("rememberMe", "false")
                .formParam("submit", "Login")
                .when()
                .post( Setup.apiUrl + loginPath);
        return response;
    }

}


