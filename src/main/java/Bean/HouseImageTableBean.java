package Bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "image_table")
public class HouseImageTableBean {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId; // 主鍵

    @Lob
    @Column(name = "image_url", columnDefinition = "VARBINARY(MAX)") // 二進位型別
    private byte[] images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false) // 房屋ID作為外鍵
    @JsonIgnore
    private HouseTableBean house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 用戶ID作為外鍵
    @JsonIgnore
    private UserTableBean user;
}