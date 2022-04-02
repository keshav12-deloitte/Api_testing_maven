package RestAssuredMainAssignment;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.testng.annotations.*;
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
import org.testng.annotations.BeforeTest;
import org.testng.asserts.Assertion;
import utils.Excelutils;
import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;

public class TodoList {

    String bearerToken;
    String baseUri = "https://api-nodejs-todolist.herokuapp.com";
    //Excelutils excel=new Excelutils(excelPath,sheetName);
    static ExtentReports extent = new ExtentReports();
    static ExtentSparkReporter spark = new ExtentSparkReporter("Todolist.html");

    @BeforeTest
    public void handshake() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test(priority = 1)
    public void userRegister() {
        extent.attachReporter(spark);
        String excelPath = "C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\userData.xlsx";
        String sheetName = "userData";
        ExtentTest test = extent.createTest("verifying Customers are Added successfully");

        Excelutils excel = new Excelutils(excelPath, sheetName);
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", excel.getCellData(3, 0).toString());
        requestParams.put("email", excel.getCellData(3, 1).toString());
        requestParams.put("password", excel.getCellData(3, 2).toString());
        requestParams.put("age", excel.getCellData(3, 3));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .baseUri(baseUri)
                        .body(requestParams.toString())
                        .when().post("/user/register")
                        .then()
                        .log().all()
                        .extract().response();

        JSONObject registerResponse = new JSONObject(response.asString());
        //System.out.println(registerResponse);
        JSONObject userDetails=new JSONObject(registerResponse.get("user").toString());//for only user part of validation
        assertThat(userDetails.get("name"),equalTo("ramesh"));
        assertThat(userDetails.get("email"),equalTo("ramesh@gmail.com"));
        assertThat(userDetails.get("age"),equalTo(45));
        test.pass("customers  are added Successfully");

    }

    @Test(priority = 2)
    public void userlogin() {

        ExtentTest test = extent.createTest("verifying Users are login successfully");
        test.pass("login is done Successfully");
        String excelPath = "C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\userlogindata.xlsx";
        String sheetName = "Sheet1";
        Excelutils excel = new Excelutils(excelPath, sheetName);
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", excel.getCellData(1, 0));
        requestParams.put("password", excel.getCellData(1, 1));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .baseUri(baseUri)
                        .body(requestParams.toString())
                        .when().post("/user/login")
                        .then()
                        .log().all()
                        .extract().response();
        JSONObject userlogin = new JSONObject(response.asString());//for the entire json object of response
        //System.out.println(userlogin);
        //int status=response.getStatusCode();
        //assertThat(status,equalTo(200));
        //System.out.println("hi");
        bearerToken=userlogin.get("token").toString();
        //System.out.println(bearerToken);
        JSONObject userDetails=new JSONObject(userlogin.get("user").toString());//for only user part of validation
        assertThat(userDetails.get("name"),equalTo("V Kesava Chander"));
        assertThat(userDetails.get("email"),equalTo("keshavchander100@gmail.com"));
        assertThat(userDetails.get("age"),equalTo(22));
        System.out.println("All credential are matched");

    }

    @Test(priority = 3)
    public void addtask() {

        ExtentTest test = extent.createTest("verifying Tasks are Added successfully");
        String excelPath = "C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\TaskData.xlsx";
        String sheetName = "Sheet1";
        Excelutils excel = new Excelutils(excelPath, sheetName);
        JSONObject requestParams = new JSONObject();
        int noOfRows=excel.getRowCount();
        for(int i=1;i<noOfRows;i++){
            requestParams.put("description", excel.getCellData(i, 0));
        }

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .baseUri(baseUri)
                        .header("Authorization",
                                "Bearer "+bearerToken)
                        .header("Content-Type","application/json")
                        .body(requestParams.toString())
                        .when().post("/task")
                        .then()
                        .log().all()
                        .extract().response();

        //JSONObject taskdata=new JSONObject(response.asString());
        //System.out.println(taskdata);
        test.pass("Tasks are added successfully");

    }
    @Test(priority = 4)
    public void paginationWithLimits()
    {
        ExtentTest test = extent.createTest("verifying Tasks are filtered based on given limits");
        Response response=
                given()
                        .baseUri(baseUri)
                        .queryParam("limit","2")
                        .header("Authorization",
                                "Bearer "+bearerToken)
                        .header("Content-Type","application/json")
                        .when()
                        .get("/task")
                        .then()
                        .log().all()
                        .extract().response();
        //JSONObject taskPagination=new JSONObject(response.asString());
        //System.out.println(taskPagination);

    }


    @AfterTest
    public void afterTest() {
        extent.flush();
    }
}
