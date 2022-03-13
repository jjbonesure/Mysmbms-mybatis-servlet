package com.echo.pojo;


import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Provider {
        private int id;
        private String proCode;
        private String proName;
        private String proDesc;
        private String proContact;
        private String proPhone;
        private String proAddress;
        private String proFax;
        private int createdBy;
        private Date creationDate;
        private Date modifyDate;
        private int modifyBy;
}
