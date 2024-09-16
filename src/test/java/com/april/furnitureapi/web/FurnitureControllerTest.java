package com.april.furnitureapi.web;

import static com.april.furnitureapi.utils.JsonUtils.contentMatcher;
import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.FURNITURE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import com.april.furnitureapi.web.dto.comment.CommentCreationDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureCreationDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureUpdateDto;
import com.april.furnitureapi.web.mapper.CommentMapper;
import com.april.furnitureapi.web.mapper.FurnitureMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureMockMvc
class FurnitureControllerTest {
    private final String url = API + FURNITURE;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private FurnitureMapper furnitureMapper;
    @Autowired
    private CommentMapper commentMapper;

    private static Stream<Arguments> testForbidToSaveUnValidItem() {
        return Stream.of(
                Arguments.of("1000", "N", 5, FurnitureCategory.TABLE, FurnitureDomain.KITCHEN,
                        "Description", 2L),
                Arguments.of("-1", "New item", 5, FurnitureCategory.TABLE, FurnitureDomain.KITCHEN,
                        "Description", 2L),
                Arguments.of("1000", "New item", -1, FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, "Description", 2L),
                Arguments.of("1000", "New item", 5, null, FurnitureDomain.KITCHEN, "Description",
                        2L),
                Arguments.of("1000", "New item", 5, FurnitureCategory.TABLE, null, "Description",
                        2L), Arguments.of("1000", "New item", 5, FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, "D", 2L),
                Arguments.of("1000", "New item", 5, FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, "Description", null),
                Arguments.of(null, "New item", 5, FurnitureCategory.TABLE, FurnitureDomain.KITCHEN,
                        "Description", 2L),
                Arguments.of("1000", null, 5, FurnitureCategory.TABLE, FurnitureDomain.KITCHEN,
                        "Description", 2L),
                Arguments.of("1000", "New item", null, FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, "Description", 2L),
                Arguments.of("1000", "New item", 5, FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, null, 2L));
    }

    private static Stream<Arguments> testForbidToLeaveUnValidComment() {
        return Stream.of(Arguments.of("Content", 5.1), Arguments.of("", 5.0),
                Arguments.of("Content", -1.2));
    }

    private static Stream<Arguments> testSorting() {
        return Stream.of(
                Arguments.of("cheap", "Stol1"),
                Arguments.of("expensive", "Stol2"),
                Arguments.of("incomprehensible", "Stol4")
        );
    }

    @AfterEach
    void cleanAll() {
        warehouseRepository.deleteAll();
        furnitureRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Sql({"/users-create.sql", "/warehouse-create.sql", "/furniture-create.sql",
            "/warehouse-storage-create.sql"})
    @WithMockUser(roles = "ADMIN", username = "email@gmail.com")
    void testSaveFurniture() throws Exception {
        var furnitureJson =
                getFurnitureCreationDtoJson("1000", "New item", 5, FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, "Description", 2L);

        var saveResponse = mockMvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON).content(furnitureJson));
        var furnitureExpectedDto = furnitureMapper.furnitureToFurnitureDto(
                furnitureRepository.findByCategory(FurnitureCategory.TABLE).get(4));
        saveResponse.andExpectAll(status().isCreated(), contentMatcher(furnitureExpectedDto));
    }

    @Test
    @Sql("/users-create.sql")
    @WithMockUser(username = "email4@gmail.com", roles = "ADMIN")
    void testForbidSaveFromUnverifiedUser() throws Exception {
        var furnitureJson =
                getFurnitureCreationDtoJson("1000", "New item", 5, FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, "Description", 2L);

        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(furnitureJson))
                .andExpectAll(status().isForbidden(),
                        jsonPath("$.message").value("Please verify your account"));
    }

    @ParameterizedTest
    @MethodSource
    @Sql("/users-create.sql")
    @WithMockUser(username = "email@gmail.com", roles = "ADMIN")
    void testForbidToSaveUnValidItem(String price, String title, Integer amount,
                                     FurnitureCategory category, FurnitureDomain domain,
                                     String description, Long warehouseId) throws Exception {
        var funitureCreationDto =
                getFurnitureCreationDtoJson(price, title, amount, category, domain, description,
                        warehouseId);
        mockMvc.perform(
                        post(url).contentType(MediaType.APPLICATION_JSON).content(funitureCreationDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    @WithMockUser("email@gmail.com")
    void testAddComment() throws Exception {
        var commentCreationDto = getCommentCreationDto();

        mockMvc.perform(post(url + "/comments/{vendorCode}", 9543422).contentType(
                                MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreationDto)))
                .andExpectAll(status().isCreated(), jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.content").value("Content"), jsonPath("$.rating").value(4.5));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/comments-create.sql"})
    @WithMockUser("email@gmail.com")
    void testForbidToLeaveComment() throws Exception {
        var commentCreationDto = getCommentCreationDto();

        mockMvc.perform(post(url + "/comments/{vendorCode}", 4238748).contentType(
                                MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreationDto)))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource
    @WithMockUser("email@gmail.com")
    void testForbidToLeaveUnValidComment(String content, Double rating) throws Exception {
        var unValidComment = new CommentCreationDto();
        unValidComment.setContent(content);
        unValidComment.setRating(rating);

        mockMvc.perform(post(url + "/comments/{vendorCode}", 8563276).contentType(
                                MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(unValidComment)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/comments-create.sql"})
    void testGetComments() throws Exception {
        var commentResponse = mockMvc.perform(get(url + "/comments/{vendorCode}", 4238748))
                .andExpectAll(status().isOk(),
                        jsonPath("$.length()").value(2))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var expectedCommentList =
                furnitureRepository.findByVendorCode("4238748").get().getComments().stream()
                        .map(commentMapper::commentToDto).toList();
        var expectedJson = objectMapper.writeValueAsString(expectedCommentList);
        assertEquals(objectMapper.readTree(expectedJson), objectMapper.readTree(commentResponse));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testFindByVendorCode() throws Exception {
        var furnitureDetailedDto = furnitureMapper.furnitureToDetailedDto(
                furnitureRepository.findByVendorCode("4238748").get());

        mockMvc.perform(get(url + "/{vendorCode}", "4238748"))
                .andExpectAll(status().isOk(), contentMatcher(furnitureDetailedDto));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testGetFurnitureByDomain() throws Exception {
        mockMvc.perform(get(url + "/{domain}/{category}", "kitchen", "table"))
                .andExpectAll(status().isOk(), jsonPath("$[0].title").value("Stol4"));
    }

    @ParameterizedTest
    @MethodSource
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testSorting(String sortBy, String title) throws Exception {
        mockMvc.perform(get(url + "/{domain}/{category}/{sortBy}", "kitchen", "table", sortBy))
                .andExpectAll(status().isOk(),
                        jsonPath("$[0].title").value(title));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    @WithMockUser(roles = "ADMIN")
    void testUpdateFurniture() throws Exception {
        var updatedDto = new FurnitureUpdateDto();
        updatedDto.setPrice(9999L);

        mockMvc.perform(
                        patch(url + "/update/{vendorCode}", 7891187).contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpectAll(status().isOk(), jsonPath("$.title").value("Stol2"),
                        jsonPath("$.price").value(9999));
    }

    private String getFurnitureCreationDtoJson(String price, String title, Integer amount,
                                               FurnitureCategory category, FurnitureDomain domain,
                                               String description, Long warehouseId)
            throws Exception {
        var furnitureCreationDto = new FurnitureCreationDto();
        furnitureCreationDto.setPrice(price);
        furnitureCreationDto.setTitle(title);
        furnitureCreationDto.setAmount(amount);
        furnitureCreationDto.setCategory(category);
        furnitureCreationDto.setDomain(domain);
        furnitureCreationDto.setDescription(description);
        furnitureCreationDto.setWarehouseId(warehouseId);
        return objectMapper.writeValueAsString(furnitureCreationDto);
    }

    private CommentCreationDto getCommentCreationDto() {
        var commentCreationDto = new CommentCreationDto();
        commentCreationDto.setRating(4.5);
        commentCreationDto.setContent("Content");
        return commentCreationDto;
    }
}