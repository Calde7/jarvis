package com.tenpo.jarvis.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.jarvis.controller.exception.TooManyRequestException;
import com.tenpo.jarvis.dto.OperationResponseDTO;
import com.tenpo.jarvis.dto.TrackingDTO;
import com.tenpo.jarvis.service.tracking.TrackingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = { com.tenpo.jarvis.application.JarvisApplication.class })
public class OperationControllerTest {

    MockMvc mvc;

    MockHttpServletRequest request;

    @Autowired
    WebApplicationContext webApplicationContext;

    @InjectMocks
    OperationController controller;

    @Mock
    TrackingServiceImpl trackingService;


    @BeforeEach
    void init() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void test_ping_endpoint() throws Exception {
        String uri = "/v1/ping";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);
        assertEquals("pong", content);
    }

    @Test
    public void test_max_request_is_exceeded() throws Exception {
        when(trackingService.save(any())).thenThrow(TooManyRequestException.class);
        TooManyRequestException thrown = assertThrows(TooManyRequestException.class, () -> {
            controller.ping(request);
            controller.ping(request);
            controller.ping(request);
            controller.ping(request);
        });
    }

    @Test
    public void test_calculate_sum_numbers() throws Exception {
        String uri = "/v1/calculate/5/5";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        OperationResponseDTO responseDTO = mapFromJson(content, OperationResponseDTO.class);
        assertEquals(200, status);
        assertEquals(BigDecimal.valueOf(5), responseDTO.getNumber1());
        assertEquals(BigDecimal.valueOf(5), responseDTO.getNumber2());
        assertNotNull(responseDTO);
    }

    @Test
    public void test_find_tracking_history() throws Exception {
        String uri = "/v1/tracking";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        List<TrackingDTO> trackingList = mapFromJson(content, List.class);
        assertEquals(200, status);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
