package ru.open.meetup.steps

import cucumber.api.DataTable
import cucumber.api.java.en.Given
import io.qameta.allure.Attachment
import io.restassured.RestAssured.given
import io.restassured.path.json.JsonPath
import io.restassured.response.Response
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import java.net.URLEncoder


/**
 * Created by vk on 22.06.17.
 */
class StepDefs {
    var currentResponse: Response?=null
    var storedJp: JsonPath? =null
    var elem2Delete:Map<String,Object>? = null
    val baseUrl = System.getProperty("AppBaseUrl","http://localhost:5050/")
    val baseUrlApi = baseUrl + "apitest/"


    @Given("^получить счет (\\d*)$")
    fun startGetAcc(id:String):Response {
        val url = baseUrlApi + "accounts/$id"
        currentResponse = given().get(url)
        return currentResponse!!
    }

    @Given("^получить счета$")
    fun startGetAccs() {
        val url = baseUrlApi + "accounts/"
        currentResponse = given().get(url)
    }

    @Given("^сбросить состояние$")
    fun startResetStorage() {
        val url = baseUrl + "kot/resetstorage"
        currentResponse = given().get(url)
    }


    @Given("^получить ответ (.*)$")
    fun startGet(inurl:String) {
        val url = baseUrlApi + inurl
        currentResponse = given().get(url)
    }


    @Given("^удалить счет (\\d*)$")
    fun startDelete(id:String) {
        //store element to delete
        elem2Delete=startGetAcc(id)
                .then()
                .statusCode(200)
                .extract().jsonPath().getMap("")

        val url = baseUrlApi + "accounts/$id/delete"
        currentResponse = given().post(url)

    }

    @Given("^переименовать счет (\\d+) (.*)$")
    fun startUpdate(id:String,newName:String) {
        val url = baseUrlApi + "accounts/$id/rename"
        val bodyData = "name=${URLEncoder.encode(newName,"UTF-8")}"
        currentResponse = given().body(bodyData).post(url)
    }


    @Given("^сохранить ответ$")
    fun storeResponse() {
        storedJp = currentResponse!!.then().extract().jsonPath()
        Attachments.attachResponse(storedJp.toString())
    }

    @Given("^проверить статуст ответа (\\d*)$")
    fun verifyStatus(strcode:String) {
        val numCode = strcode.toInt();
        currentResponse!!.then().statusCode(numCode)
    }

    @Given("^проверить поля ответа списка:$")
    fun verifyListFields(data:DataTable) {
        val jpResp = currentResponse!!.then().extract().jsonPath()
        Attachments.attachResponse(jpResp.toString())
        val listAcc:List<Map<String,Object>> = jpResp.getList("")
        data.asMaps(String::class.java,String::class.java).forEach { row ->
            val filteredList = listAcc.filter { it[row["имя"]].toString().equals(row["значение"]) }
                    .toList();
            assertTrue("${row["имя"]} of value ${row["значение"]} is not found in response",filteredList.size>0);
        }
    }

    @Given("^сравнить ответ с одиночным сохраненным значением:$")
    fun verifyByStored(data:DataTable) {
        assertNotNull("нет сохраненного значения", storedJp)
        val curJp = currentResponse!!.then().extract().jsonPath()
        val storedVal:Map<String,Object> = storedJp!!.getMap("")
        val curVal:Map<String,Object> = curJp.getMap("")
        data.asMaps(String::class.java,String::class.java).forEach { row ->
            val expVal = when(row["проверка"]) {
                "старое" -> storedVal[row["имя"]].toString()
                else -> row["значение"].toString()
            }
            assertThat("поле ${row["имя"]}",curVal[row["имя"]].toString(), equalTo(expVal))
        }

    }

    @Given("^проверить длину списка: (\\d*)$")
    fun verifyListLen(strlen:String) {
        val reqlen = strlen.toInt()
        val jpResp = currentResponse!!.then().extract().jsonPath()
        Attachments.attachResponse(jpResp.toString())
        val listAcc:List<Map<String,Object>> = jpResp.getList("")
        assertThat(listAcc,hasSize(reqlen))
    }



    @Given("^проверить поля одиночного ответа:$")
    fun verifyFields(data:DataTable) {
        val jpResp = currentResponse!!.then().extract().jsonPath()
        Attachments.attachResponse(jpResp.toString())
        val accData:Map<String,Object> = jpResp.getMap("")
        data.asMaps(String::class.java, String::class.java).forEach { row ->
            assertThat(accData[row["имя"]].toString(),equalTo(row["значение"]))
        }
    }

    @Given("^проверить удаление$")
    fun checkDelete() {
        assertNotNull("нет сохраненного значения",storedJp)
        val curJp = currentResponse!!.then().extract().jsonPath()
        val storedVal:List<Map<String,Object>> = storedJp!!.getList("")
        val curVal:List<Map<String,Object>> = curJp.getList("")
        assertThat("неверный размер списка после удаления", curVal.size, equalTo(storedVal.size-1))
        assertTrue("лишние элементы после удаления", storedVal.containsAll(curVal))
        assertFalse("элементы не удалились",curVal.containsAll(storedVal));
        assertThat("after has original element",curVal,not(hasItem(elem2Delete)));
    }


    class Attachments {
        companion object {
            @Attachment("response")
            fun attachResponse(resp:String): ByteArray {
                return resp.toByteArray()
            }

        }
    }
}