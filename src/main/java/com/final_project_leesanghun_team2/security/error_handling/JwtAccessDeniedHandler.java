package com.final_project_leesanghun_team2.security.error_handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project_leesanghun_team2.domain.Response;
import com.final_project_leesanghun_team2.domain.ErrorResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException e) throws IOException, ServletException {
		ObjectMapper objectMapper = new ObjectMapper();
		log.error("No Authorities", e);
		log.error("Request Uri : {}", request.getRequestURI());
		log.error("인증은 됐으나 특정 리소스에 대한 권한이 없는 경우, 403 forbidden.");

		ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName(),
				"JwtAccessDeniedHandler 에 들어왔습니다.");
		Response<ErrorResponse> error = Response.error(errorResponse);

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
				.write(objectMapper.writeValueAsString(error)); //Response 객체를 response 의 바디값으로 파싱

		response.sendRedirect("/index");

	}
}
