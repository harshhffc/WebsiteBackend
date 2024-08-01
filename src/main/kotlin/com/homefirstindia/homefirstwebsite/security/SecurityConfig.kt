package com.homefirstindia.homefirstwebsite.security


import com.homefirstindia.homefirstwebsite.prop.AppProperty
import com.homefirstindia.homefirstwebsite.utils.*
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.http.entity.ContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Autowired val websiteAuthentication: WebsiteAuthentication,

) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors(Customizer.withDefaults())

            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/public/**","/api/**").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf { csrf ->
                csrf.disable()
            }
            .addFilterBefore(websiteAuthentication, BasicAuthenticationFilter::class.java)
        return http.build()
    }

    private val domains = listOf(
        "https://homefirstindia.com",
        "https://red.homefirstindia.com",
        "https://stage.homefirstindia.com",
        "http://localhost:4200",

    )

    private val allowHeader = listOf(
        "Origin",
        "X-Requested-With",
        "Content-Type",
        "Accept",
        "Key",
        "Authorization",
        "refreshToken",
        "crypt",
        "cypher",
        "userId",
        "sessionPasscode",
        "sourcePasscode"
    )

    private val maxAge: Long = 3600

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val source = UrlBasedCorsConfigurationSource()

        val configuration = CorsConfiguration()
        configuration.allowedOrigins = domains
        configuration.allowedMethods = listOf("GET", "POST")
        configuration.maxAge = maxAge
        configuration.allowedHeaders = allowHeader

        source.registerCorsConfiguration("/**", configuration)

        return source
    }

    @Component
    class WebsiteAuthentication(
        @Autowired val appProperty: AppProperty,
        @Autowired val cryptoUtils: CryptoUtils,
    ): OncePerRequestFilter() {

        @Throws(IOException::class, ServletException::class)
        override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            chain: FilterChain
        ) {

            LoggerUtils.log("WebsiteAuthentication.doFilterInternal - Path : ${request.requestURI}")

            val header = request.getHeader(SOURCE_PASSCODE)
            if (header == null) {
                chain.doFilter(request, response)
                return
            }

            val authRequest = AuthRequest(request)

            if (!cryptoUtils.validateSourcePasscode(authRequest.sourcePasscode)) {
                response.setFailureResponse(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    LocalResponse()
                        .setMessage("Invalid Session Passcode")
                        .setError(Errors.ACCESS_DENIED.value)
                        .setAction(Actions.CANCEL.value)
                )
                return
            }

            request.getRequestDispatcher(request.servletPath).forward(request, response)

        }

    }

}

private fun HttpServletResponse.setFailureResponse(
    statusCode: Int,
    localResponse: LocalResponse
) {
    this.apply {
        contentType = ContentType.APPLICATION_JSON.toString()
        status = statusCode
        outputStream.println(
            localResponse
                .toJson()
                .toString()
        )
    }
}





