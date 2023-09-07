package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.ValidateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private long id;
    private String fullName;
    private String birthday;
    private String phoneNumber;
    private String address;
    private String email;
    private String userName;
    private String password;
    private Role role;
    private LocalDateTime creationTime;

    public User(String raw) {
        raw = raw.replaceAll("\"", "");
        String[] userInformation = raw.split(",");
        this.id = Long.parseLong(userInformation[0]);
        this.fullName = userInformation[1];
        this.birthday = userInformation[2];
        this.phoneNumber = userInformation[3];
        this.address = userInformation[4];
        this.email = userInformation[5];
        this.userName = userInformation[6];
        this.password = userInformation[7];
        this.role = Role.parseRole(userInformation[8]);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.creationTime = ValidateUtils.parseTime(userInformation[9]);
    }

    public String getFirstName() {
        int lastWhiteSpaceIndex = this.fullName.lastIndexOf(' ');
        if (lastWhiteSpaceIndex == -1) return this.fullName;
        return this.fullName.substring(lastWhiteSpaceIndex + 1);
    }

    public String getLastName() {
        int lastWhiteSpaceIndex = this.fullName.lastIndexOf(' ');
        if (lastWhiteSpaceIndex == -1) return "";
        return this.fullName.substring(0, lastWhiteSpaceIndex);
    }

    @Override
    public String toString() {
        return id +
                "," + fullName +
                "," + birthday +
                "," + phoneNumber +
                "," + address +
                "," + email +
                "," + userName +
                "," + password +
                "," + role +
                "," + creationTime;
    }
}
