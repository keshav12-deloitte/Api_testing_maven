package RestAssuredMiniassignmnet1;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class practice {
    @BeforeTest
    public void handshake() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test(priority = 1)
    public void array()
    {
        Response response=
        given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .log().all()
                .when()
                .get("/posts")
                .then()
                .statusCode(209)
                .log().ifError()
                .extract().response();
        JSONArray array=new JSONArray(response.asString());
        boolean test=false;
        for(int i=0;i< array.length();i++)
        {
            if(Integer.parseInt(array.getJSONObject(i).get("id").toString())==40)
            {
                if(Integer.parseInt(array.getJSONObject(i).get("userId").toString())==4)
                {
                    test=true;
                }
            }
        }
        Assert.assertTrue(test);
        System.out.println("you are pro");

    }
}
