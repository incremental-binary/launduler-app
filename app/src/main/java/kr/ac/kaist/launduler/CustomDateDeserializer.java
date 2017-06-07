package kr.ac.kaist.launduler;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by woonggyu on 2017. 6. 6..
 */
public class CustomDateDeserializer extends StdDeserializer<Calendar> {

    private SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public CustomDateDeserializer() {
        this(null);
    }



    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Calendar deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String date_string = jsonparser.getText();
        try {

            Date date = formatter.parse(date_string);
            Calendar c =Calendar.getInstance();
            c.setTime(date);
            return c;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
