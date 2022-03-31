import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import  org.hamcrest.Matchers.*;


public class RestAssuredTest {
    @BeforeTest
    public void handshake()
    {
        RestAssured.useRelaxedHTTPSValidation();
    }
    @Test
    public void get_Call()
    {
        Response response;
        given().header("Content-Type","application/json").
                when().get("https://jsonplaceholder.typicode.com/posts")
                .then().statusCode(200).body("userId[39]",equalTo(4)).body("id[39]",equalTo(40));

    }
}
