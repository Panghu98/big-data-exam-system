package cn.edu.swpu.exam.support;

import org.springframework.security.core.GrantedAuthority;

public class UserGrantedAuthority implements GrantedAuthority {

    private String authority;

    public UserGrantedAuthority(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }


}
