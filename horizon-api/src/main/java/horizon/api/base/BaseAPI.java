package horizon.api.base;

import apiobject.APIObjectModel;
import io.restassured.response.Response;

import java.util.Map;

/**
 * @ClassName: BaseAPI
 * @Description: base api of all
 * @Author: zhzh.yin
 * @Date: 2020-04-23 21:31
 * @Verion: 1.0
 */
public class BaseAPI {
    public Map<String, String> getAuthCookie() {
        Map <String,String> map= APIObjectModel.parseParam(BaseAPI.class);
        return login(map).cookies();
    }
    public Response login( Map<String, String> map) {
        Response response =  APIObjectModel.parseAPI(BaseAPI.class, map);

        return response;
    }
}
