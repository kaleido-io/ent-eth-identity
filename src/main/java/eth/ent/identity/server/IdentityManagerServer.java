package eth.ent.identity.server;

import static spark.Spark.get;
import static spark.Spark.put;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class IdentityManagerServer {
  
  public static class JsonTransformer implements ResponseTransformer {
    private Gson gson = new Gson();

    @Override public String render(Object model) {
      return gson.toJson(model);
    }
  }

  public static class Thingymobo {
    public String test = "whatsit";

    public String getTest() {
      return test;
    }

    public void setTest(String test) {
      this.test = test;
    }
  }

  public static void main(String[] args) {
    get("/identities", (req, res) -> {
      return new Thingymobo();
    }, new JsonTransformer());
    put("/identities", (req, res) -> {
      return "Hello World";
    });
  }

}
