package me.youngjin.infleranjavatest.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.youngjin.infleranjavatest.StudyStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Study {

    @Id @GeneratedValue
    private Long id;
    private StudyStatus status;
    private int limitation;
    private String name;

    public Study(int limitation, String name) {
        this.limitation = limitation;
        this.name = name;
    }

    public Study(int limitation) {
        if (limitation < 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }
        this.limitation = limitation;
    }

    public StudyStatus getStatus() {
        return this.status;
    }

    public int getLimitation() {
        return limitation;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Study{" +
                "status=" + status +
                ", limit=" + limitation +
                ", name='" + name + '\'' +
                '}';
    }

    public void setOwner(Member member) {
    }
}
