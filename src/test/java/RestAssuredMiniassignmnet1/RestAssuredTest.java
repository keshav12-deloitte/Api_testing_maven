package RestAssuredMiniassignmnet1;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.testng.Assert;
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

import java.io.File;


public class RestAssuredTest {
    @BeforeTest
    public void handshake()
    {
        RestAssured.useRelaxedHTTPSValidation();
    }
    @Test(priority = 1)
    public void get_Call() {
        Response response =
                given().header("Content-Type", "application/json").
                        when().get("https://jsonplaceholder.typicode.com/posts")
                        .then().statusCode(200).body("userId[39]", equalTo(4)).body("id[39]", equalTo(40)).
                        extract().response();
        /*JSONArray array=new JSONArray(response.asString());
        boolean flag=false;
        for (int i=0;i<array.length();i++)
        {
            if((Integer.parseInt(array.getJSONObject(i).get("id").toString())==4))
            {
                if(Integer.parseInt(array.getJSONObject(i).get("userId").toString())==40)
                {

                     flag=true;
                }
            }
        }
        System.out.println("hi");
        Assert.assertTrue(flag);
    }
         */
    }
    @Test(priority = 2)
    public void put_call()
    {
        File jsonData=new File("C:\\Users\\vuchander\\Api_testing_maven\\src\\test\\java\\RestAssuredMiniassignmnet1\\adddata.json");
        given()
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .put("https://reqres.in/api/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name",equalTo("Arun"))
                .body("job",equalTo("Manager"));

    }
}
