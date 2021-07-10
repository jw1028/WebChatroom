package org.example.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JSONResponse {
    private boolean ok;
    private String reason;
}
