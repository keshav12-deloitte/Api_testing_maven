package RestAssuredMiniAssignment2;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.with;

import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;//for assertthat


public class request {

    RequestSpecification requestSpecification;
    RequestSpecification requestSpecification1;
    ResponseSpecification responseSpecification;
    File jsonData=new File("C:\\Users\\vuchander\\Api_testing_maven\\src\\test\\java\\RestAssuredMiniassignmnet1\\adddata.json");

    @BeforeTest
    public void handshake()
    {
        RestAssured.useRelaxedHTTPSValidation();
    }
    @BeforeClass
    public void setup()
    {
        RequestSpecBuilder requestSpecBuilder =new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://jsonplaceholder.typicode.com");
        requestSpecification=RestAssured.with().spec(requestSpecBuilder.build());


        RequestSpecBuilder requestSpecBuilder1=new RequestSpecBuilder();
        requestSpecBuilder1.setBaseUri("https://reqres.in/api");
        requestSpecification1=RestAssured.with().spec(requestSpecBuilder1.build());

        ResponseSpecBuilder responseSpecBuilder=new ResponseSpecBuilder().expectStatusCode(200);
        responseSpecification=responseSpecBuilder.build();
    }
    @Test(priority = 1)
            public void get_call()
    {
requestSpecification.header("Content-Type","application/json");
        Response response =requestSpecification.get("/posts");
        requestSpecification.then().body(matchesJsonSchemaInClasspath("schema1.json"));
        JSONArray array=new JSONArray(response.asString());
        boolean flag=false;
        for (int i=0; i<array.length();i++){
            if (Integer.parseInt(array.getJSONObject(i).get("id").toString())==40){
                if (Integer.parseInt(array.getJSONObject(i).get("userId").toString())==4){
                    flag = true;
                }
            }
        }
        Assert.assertTrue(flag);
        assertThat(response.statusCode(),equalTo(200));

    }

    @Test(priority = 2)
    public void put_call()
    {
      requestSpecification1.body(jsonData);
      requestSpecification1.header("Content-Type","application/json");
       Response response1=requestSpecification1.put("/users");
        JSONObject object=new JSONObject(response1.asString());
        System.out.println(object);
        assertThat(object.get("name"),equalTo("Arun"));

    }


}
