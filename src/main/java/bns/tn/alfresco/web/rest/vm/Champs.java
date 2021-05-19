package bns.tn.alfresco.web.rest.vm;

import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;
import java.time.LocalDate;

public class Champs implements Serializable {
 String m0;
 String m;
 String m1;
 String m2;
 String m3;
 LocalDate m4;
 LocalDate m5;

    public String getM0() {
        return m0;
    }

    public void setM0(String m0) {
        this.m0 = m0;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getM1() {
        return m1;
    }

    public void setM1(String m1) {
        this.m1 = m1;
    }

    public String getM2() {
        return m2;
    }

    public void setM2(String m2) {
        this.m2 = m2;
    }

    public String getM3() {
        return m3;
    }

    public void setM3(String m3) {
        this.m3 = m3;
    }

    public LocalDate getM4() {
        return m4;
    }

    public void setM4(LocalDate m4) {
        this.m4 = m4;
    }

    public LocalDate getM5() {
        return m5;
    }

    public void setM5(LocalDate m5) {
        this.m5 = m5;
    }

    @Override
    public String toString() {
        return "Champs{" +
            "m0='" + m0 + '\'' +
            ", m='" + m + '\'' +
            ", m1='" + m1 + '\'' +
            ", m2='" + m2 + '\'' +
            ", m3='" + m3 + '\'' +
            ", m4=" + m4 +
            ", m5=" + m5 +
            '}';
    }
}
