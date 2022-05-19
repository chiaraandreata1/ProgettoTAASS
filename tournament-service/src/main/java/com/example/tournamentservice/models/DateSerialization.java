package com.example.tournamentservice.models;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateSerialization {

    public static final SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static class DateSerialize extends JsonSerializer<Date> {

        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            String serialized = DAY_DATE_FORMAT.format(date);

            jsonGenerator.writeString(serialized);
        }
    }

    public static class DateDeserialize extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws IOException, JacksonException {
            String formatted = jsonParser.readValueAs(String.class);
            try {
                return DAY_DATE_FORMAT.parse(formatted);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class DateListDeserialize extends JsonDeserializer<List<Date>> {

        @Override
        public List<Date> deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws IOException, JacksonException {
            ArrayList<Date> dates = new ArrayList<>();
            for (Object o : jsonParser.readValueAs(List.class)) {
                try {
                    dates.add(DAY_DATE_FORMAT.parse(String.valueOf(o)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            return dates;
        }
    }

    public static class DateTimeSerialize extends JsonSerializer<Date> {

        @Override
        public void serialize(Date date,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            String formatted = DAY_TIME_DATE_FORMAT.format(date);
            jsonGenerator.writeString(formatted);
        }
    }

    public static class DateTimeDeserialize extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws IOException, JacksonException {
            String formatted = jsonParser.readValueAs(String.class);
            try {
                return DAY_TIME_DATE_FORMAT.parse(formatted);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String serializeDate(Date date) {
        return DAY_DATE_FORMAT.format(date);
    }
}
