package com.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;


@Document(collection = "user")
@CompoundIndexes({
    @CompoundIndex(name = "education_unique_index", def = "{'username': 1, 'educationList.educationType': 1, 'educationList.institutionName': 1, 'educationList.course': 1}", unique = true)
})
public class User {
    @Id
    private ObjectId id;
    @Size(min = 3, message = "Minimum length of username should be 3")
    @NotBlank(message = "Username is mandatory")
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
    @Transient
    @Pattern(
        regexp = "^(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+]{6,}$",
        message = "Password must be at least 6 characters long, contain at least one number, and include only letters, numbers, and special characters (!@#$%^&*()_+)."
    )
    private String rawPassword;
    private String password;

    private List<UserEducation> educationList;
    private UserDetails userDetails;

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserEducation> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<UserEducation> educationList) {
        this.educationList = educationList;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
        setPassword(rawPassword);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }
}
