import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Timer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class Main {
    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    // default broker URL is : tcp://localhost:61616"

    // Name of the queue we will receive messages from
    private static String subject = "sensor.Weather";


    public static void main(String args[]) throws Exception{

        //Creating a HttpClient object
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //Creating a HttpGet object
        HttpGet httpget = new HttpGet("https://api.openweathermap.org/data/2.5/weather?q=Madrid&appid=571acc31657a43c79187ce032c7cbcbe");

        //Printing the method used
        System.out.println("Request Type: "+httpget.getMethod());

        //Executing the Get request
        HttpResponse httpresponse = httpclient.execute(httpget);

        Scanner sc = new Scanner(httpresponse.getEntity().getContent());

        //Printing the status line
        System.out.println(httpresponse.getStatusLine());
        while(sc.hasNext()) {
            System.out.println(sc.nextLine());
        }

        // Creamos el contador  para que cada 15 minutos se ejecute
        Timer timer =  new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //Ahora nos tocaría mandar la solicitud
                try {
                    DateTimeformatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:nn:ss");
                    String ts = dtf.format(LocalDateTime.now()); // Fecha y hora para el elemento ts

                    System.out.println("*** Fecha y hora de llamada al servicio OpenWeatherMap: " + ts + " ***\n");

                    HttpResponse<String> response = HttpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    System.out.println("*** Respuesta del servicio OpenWeatherMap ***");
                    System.out.println(response.body() + "\n");


                    // Buscamos los valores de las temperaturas
                    JsonObject js = new Gson().fromJson(response.body(), Json0bject.class);

                    Json0bject location = js.get("coord").getAsJsonObject();

                    JsonArray weather_a = js.get("weather").getAsJsonArray();
                    Json0bject weather_o = weather_o.get(0).getAsJsonobject();
                    string weather = weather_o.get("main").getAsString();

                    Jsonobject main = js.get("main").getAsJsonobject();
                    double temp = main.get("temp").getAsDouble();
                    int humidity = main.get("humidity").getAsInt();
                    Int pressure = main.get("pressure").getAsInt();

                    Json0bject wind_o = js.get("wind").getAsJsonobject();
                    double wind = wind_o.get("speed").getAsDouble();
                    int windDirection = wind_o.get("deg").getAsInt();

                    //Creamos el objeto de tipo weather
                    Weather clima = new Weather(ts, location, weather, wind, windDirection, temp, humidity, pressure);

                    //Serializanps a Json
                    String Json_final = new Gson().toJson(clima);


                    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
                    Connection connection = connectionFactory.createConnection();
                    connection.start();

                    //Creating a non transactional session to send/receive JMS message.
                    Session session = connection.createSession(false,
                            Session.AUTO_ACKNOWLEDGE);

                    //Destination represents here our queue 'sensor.weather' on the JMS server.
                    //The queue will be created automatically on the server.
                    Destination destination = session.createTopic(topic);

                    // MessageProducer is used for sending messages to the queue.
                    MessageProducer producer = session.createProducer(destination);

                    // We will send a small text message saying 'Hello World!!!'
                    TextMessage message = session
                            .createTextMessage(Json_final);

                    // Here we are sending our message!
                    producer.send(message);
                    connection.close();

                } catch (Exception e) {
                    System.out.println("Problemas con el servidor");
                }
            }
        };






                //Ejecutamos a el contador
            timer.schedule(task, delay:0, period:1000*60*15);

        }



    }


}




























    }
}



