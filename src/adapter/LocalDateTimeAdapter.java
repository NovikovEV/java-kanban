package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import util.DataTimeFormat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        localDateTime.format(DataTimeFormat.getDTF());
        jsonWriter.value(localDateTime.format(DataTimeFormat.getDTF()));
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return LocalDateTime.of(LocalDate.of(1, 1, 1), LocalTime.of(0, 0));
        }

        return LocalDateTime.parse(jsonReader.nextString(), DataTimeFormat.getDTF());
    }
}
