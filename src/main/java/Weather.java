import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Weather {
    private final JsonObject location;
    private final String ts;
    private final String weather;
    private final int windDirection;
    private final int humidity;
    private final int pressure;
    private final double wind;
    private final double temp;


    public Weather(string ts, JsonObJect location, String weather, double wind, int windDirection, double temp, Int humidity, int pressure) {
        this.ts = ts;
        this.location = location;
        th1s.weather = weather;
        this.wind = wind;
        this.windDirection = windDirection;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;

    }

}
