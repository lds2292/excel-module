package sfn.excel.module;

import com.google.gson.Gson;

public class TestFunctions {

    public static class Data {
        String title = "title";
        String subtitle = "title";

    }


    public static String generateStringUsingGson() {
        Gson gson = new Gson();
        Data data = new Data();
        return gson.toJson(data);
    }

    public static String generateHelloWorldString() {
        return "Hello World - SFN Excel Module - Wonder :)";
    }
}
