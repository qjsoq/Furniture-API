package com.april.furnitureapi.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.List;
import org.springframework.test.web.servlet.ResultMatcher;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <T> T readValue(String json, Class<T> expectedClass) throws IOException {
        return objectMapper.readValue(json, expectedClass);
    }

    public static <T> ResultMatcher contentMatcher(T expected) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actual = readValue(json, (Class<T>) expected.getClass());
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        };
    }

    public static <T> ResultMatcher contentListMatcher(List<T> expected) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            List<T> actual = objectMapper.readValue(json, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, expected.get(0).getClass()));
            assertThat(actual).isEqualTo(expected);
        };
    }
}
