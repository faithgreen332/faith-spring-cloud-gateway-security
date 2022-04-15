package com.faith.mygateway.security.utils;

import com.faith.mygateway.security.dto.TEnum;
import com.faith.mygateway.security.dto.TRole;
import com.faith.mygateway.security.dto.TUser;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leeko
 * @date 2022/4/6
 **/
public class TokenAuthenticationUtil {

    public static Authentication userNamePasswordAuthenticationToken(TUser tUser) {
        String credential = tUser.getPassword();
        UserDetails userDetails = new User(tUser.getUserName(), credential,
                AuthorityUtils.commaSeparatedStringToAuthorityList(getRoleAuthorities(tUser)));
        return new UsernamePasswordAuthenticationToken(userDetails, credential, userDetails.getAuthorities());
    }

    private static String getRoleAuthorities(TUser tUser) {
        List<TRole> roles = tUser.getRoles() == null ? Lists.newArrayList() : tUser.getRoles();
        List<String> roleList = roles.stream().map(TRole::getRoleName).collect(Collectors.toList());
        List<TEnum> authorities = tUser.getAuthorities() == null ? Lists.newArrayList() : tUser.getAuthorities();

        List<String> authorityList = new ArrayList<>(authorities.size());
        authorities.forEach(tEnum -> authorityList.add(TokenAuthenticationUtil.constructAuthority(tEnum.getUrl(), tEnum.getRequestMethod())));

        StringBuilder sf = new StringBuilder();
        for (String role : roleList) {
            sf.append("ROLE_").append(role).append(",");
        }
        for (String permission : authorityList) {
            sf.append(permission).append(",");
        }

        return StringUtils.isEmpty(sf.toString()) ? Strings.nullToEmpty(null) : sf.substring(0, sf.toString().length() - 1);
    }

    public static String constructAuthority(String url, Object method) {
        return url + "_" + method;
    }

}
