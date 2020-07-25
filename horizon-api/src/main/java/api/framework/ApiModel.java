package api.framework;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
@Data
public class ApiModel  {
    public String name ;
    public String describle;
    public HashMap<String,ApiContent> contents;
    public HashMap<String,Object> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrible() {
        return describle;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }

    public HashMap<String, ApiContent> getContents() {
        return contents;
    }

    public void setContents(HashMap<String, ApiContent> contents) {
        this.contents = contents;
    }

/*    public void load(String path){}
    public void run(ApiContent api){
        api.run();
    }*/
}
