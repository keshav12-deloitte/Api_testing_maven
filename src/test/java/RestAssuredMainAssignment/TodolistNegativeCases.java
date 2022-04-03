package RestAssuredMainAssignment;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.Excelutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TodolistNegativeCases {
    private  static PrintStream logForNeagativeCases;
    static {
        try {
            logForNeagativeCases = new PrintStream(new File("C:\\Users\\vuchander\\Api_testing_maven\\logs\\logForNeagativeCases.log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    String baseUri = "https://api-nodejs-todolist.herokuapp.com";
    String bearerToken="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MjQ5ODk2MjE2YTc0ZTAwMTc0NjA1ZTQiLCJpYXQiOjE2NDg5ODY0NjZ9.GRqiygO9dVoH__0eg8ocCCG_aSx_ecQ2dXXJGuRANt0";
    static ExtentReports extent = new ExtentReports();
    static ExtentSparkReporter spark = new ExtentSparkReporter("TodolistNeagativeCase.html");
    @BeforeTest
    public void handshake() {
        RestAssured.useRelaxedHTTPSValidation();
    }
    @Test(priority = 1)
    public void registerWithExistingUser()
    {
        extent.attachReporter(spark);
        String excelPath = "C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\userData.xlsx";
        String sheetName = "userData";
        ExtentTest test = extent.createTest("verifying Existing Users Can be Registered Again");
        test.fail("User Already registered Please Sign-in");

        Excelutils excel = new Excelutils(excelPath, sheetName);
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", excel.getCellData(1, 0).toString());
        requestParams.put("email", excel.getCellData(1, 1).toString());
        requestParams.put("password", excel.getCellData(1, 2).toString());
        requestParams.put("age", excel.getCellData(1, 3));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .baseUri(baseUri)
                        .body(requestParams.toString())
                        .filter(ResponseLoggingFilter.logResponseTo(logForNeagativeCases))
                        .when().post("/user/register")
                        .then()
                        .log().ifError()
                        .extract().response();

        JSONObject registerResponse = new JSONObject(response.asString());
        //System.out.println(registerResponse);
        JSONObject userDetails=new JSONObject(registerResponse.get("user").toString());//for only user part of validation
        assertThat(userDetails.get("name"),equalTo("ramesh"));
        assertThat(userDetails.get("email"),equalTo("ramesh@gmail.com"));
        assertThat(userDetails.get("age"),equalTo(45));


    }
    @Test(priority = 2)
    public void loginWithoutRegistration() {

        ExtentTest test = extent.createTest("verifying Users are login without Reistration");

        String excelPath = "C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\userlogindata.xlsx";
        String sheetName = "Sheet1";
        Excelutils excel = new Excelutils(excelPath, sheetName);
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", excel.getCellData(2, 0));
        requestParams.put("password", excel.getCellData(2, 1));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .baseUri(baseUri)
                        .body(requestParams.toString())
                        .filter(ResponseLoggingFilter.logResponseTo(logForNeagativeCases))
                        .when().post("/user/login")
                        .then()
                        .log().all()
                        .extract().response();
        test.fail("Users need to register before login");
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
       test.fail("Users need to register before login");

    }
    @Test(priority = 3)
    public void validatingAddtask() {

        ExtentTest test = extent.createTest("verifying Tasks are Added successfully");
        String excelPath = "C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\TaskData.xlsx";
        String sheetName = "Sheet1";
        Excelutils excel = new Excelutils(excelPath, sheetName);
        JSONObject requestParams = new JSONObject();
        int noOfRows=excel.getRowCount();

        requestParams.put("description", excel.getCellData(19, 0));


        Response response =
                given()
                        .header("Content-type", "application/json")
                        .baseUri(baseUri)
                        .header("Authorization",
                                "Bearer "+bearerToken)
                        .header("Content-Type","application/json")
                        .filter(ResponseLoggingFilter.logResponseTo(logForNeagativeCases))
                        .body(requestParams.toString())
                        .when().post("/task")
                        .then()
                        //.body(matchesJsonSchemaInClasspath("C:\\Users\\vuchander\\Api_testing_maven\\src\\test\\resources\\taskDataSchema.json"))
                        .log().all()
                        .extract().response();

        JSONObject taskdata=new JSONObject(response.asString());
        //System.out.println(taskdata);
        JSONObject taskdatatype=new JSONObject(taskdata.get("data").toString());
        assert taskdatatype.get("description") instanceof String;
        test.fail("Tasks are not Added due to wrong format ");

    }
    @AfterTest
    public void afterTest() {
        extent.flush();
    }
}


