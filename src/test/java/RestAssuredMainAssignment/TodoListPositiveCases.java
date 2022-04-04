package RestAssuredMainAssignment;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.*;
import org.testng.annotations.*;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import utils.Excelutils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import static org.hamcrest.MatcherAssert.assertThat;



public class TodoListPositiveCases {

    private static Logger log=LogManager.getLogger(TodoListPositiveCases.class.getName());
    private  static PrintStream log1;

     {
        try {
            log1 = new PrintStream(new File("C:\\Users\\vuchander\\Api_testing_maven\\logs\\logdemo.log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    String bearerToken;
    String baseUri = "https://api-nodejs-todolist.herokuapp.com";

    static ExtentReports extent = new ExtentReports();//initializing the Extent Report
    static ExtentSparkReporter spark = new ExtentSparkReporter("Todolist.html");//initializing the spark report in Extent Report

    @BeforeTest
    public void handshake() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test(priority = 1)
    public void userRegister() {

        extent.attachReporter(spark);
        ExtentTest test = extent.createTest("verifying Customers are Added successfully");//Creating a test in Extent Report

        String excelPath = "C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\userData.xlsx";
        String sheetName = "userData";
        Excelutils excel = new Excelutils(excelPath, sheetName);//passing excel path and sheetname  as parameters to excelUtlis class


        JSONObject requestParams = new JSONObject();
        requestParams.put("name", excel.getCellData(5, 0).toString());
        requestParams.put("email", excel.getCellData(5, 1).toString());
        requestParams.put("password", excel.getCellData(5, 2).toString());
        requestParams.put("age", excel.getCellData(5, 3));

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .baseUri(baseUri)
                        .filter(ResponseLoggingFilter.logResponseTo(log1))
                        .body(requestParams.toString())
                        .when().post("/user/register")
                        .then()
                        .log().all()
                        .extract().response();

        JSONObject registerResponse = new JSONObject(response.asString());
        JSONObject userDetails=new JSONObject(registerResponse.get("user").toString());//extracting user details object from main object
        //Validating user details from the response we get ,with the expected one
        assertThat(userDetails.get("name"),equalTo("lakshmi"));
        assertThat(userDetails.get("email"),equalTo("lakshmi12@gmail.com"));
        assertThat(userDetails.get("age"),equalTo(44));
        test.pass("customers  are added Successfully");

    }

    @Test(priority = 2)
    public void userlogin() {

        ExtentTest test = extent.createTest("verifying Users are login successfully");//Creating a test in Extent Report

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
                        .filter(ResponseLoggingFilter.logResponseTo(log1))
                        .when().post("/user/login")
                        .then()
                        .log().all()
                        .extract().response();
        JSONObject userlogin = new JSONObject(response.asString());//for the entire json object of response
        bearerToken=userlogin.get("token").toString();//Storing the bearer token for future tests
        //System.out.println(bearerToken);

        JSONObject userDetails=new JSONObject(userlogin.get("user").toString());//for only user part of validation
        assertThat(userDetails.get("name"),equalTo("V Kesava Chander"));
        assertThat(userDetails.get("email"),equalTo("keshavchander100@gmail.com"));
        assertThat(userDetails.get("age"),equalTo(22));
        test.pass("login is done Successfully");//logging in the Extent report
        System.out.println("All credential are matched");


    }

    @Test(priority = 3)
    public void addtask() {

        ExtentTest test = extent.createTest("verifying Tasks are Added successfully");//Creating a test in Extent Report
        String excelPath = "C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\TaskData.xlsx";
        String sheetName = "Sheet1";
        Excelutils excel = new Excelutils(excelPath, sheetName);
        JSONObject requestParams = new JSONObject();
        int noOfRows=excel.getRowCount();
        System.out.println(noOfRows);
        for(int i=1;i<noOfRows;i++){

                   //iterating the loop till we add 20 tasks
            requestParams.put("description", excel.getCellData(i, 0));
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .baseUri(baseUri)
                            .header("Authorization",
                                    "Bearer "+bearerToken)
                            .header("Content-Type","application/json")
                            .body(requestParams.toString())
                            .filter(ResponseLoggingFilter.logResponseTo(log1))
                            .when().post("/task")
                            .then()
                            //.body(matchesJsonSchemaInClasspath("taskDataSchema.json"))
                            .log().all()
                            .extract().response();
            JSONObject taskdata=new JSONObject(response.asString());
            JSONObject taskDatatype=new JSONObject(taskdata.get("data").toString());
            assert taskDatatype.get("description") instanceof String;

        }

        test.pass("Tasks are added successfully");

    }

    @Test(priority = 4)
    public void paginationWithLimits()
    {
        ExtentTest test = extent.createTest("verifying Tasks are filtered based on given limits");
        Response response=
                given()
                        .baseUri(baseUri)
                        .queryParam("limit","5")
                        .header("Authorization",
                                "Bearer "+bearerToken)
                        .header("Content-Type","application/json")
                        .filter(ResponseLoggingFilter.logResponseTo(log1))
                        .when()
                        .get("/task")
                        .then()
                        .log().all()
                        .extract().response();


    }
    @Test(priority = 5)
    public void deleteExistinguserAccount()
    {
        String bearer="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MjQ5YmM4YjE2YTc0ZTAwMTc0NjBlNTUiLCJpYXQiOjE2NDkwNTAzOTF9.PiUWU3vnWKNs3inH3bxcMHJdl7KbpCHwP3v_ThsWkhc";
        ExtentTest test = extent.createTest("verifying Existing User Account are deactivated successfully");
        Response response =
                given()
                        .baseUri(baseUri)
                        .header("Authorization",
                "Bearer "+bearer)
                        .filter(ResponseLoggingFilter.logResponseTo(log1))
                        .when().delete("/user/me")
                        .then()
                        .log().all()
                        .extract().response();
        test.pass("User Account Deleted Successfully");

    }
    @Test(priority = 6)
    public void getAllTask()
    {
        given()
                .baseUri(baseUri)
                .header("Authorization",
                        "Bearer "+bearerToken)
                .header("Content-Type","application/json")
                .filter(ResponseLoggingFilter.logResponseTo(log1))
                .when()
                .get("/task")
                .then()
                .log().all()
                .extract().response();

    }


    @AfterTest
    public void afterTest() {
        extent.flush();//storing all the logs in extent report and closing it
    }
}
