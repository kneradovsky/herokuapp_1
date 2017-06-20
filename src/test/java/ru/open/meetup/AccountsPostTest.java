package ru.open.meetup;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by vk on 19.06.17.
 */
public class AccountsPostTest {
  private String baseUrl = hostUrl+"apitest";
  private static String hostUrl = System.getProperty("AppBaseURL","https://kn-ktapp.herokuapp.com/");

  @BeforeClass
  public static void beforeAllTests() {
    get(hostUrl+"kot/resetstorage")
        .then()
        .statusCode(200);
  }

  @Test
  public void renameTest() {
    int accId = 12345682;
    String newName = "testname1";
    JsonPath beforeJP = get(baseUrl + "/accounts/" + accId)
        .then()
        .statusCode(200)
        .extract().jsonPath();
    Map<String, Object> beforeAcc = beforeJP.getMap("");

    given().body("name=" + newName)
        .post(baseUrl+"/accounts/"+accId+"/rename")
        .then()
        .statusCode(200)
        .body("title", equalTo(newName))
        .body("account_id", equalTo(accId));

    JsonPath afterJP = get(baseUrl + "/accounts/" + accId)
        .then()
        .statusCode(200)
        .extract().jsonPath();
    Map<String, Object> afterAcc = afterJP.getMap("");
    assertThat("element has wrong id", afterAcc.get("account_id"), Matchers.equalTo(beforeAcc.get("account_id")));
    assertThat("element has wrong title", afterAcc.get("title"), Matchers.equalTo(newName));
    assertThat("element has wrong small title", afterAcc.get("title_small"), Matchers.equalTo(beforeAcc.get("title_small")));
    assertThat("element has wrong currency", afterAcc.get("currency"), Matchers.equalTo(beforeAcc.get("currency")));
  }

  @Test
  public void renameInexistentAccount() {
    Integer accId = 55566777;
    String newName = "testname1";
    given().body("name=" + newName)
        .post(baseUrl+"/accounts/"+accId+"/rename")
        .then()
        .statusCode(404);
  }

  @Test
  public void renameInvalidAccount() {
    Integer accId = 55566777;
    String newName = "testname1";
    given().body("name=" + newName)
        .post(baseUrl+"/accounts//rename")
        .then()
        .statusCode(404);
  }

  @Test
  public void renameInvalidOperation() {
    Integer accId = 12345681;
    String newName = "testname1";
    given().body("name=" + newName)
        .post(baseUrl+"/accounts/"+accId+"/")
        .then()
        .statusCode(405);
  }

  @Test
  public void postUnknownOperation() {
    Integer accId = 12345681;
    String newName = "testname1";
    given().body("name=" + newName)
        .post(baseUrl+"/accounts/"+accId+"/operunknown")
        .then()
        .statusCode(400);
  }

  @Test
  public void postDelete() {
    int accId = 12345680;
    JsonPath beforeJP = get(baseUrl + "/accounts")
        .then()
        .statusCode(200)
        .extract().jsonPath();
    List<Map<String, Object>> beforeAccs = beforeJP.getList("");
    Map<String,Object> elem2delete = beforeAccs.stream()
        .filter(m -> ((Integer)m.get("account_id"))==accId).findFirst()
        .orElse(null);
    assertNotNull("object to delete is not found, check test data",elem2delete);

    post(baseUrl+"/accounts/"+accId+"/delete")
        .then()
        .statusCode(204);

    JsonPath afterJP = get(baseUrl + "/accounts")
        .then()
        .statusCode(200)
        .extract().jsonPath();
    List<Map<String, Object>> afterAccs = afterJP.getList("");
    assertThat("before and after has equal size", afterAccs.size(), Matchers.equalTo(beforeAccs.size()-1));
    assertTrue("before doesn't contain all from after", beforeAccs.containsAll(afterAccs));
    assertFalse("after contains all element from before",afterAccs.containsAll(beforeAccs));
    assertThat("after has original element",afterAccs,not(hasItem(elem2delete)));
  }


}
