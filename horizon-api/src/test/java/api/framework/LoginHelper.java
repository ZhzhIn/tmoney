package api.framework;

public class LoginHelper {
    public static void login(){
        ApiHelper basePO = new ApiHelper();
        ApiHelper.run(basePO.load("src/test/resources/apiyaml/base.yaml").getContents().get("login"));
    }
}
