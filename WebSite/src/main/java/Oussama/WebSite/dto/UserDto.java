package Oussama.WebSite.dto;

import Oussama.WebSite.Entity.Enum.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String email;
    private String name;
    private UserRole role;
}
