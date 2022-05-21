package org.practicefx.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class GsonUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static Gson gson;

    // 经典双重锁校验  单例模式 
    public static Gson getGson() {
        if (gson == null) {
            synchronized (GsonUtils.class) {
                if (gson == null) {
                    gson = new GsonBuilder()
                            .setDateFormat(DATE_FORMAT)
                            .registerTypeAdapter(LocalTime.class, new GsonLocalTimeTypeAdapter())
                            .registerTypeAdapter(Date.class, new GsonDateTypeAdapter())
                            .registerTypeAdapter(LocalDate.class, new GsonLocalDateTypeAdapter())
                            .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeTypeAdapter())
                            .registerTypeAdapter(ByteBuffer.class, new GsonByteBufferTypeAdapter())
                            .registerTypeAdapter(byte[].class, new GsonByteArrayTypeAdapter())
                            .disableHtmlEscaping()
                            .create();
                }
            }
        }
        return gson;
    }
    
    // 只是简单的转换 
    public static String toJsonString(Object obj) {
        String result = getGson().toJson(obj);
        
        return result;
    }
    public static <T> T fromJsonString(String originJson, Class<T> classOfT) {
        T objT = getGson().fromJson(originJson, classOfT);
        
        return objT;
    }

    public static boolean isValidJson(String content) {
        Object parsedValue = null;
        try {
            parsedValue = getGson().fromJson(content, JsonObject.class);
        } catch (Exception e) {
            try {
                parsedValue = getGson().fromJson(content, new TypeToken<List<JsonObject>>(){}.getType());
            } catch (Exception e2) {
                try {
                    parsedValue = getGson().fromJson(content, new TypeToken<List<JsonPrimitive>>(){}.getType());
                } catch (Exception e3) {
                    return false;
                }
            }
        }
        return parsedValue != null;
    }
    
    public static String toJsonGZIP(Object value) throws Exception {
        String jsonStr = getGson().toJson(value);
        return compress(jsonStr);
    }
    
    public static <T> T fromJsonGZIP(String json, Class<T> classOfT) throws Exception {
        String decompressedJson = decompress(json);
        return getGson().fromJson(decompressedJson, classOfT);
    }
    
    public static <T> T fromJsonGZIP(String json, Type typeOfT) throws Exception {
        String decompressedJson = decompress(json);
        return getGson().fromJson(decompressedJson, typeOfT);
    }

    // 对字符串进行压缩  
    private static String compress(String str) throws Exception {
        if (str == null || str.length() == 0) {
            return "";
        }
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes(StandardCharsets.ISO_8859_1));
        gzip.close();
        return obj.toString(StandardCharsets.ISO_8859_1.name());
    }

    private static String decompress(String json) throws Exception {
        byte[] bytes = json.getBytes(StandardCharsets.ISO_8859_1);
        if (bytes == null || bytes.length == 0) {
            return "{}";
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, StandardCharsets.ISO_8859_1));
        String outStr = "";
        String line;
        while ((line = bf.readLine()) != null) {
            outStr += line;
        }
        return outStr;
    }
    
    /**
     * Gson 转码过程中的各种适配器 
     * @author not eron
     *
     */
    public static class GsonDateTypeAdapter implements JsonDeserializer<Date> {
         
        private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
        private static final String START_OF_DAY = "T00:00:00";
         
        @Override 
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try { 
                String jsonStr = json.getAsJsonPrimitive().getAsString();
                return parseDate(jsonStr);
            } catch (ParseException e) { 
                throw new JsonParseException(e.getMessage(), e);
            } 
        } 
         
        private Date parseDate(String dateString) throws ParseException {
            if (dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            if (dateString.length() < 19) {
                if (dateString.length() == 10) {
                    dateString = dateString + START_OF_DAY;
                } else {
                    dateString = dateString + START_OF_DAY.substring(START_OF_DAY.length() + dateString.length() - 19);
                }
            }
            return new SimpleDateFormat(DATE_PATTERN).parse(dateString);
        }
        
    }
    
    
    public static class GsonLocalTimeTypeAdapter implements JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {

        @Override
        public LocalTime deserialize(JsonElement json, Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {
            String jsonStr = json.getAsJsonPrimitive().getAsString();
            return LocalTime.parse(jsonStr);
        }

        @Override
        public JsonElement serialize(final LocalTime src, final Type typeOfSrc,
                final JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
        
    }
    
    
    public static class GsonLocalDateTypeAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
    
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {
            try { 
                String jsonStr = json.getAsJsonPrimitive().getAsString();
                return parseLocalDateTime(jsonStr);
            } catch (ParseException e) { 
                throw new JsonParseException(e.getMessage(), e);
            } 
        }
        
        private LocalDate parseLocalDateTime(String dateString) throws ParseException {
            if (dateString != null && dateString.trim().length() > 0) {
                if (dateString.contains("T")) {
                    dateString = dateString.substring(0, dateString.indexOf("T"));
                }
                return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
            } else { 
                return null; 
            } 
        }
    
        @Override
        public JsonElement serialize(final LocalDate src, final Type typeOfSrc,
                final JsonSerializationContext context) {
            return new JsonPrimitive(DateTimeFormatter.ISO_DATE.format(src));
        }
    
    }
    
    public static class GsonLocalDateTimeTypeAdapter implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try { 
                String jsonStr = json.getAsJsonPrimitive().getAsString();
                return parseLocalDateTime(jsonStr);
            } catch (ParseException e) {
                throw new JsonParseException(e.getMessage(), e);
            } 
        }
        
        private LocalDateTime parseLocalDateTime(final String dateString) throws ParseException {
            if (dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            if (dateString.length() == 10) {
                return LocalDate.parse(dateString).atStartOfDay();
            }
            return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
        }

        @Override
        public JsonElement serialize(final LocalDateTime src, final Type typeOfSrc, final JsonSerializationContext context) {
            String strDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(src);
            return new JsonPrimitive(strDateTime);
        }
    }
    
    public static class GsonByteBufferTypeAdapter implements JsonDeserializer<ByteBuffer>, JsonSerializer<ByteBuffer> {

        @Override
        public JsonElement serialize(ByteBuffer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.getEncoder().encodeToString(src.array()));
        }

        @Override
        public ByteBuffer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            byte[] bytes = Base64.getDecoder().decode(json.getAsString());
            return ByteBuffer.wrap(bytes);
        }
         
        
    }
    
    public static class GsonByteArrayTypeAdapter implements JsonDeserializer<byte[]>, JsonSerializer<byte[]> {

        @Override
        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
        }

        @Override
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.getDecoder().decode(json.getAsString());
        }
         
        
    }
    
}
