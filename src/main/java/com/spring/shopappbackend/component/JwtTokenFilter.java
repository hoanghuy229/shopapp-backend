package com.spring.shopappbackend.component;

import com.spring.shopappbackend.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try{
            // kiểm tra yêu cầu hiện tại có cần JWT
            if(isByPassToken(request)){
                filterChain.doFilter(request,response);
                return; //nếu isByPassToken trả về true thì kết thúc doFilterInternal => request tới endpoints
            }

            final String authHeader = request.getHeader("Authorization");//kiểm tra header có chứa Authorization không ?
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
                return;
            }
            final String token = authHeader.substring(7); //cắt chuỗi lấy token
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);//giải mã và lấy thông tin về sđt
            //có sđt và chưa được xác thực
            if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User user = (User) userDetailsService.loadUserByUsername(phoneNumber);//lấy user info trong CSDL bằng sđt
                if(jwtTokenUtil.validateToken(token,user)){// kiểm tra token
                    UsernamePasswordAuthenticationToken authenticationToken = new
                            UsernamePasswordAuthenticationToken(user,null,user.getAuthorities()); // lưu thông tin user
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //set thông tin chi tiết về request
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);//xác thực ngươời dùng
                }
            }
            filterChain.doFilter(request,response);

        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        }

    }

    //phương thức cho các request không cần xác thực JWT
    private boolean isByPassToken(@NonNull HttpServletRequest request){
        //tạo ra list Pair chứa các url(first) và method(second) không cần xác thực jwt
        final List<Pair<String,String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products",apiPrefix),"GET"),
                Pair.of(String.format("%s/categories",apiPrefix),"GET"),
                Pair.of(String.format("%s/users/register",apiPrefix),"POST"),
                Pair.of(String.format("%s/users/login",apiPrefix),"POST"),
                Pair.of(String.format("%s/users/otp",apiPrefix),"POST"),
                Pair.of(String.format("%s/roles",apiPrefix),"GET"),
                Pair.of(String.format("%s/products",apiPrefix),"GET")
        );
        //lặp qua từng http request
        for(Pair<String,String> bypassToken : bypassTokens){
            //kiểm tra nếu endpoint và method có giống với cặp giá trị của bypassToken không
            if(request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())){
                return true;
            }
        }

        return false;
    }
}
