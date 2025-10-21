//package com.example.funding.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@Profile("dev")
//public class DevMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                //CORS 프리플라이트 무조건 통과
//                if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//                    return true;
//                }
//
//                String id = request.getHeader("X-Dev-Creator-Id");
//
//                if (id == null) {
//                    response.sendError(400, "X-Dev-Creator-Id header required");
//                    return false;
//                }
//                request.setAttribute("creatorId", Long.valueOf(id));
//                return true;
//            }
//        }).addPathPatterns("/api/v1/creator/**");
//    }
//}
