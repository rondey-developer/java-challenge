package jp.co.axa.apidemo.security;

public final class SecurityConstants {
    public static final String SIGN_UP_URL = "/api/v1/register";
    //1024 signning key
    public static final String KEY = "Xp2s5v8x/A?D(G+KbPeShVmYq3t6w9z$B&E)H@McQfTjWnZr4u7x!A%D*F-JaNdRgUkXp2s5v8y/B?E(H+KbPeShVmYq3t6w9z$C&F)J@NcQfTjWnZr4u7x!A%D*G-Ka";
    public static final String HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
