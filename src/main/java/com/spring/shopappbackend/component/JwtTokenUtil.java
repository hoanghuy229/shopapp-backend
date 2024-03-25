package com.spring.shopappbackend.component;

import com.spring.shopappbackend.exception.InvalidParamException;
import com.spring.shopappbackend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private Long expiration; //save to environment variable (application.yml)

    @Value("${jwt.secretKey}")
    private String secretKey;


    // tạo token JWT dựa trên thông tin của một đối tượng người dùng
    public String generateToken(User user) throws InvalidParamException {
        //properties => claims
        // lưu trữ các claim của JWT
        Map<String,Object> claims = new HashMap<>();
        //this.generateSecretKey();
        //thêm vào là phoneNumber, được lấy từ đối tượng User
        claims.put("phoneNumber",user.getPhoneNumber());
        claims.put("userId",user.getId());

        try {
            //tạo JWT
            String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getPhoneNumber())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
            return token;
        }
        catch (Exception e){
            //can inject logger instead of sysout
            throw new InvalidParamException("cannot create token");
            //return null;
        }
    }


    //Phương thức này trả về khóa sử dụng để ký và xác minh token.
    private Key getSignKey(){
        //giải mã chuỗi base64 thành dạng byte
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        // tạo khóa từ mảng bytes đã giải mã
        return Keys.hmacShaKeyFor(bytes);
    }

    //trích xuất tất cả các thông tin từ token JWT.
    private Claims extractAllClaims(String token){
        // phân tích cú pháp và xác minh chữ ký của token
         return Jwts.parser()
                    .setSigningKey(getSignKey()) // nhận khóa đã được tạo trước đó
                    .build()
                    .parseClaimsJws(token) //Phân tích và xác minh chữ ký của token
                    .getBody(); // Lấy claims từ phần thân của token.
    }

    //trích xuất một phần cụ thể từ các thông tin của token JWT
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        //Sử dụng phương thức extractAllClaims để lấy tất cả các thông tin từ token
        final Claims claims = this.extractAllClaims(token);
        // Áp dụng hàm claimsResolver để trích xuất giá trị từ Claims.
        return claimsResolver.apply(claims);
    }

    //check expiration
    public boolean isTokenExpired(String token){
        // trích xuất thông tin về thời gian hết hạn từ claims của token
        Date expirationDate = this.extractClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());//true nếu token đã hết hạn
    }

    // lấy ra phoneNumber trong Subject của token
    public String extractPhoneNumber(String token){
        return extractClaim(token,Claims::getSubject);
    }

    //kiểm tra token hợp lệ không
    public boolean validateToken(String token, User userDetails){
        String phoneNumber = extractPhoneNumber(token);
        if(!userDetails.isActive()){
            return false;
        }
        return (phoneNumber.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //    private String generateSecretKey(){
//        SecureRandom random = new SecureRandom();
//        byte[] keyBytes = new byte[32]; //256 bit-key
//        random.nextBytes(keyBytes);
//        String secretKey = Encoders.BASE64.encode(keyBytes);
//        return secretKey;
//    }
}



