package com.docs.drugs.Docs.Drugs.Controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.docs.drugs.Docs.Drugs.Service.DoctorService;
import com.docs.drugs.Docs.Drugs.model.DoctorModel;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // @Autowired
    // private JwtUtil jwtUtil;

    @PostMapping("/register")
    public DoctorModel registerDoctor(@RequestBody userModelss userModel){

        DoctorModel existingDoctor = doctorService.findByEmail(userModel.getEmail());
        if(existingDoctor != null){
            return existingDoctor;
        }

        DoctorModel doctor = new DoctorModel();
        doctor.setFullName(userModel.getFullName());
        doctor.setGender(userModel.getGender());
        doctor.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        doctor.setEmail(userModel.getEmail());
        return doctorService.saveDoctor(doctor);
    }

    @GetMapping("/all")
    public List<DoctorModel> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginBody loginBody) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword()));

            if (authentication.isAuthenticated()) {
                DoctorModel doctor = doctorService.findByEmail(loginBody.getEmail());
                return ResponseEntity.ok(doctor);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication error");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
   

}


class userModelss {
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    private String gender;
    private String email;
    private String password;
    private String mobileNo;
}


class LoginBody {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
