package jp.co.axa.apidemo.security;

public class SecurityConstants {
    public static final String SIGN_UP_URL = "/api/v1/register";
    public static final String LOGIN_URL = "/api/v1/login";
    //1024 signning key for JWT token
    public static final String KEY = "Xp2s5v8x/A?D(G+KbPeShVmYq3t6w9z$B&E)H@McQfTjWnZr4u7x!A%D*F-JaNdRgUkXp2s5v8y/B?E(H+KbPeShVmYq3t6w9z$C&F)J@NcQfTjWnZr4u7x!A%D*G-Ka";
    public static final String HEADER_NAME = "Authorization";
    //For parsing token
    public static final String TOKEN_PREFIX = "Bearer ";


}
