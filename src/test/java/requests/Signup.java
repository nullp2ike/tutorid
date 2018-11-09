package requests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import utils.Setup;
import utils.data_generation.Any;

import static io.restassured.RestAssured.given;
import static utils.Setup.signupUrl;
import static utils.Setup.usersUrl;

public class Signup {

    private CookieFilter cookieFilter;
    private String nickname;
    private boolean isTutor;

    public Signup(String nickname){
        this.cookieFilter = new CookieFilter();
        this.nickname = nickname;
    }

    public Response step1(){
        String content = "{\"email\":\"" + Any.email(nickname) + "\",\"language\":\"en\",\"zoneId\":\"Europe/Tallinn\"}";
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body(content)
                .when()
                .post(signupUrl + "/step-1");
        response.then().statusCode(200);
        return response;
    }

    public Response step2(boolean isTutor){
        this.isTutor = isTutor;
        String fristnamePrefix = "SF";
        String lastnamePrefix = "SL";
        if(isTutor){
            fristnamePrefix = "TU";
            lastnamePrefix = "TL";
        }
        String firstName = fristnamePrefix + Any.alphabetic(4, 10);
        String lastName = lastnamePrefix + Any.alphabetic(4, 10);
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"firstName\":\"" + firstName + "\"," +
                        "\"lastName\":\"" + lastName + "\"," +
                        "\"password\":\"" + Setup.defaultPassword + "\"," +
                        "\"dateOfBirth\":\"1984-10-13\"," +
                        "\"gender\":\"female\"," +
                        "\"phoneNumber\":\"" + RandomStringUtils.randomNumeric(9) +  "\"}")
                .when()
                .put(signupUrl + "/step-2");
        response.then().statusCode(200);
        return response;
    }

    public Response step3(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("{\"nickname\":\"" + this.nickname + "\",\"isTutor\":" + isTutor + "}")
                .when()
                .put(signupUrl + "/step-3");
        response.then().statusCode(200);
        return response;
    }

    public Response step4(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("{\"country\":\"EE\",\"currency\":\"EUR\",\"location\":\"Tallinn, Estonia\",\"latitude\":59.43696079999999,\"longitude\":24.753574699999945}")
                .when()
                .put(signupUrl + "/step-4");
        response.then().statusCode(200);
        return response;
    }

    public Response step5(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("[\"1\",\"2\",\"3\"]")
                .when()
                .put(signupUrl + "/step-5");
        response.then().statusCode(200);
        return response;
    }

    public Response step6(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("{\"value\":\"https://d1sx2jld513c5l.cloudfront.net/6215/profilePictures/pixel_camera_1_20181017T182336.png\"}")
                .when()
                .put(signupUrl + "/step-6");
        response.then().statusCode(200);
        return response;
    }

    public Response subjects(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("[{\"id\":null,\"price\":\"20\",\"subject\":\"English\",\"subjectName\":\"English\",\"subjectNameId\":\"609\",\"subjectLevels\":{\"level1\":true,\"level2\":false,\"level3\":false,\"level4\":false,\"level5\":false,\"level6\":false,\"level7\":false,\"level8\":false}}]")
                .body("[{\"id\":null,\"price\":\"20\",\"subject\":\"English\",\"subjectName\":\"English\",\"subjectNameId\":\"609\",\"subjectLevels\":{\"level1\":true,\"level2\":false,\"level3\":false,\"level4\":false,\"level5\":false,\"level6\":false,\"level7\":false,\"level8\":false}}]")
                .when()
                .post(usersUrl + "/" + nickname + "/subjects");
        response.then().statusCode(200);
        return response;
    }

    public Response interests(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("[\"Cooking\"]")
                .when()
                .post(usersUrl + "/" + nickname + "/interestTags");
        response.then().statusCode(200);
        return response;
    }

    public Response locations1(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("[{\"label\":\"Skype\",\"name\":\"secretfish123\",\"locationType\":\"onlineLocation\"}]")
                .when()
                .post(usersUrl + "/" + nickname + "/locations");
        response.then().statusCode(200);
        return response;
    }

    public Response locations2(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("[{\"label\":\"8\",\"address\":\"Erika 13, 10416 Tallinn, Estonia\",\"latitude\":59.4513564,\"longitude\":24.71535319999998,\"locationType\":\"privateLocation\"}]")
                .when()
                .post(usersUrl + "/" + nickname + "/locations");
        response.then().statusCode(200);
        return response;
    }

    public Response locations3(){
        Response response = given().filter(this.cookieFilter)
                .contentType(ContentType.JSON)
                .body("[{\"label\":\"Cocoa\",\"address\":\"Pärnu maantee 27, 10141 Tallinn, Estonia\",\"latitude\":59.4307923,\"longitude\":24.74567980000006,\"locationType\":\"publicLocation\"}]")
                .when()
                .post(usersUrl + "/" + nickname + "/locations");
        response.then().statusCode(200);
        return response;
    }

    public CookieFilter getCookieFilter() {
        return cookieFilter;
    }
}
