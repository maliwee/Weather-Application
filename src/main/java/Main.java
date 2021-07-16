import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        File input = new File("cities.json");

        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();

            //Process all cities

            ArrayList<String> cities = new ArrayList<>();
            JsonArray jsonArrayOfCities = fileObject.get("List").getAsJsonArray();
            for (JsonElement cityElement : jsonArrayOfCities){
                //Get the JsonObject:
                JsonObject cityJsonObject = cityElement.getAsJsonObject();

                //Extract data
                String code = cityJsonObject.get("CityCode").getAsString();

                cities.add(code);
            }
            System.out.println("Extracted City Codes are: " +cities+"\n\n");
            System.out.println("---------------Weather Statistics---------------"+"\n");

            for (int i=0; i< cities.size(); i++){
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?id="+cities.get(i)+"&appid=b3a4d81b68e722cd2938d6f76615f541");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline.toString());

                //Get the required object from the above created object
                JSONObject obj = (JSONObject) data_obj.get("sys");
                String sysCountry = String.valueOf(obj.get("country"));
                String sysSunrise = String.valueOf(obj.get("sunrise"));
                String sysSunset = String.valueOf(obj.get("sunset"));

                JSONObject mainObj = (JSONObject) data_obj.get("main");
                String mainTemp = String.valueOf(mainObj.get("temp"));
                String mainPressure = String.valueOf(mainObj.get("pressure"));
                String mainHumidity = String.valueOf(mainObj.get("humidity"));
                String mainTempMin = String.valueOf(mainObj.get("temp_min"));
                String mainTempMax = String.valueOf(mainObj.get("temp_max"));

                String objVisibility = String.valueOf(data_obj.get("visibility"));

                JSONObject windObj = (JSONObject) data_obj.get("wind");

                String windSpeed = String.valueOf(windObj.get("speed"));
                String windDeg = String.valueOf(windObj.get("deg"));

                JSONObject cloudsObj = (JSONObject) data_obj.get("clouds");

                String cloudsAll = String.valueOf(cloudsObj.get("all"));

                String dt = String.valueOf(data_obj.get("dt"));
                String id = String.valueOf(data_obj.get("id"));
                String name = String.valueOf(data_obj.get("name"));


                System.out.println("Country: "+sysCountry);
                System.out.println("Sunrise: "+sysSunrise);
                System.out.println("Sunset: "+sysSunset);

                JSONArray arr = (JSONArray) data_obj.get("weather");
                JSONObject weatherObj = (JSONObject) arr.get(0);

                String weatherMain = String.valueOf(weatherObj.get("main"));
                String weatherDescription = String.valueOf(weatherObj.get("description"));

                StringBuilder sb = new StringBuilder();
                sb.append("Weather: ").append("{").append("main:").append(weatherMain).append(", ").append("Description:").append(weatherDescription)
                        .append("}");

                System.out.println(sb);

                System.out.println("Temp: "+mainTemp);
                System.out.println("Pressure: "+mainPressure);
                System.out.println("Humidity: "+mainHumidity);
                System.out.println("TempMin: "+mainTempMin);
                System.out.println("TempMax: "+mainTempMax);


                System.out.println("Visibility: "+objVisibility);
                System.out.println("Wind Speed: "+windSpeed);
                System.out.println("Wind Deg: "+windDeg);

                System.out.println("Clouds All: "+cloudsAll);

                System.out.println("dt: "+dt);
                System.out.println("id: "+id);
                System.out.println("Name: "+name+"\n");
                System.out.println("<---------------------------------------------------------->"+"\n");




            }}


        }catch (FileNotFoundException | ProtocolException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}