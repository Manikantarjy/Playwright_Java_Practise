package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class DataProviderDemo {

    //The problem with below approach is if we have many parameters, it wont be good if we take too many parameters
    @DataProvider(name = "basicData")  //Multi dimensional array of objects
    public Object[][] basicData() {
        return new Object[][]{{"test1@gmail.com", "password1"}, {"test2@gmail.com", "password2"}};
    }

    @Test(dataProvider = "basicData")
    public void testFillForm(String email, String password) {
        System.out.println(email);
        System.out.println(password);
    }

    //We use hashmap to avoid above problem

    //For this one also i will have to write each and every value
    //To avoid the problem we use the json file

    @DataProvider(name = "hashMapData")  //Multi dimensional array of objects
    public Object[][] hashMapData() {
        HashMap<String, String> user1 = new HashMap<>();
        user1.put("email", "test1@gmail.com");
        user1.put("password", "password1");

        HashMap<String, String> user2 = new HashMap<>();
        user2.put("email", "test2@gmail.com");
        user2.put("password", "password2");

        return new Object[][]{{user1}, {user2}};
    }

    @Test(dataProvider = "hashMapData")
    public void testWithHashMap(HashMap<String, String> data) {
        System.out.println(data.get("email"));
        System.out.println(data.get("password"));
    }

    //using json
    @DataProvider(name = "json")
    public Object[][] testDataFromJson() throws IOException {
        //reading the entire json file into String format
        String jsonContent = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testData_TC1.json")));
        Type type = new TypeToken<List<HashMap<String, String>>>() {
        }.getType();

        //this gson is a library converts entire json into java object (list of hashmaps) - serialization
        List<HashMap<String, String>> list = new Gson().fromJson(jsonContent, type);
        Object[][] table = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            table[i][0] = list.get(i);
        }
        return table;
    }

    @Test(dataProvider = "json")
    public void testWithJsonHashMap(HashMap<String, String> data) {
        System.out.println(data.get("email"));
        System.out.println(data.get("password"));
    }
}