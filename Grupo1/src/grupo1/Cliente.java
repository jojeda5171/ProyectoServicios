package grupo1;

import okhttp3.*;
import org.json.JSONObject;

public class Cliente {

    OkHttpClient cliente = new OkHttpClient();

    public JSONObject getJSON(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = cliente.newCall(request).execute()) {
            JSONObject json = new JSONObject(response.body().string());
            return json;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    public JSONObject postJSON(String url, RequestBody datos) {
        Request request = new Request.Builder().url(url).post(datos).build();
        try (Response response = cliente.newCall(request).execute()) {
            JSONObject json = new JSONObject(response.body().string());
            return json;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    public JSONObject putJSON(String url, RequestBody datos) {
        Request request = new Request.Builder().url(url).put(datos).build();
        try (Response response = cliente.newCall(request).execute()) {
            JSONObject json = new JSONObject(response.body().string());
            return json;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
}
