package sven.phd.iot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import sven.phd.iot.hassio.bus.HassioBusState;
import sven.phd.iot.hassio.calendar.HassioCalendarState;
import sven.phd.iot.hassio.states.HassioState;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;

public class Tests {
    public static void main(String[] args) {
      /*  ObjectMapper mapper = new ObjectMapper();

        try {
            DateTest dateTest = mapper.readValue("{\"date\": \"2019-09-30T15:15:01.888888+00:00\"}", DateTest.class);
            System.out.println(dateTest.date);
        } catch (IOException e) {
            e.printStackTrace();
        } */

        PriorityQueue<HassioState> queue = new PriorityQueue<>();
        queue.add(new HassioCalendarState("1000", "bus", new Date(1000), "test", "test"));
        queue.add(new HassioCalendarState("3000", "bus", new Date(3000), "test", "test"));

        printQueue(queue);



    }

    private static void printQueue(Queue<HassioState> queue) {
        boolean isAdded = false;


        while (!queue.isEmpty()) {
            HassioState state = queue.remove();

            if(!isAdded) {
                queue.add(new HassioCalendarState("2000", "bus", new Date(2000), "test", "test"));
                isAdded = true;
            }

            System.out.println(state.state);
        }
    }

    private void tests() {
        //List<HassioState> hassioStateList = new ArrayList<HassioState>();
        // hassioStateList.add(new HassioSunState());

        //  Car car = new Car("Mercedes-Benz", "S500", 5, 250.0);
        //    Event event = new Event();
        //   event.action = Event.ACTION.PRESSED;
        //   event.effect = Event.EFFECT.CONFIRM;
        //   event.identifier = "button";

      /*  try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enableDefaultTyping();
            String jsonDataString = mapper.writeValueAsString(event);
          //  System.out.println(jsonDataString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } */

        //mapper.readValue(jsonDataString, Fleet.class);
    }
}
