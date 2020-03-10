package org.community.dot;

import lombok.Data;

@Data
public class AcceseTokenDTO {

    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
