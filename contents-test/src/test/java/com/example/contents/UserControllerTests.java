package com.example.contents;

import com.example.contents.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
<<<<<<< HEAD
=======
import org.springframework.test.context.ActiveProfiles;
>>>>>>> origin/canary
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
<<<<<<< HEAD

=======
>>>>>>> origin/canary
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

<<<<<<< HEAD
// UserService Mocking
=======
>>>>>>> origin/canary
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

<<<<<<< HEAD
    // Controller가 있을 때, HTTP 요청이 보내졌다 가정해주는 객체
=======
    // Controller가 있을때 HTTP 요청이 보내졌다 가정해주는 객체
>>>>>>> origin/canary
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("UserDto를 나타내는 JSON 요청을 보내면 id가 null이 아닌 UserDto JSON 응답")
    public void testCreate() throws Exception {
        // given
<<<<<<< HEAD
        // 1. userService.createUser에 전달할 UserDto 준비
        String username = "kim";
        UserDto requestDto = new UserDto();
        requestDto.setUsername(username);
        // 2. userService.createUser가 반환할 UserDto 준비
=======
        // 1. userService.createUser 에 전달한 UserDto 준비
        String username = "jeeho.dev";
        UserDto requestDto = new UserDto();
        requestDto.setUsername(username);
        // 2. userService.createUser 가 반환할 UserDto 준비
>>>>>>> origin/canary
        Long userId = 1L;
        UserDto responseDto = new UserDto();
        responseDto.setUsername(requestDto.getUsername());
        responseDto.setId(userId);
<<<<<<< HEAD
        // 3. userService.createUser의 동작 과정
=======
        // 3. userService.createUser 의 동작 가정
>>>>>>> origin/canary
        when(userService.createUser(requestDto))
                .thenReturn(responseDto);

        // when
<<<<<<< HEAD
        // perform : HTTP 요청을 보낸 것을 시뮬레이션하여 UserController에게
        ResultActions result = mockMvc.perform(
                // 요청의 형태(Body 등)를 빌더처럼 정의
                post("/users")
                        .content(JsonUtil.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpectAll(
                status().is2xxSuccessful(), // 상태코드가 200
                content().contentType(MediaType.APPLICATION_JSON),   // 응답이 JSON 형태로
                jsonPath("$.username", is(username)),   // username은 요청한 값 그대로
                jsonPath("$.id", notNullValue())    // id는 null은 아닌 값
=======
        // perform: HTTP 요청을 보낸것을 시뮬레이션 하여 UserController 에게
        ResultActions result = mockMvc.perform(
                // 요청의 형태(Body 라던지)를 빌더처럼 정의
                post("/users")
                        .content(JsonUtil.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpectAll(
                status().is2xxSuccessful(),  // 상태코드가 200
                content().contentType(MediaType.APPLICATION_JSON),  // 응답이 JSON 형태로
                jsonPath("$.username", is(username)),  // username은 요청한 값 그대로
                jsonPath("$.id", notNullValue())  // id는 null은 아닌 값
        );
        mockMvc.perform(
                // 요청의 형태(Body 라던지)를 빌더처럼 정의
                post("/users")
                        .content(JsonUtil.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is2xxSuccessful(),  // 상태코드가 200
                        content().contentType(MediaType.APPLICATION_JSON),  // 응답이 JSON 형태로
                        jsonPath("$.username", is(username)),  // username은 요청한 값 그대로
                        jsonPath("$.id", notNullValue())  // id는 null은 아닌 값
>>>>>>> origin/canary
        );
    }
}
