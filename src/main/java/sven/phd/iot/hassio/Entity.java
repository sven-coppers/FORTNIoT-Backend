package sven.phd.iot.hassio;

public class Entity {
    // Lights
    public static final String LIVING_STANDING_LAMP             = "light.living_standing_lamp";
    public static final String LIVING_SPOTS                     = "light.living_spots";
    public static final String KITCHEN_SPOTS                    = "light.kitchen_spots";

    // Living - Temperature
    public static final String LIVING_TEMPERATURE               = "sensor.living_temperature_measurement";
    public static final String LIVING_THERMOSTAT                = "thermostat.living_thermostat";
    public static final String LIVING_FLOOR_HEATING             = "heater.living_floor_heating";
    public static final String LIVING_AIRCO                     = "cooler.living_airco";

    // Master Bedroom - Temperature
    public static final String BEDROOM_MASTER_TEMPERATURE       = "sensor.master_bedroom_temperature_measurement";
    public static final String BEDROOM_MASTER_THERMOSTAT        = "thermostat.master_bedroom_thermostat";
    public static final String BEDROOM_MASTER_RADIATOR          = "heater.master_bedroom_radiator";
    public static final String BEDROOM_MASTER_AIRCO             = "cooler.master_bedroom_airco";

    // Children's Bedroom - Temperature
    public static final String BEDROOM_CHILDREN_TEMPERATURE     = "sensor.children_bedroom_temperature_measurement";
    public static final String BEDROOM_CHILDREN_THERMOSTAT      = "thermostat.children_bedroom_thermostat";
    public static final String BEDROOM_CHILDREN_RADIATOR        = "heater.children_bedroom_radiator";
    public static final String BEDROOM_CHILDREN_AIRCO           = "cooler.children_bedroom_airco";

    // Children's Bedroom - Motion sensor
    public static final String BEDROOM_CHILDREN_MOTION          = "binary_sensor.children_bedroom_motion_sensor_motion";

    // Shower - Temperature
    public static final String SHOWER_TEMPERATURE               = "sensor.shower_temperature_measurement";
    public static final String SHOWER_THERMOSTAT                = "thermostat.shower_thermostat";
    public static final String SHOWER_HEATER                    = "heater.shower_heater";
    public static final String SHOWER_VENTILATION               = "cooler.shower_ventilation";

    // People and Habits
    public static final String PEOPLE_MOM                      = "person.mom";
    public static final String PEOPLE_DAD                      = "person.dad";
    public static final String PEOPLE_HOME_COUNTER             = "sensor.people_home_counter";
    public static final String ROUTINE                         = "sensor.routine";
}
