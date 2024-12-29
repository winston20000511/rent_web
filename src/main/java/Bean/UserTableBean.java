
package Bean;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_table")
public class UserTableBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主鍵自動生成
    @Column(name = "user_id") // 對應 user_id 欄位
    private Long userId;

    @Column(name = "name", nullable = false, length = 50) // 名稱必填，長度限制 50 字
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 50) // 電子郵件必填，唯一，長度限制 50 字
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "NVARCHAR(MAX)") // 密碼必填，允許長文本
    private String password;

    @Column(name = "phone", length = 20) // 手機號碼，非必填，長度限制 20 字
    private String phone;

    @Column(name = "picture") // 使用者圖片，以二進制格式儲存
    private byte[] picture;

    @Column(name = "createtime") // 建立時間，資料庫自動生成，程式無需手動插入或更新
    private LocalDateTime createTime;

    @Column(name = "gender", nullable = false) // 性別必填，0 表示男，1 表示女
    private Byte gender;

    @Column(name = "coupon", nullable = false) // 初始優惠券數量，預設為 3
    private Byte coupon = 3;

    @Column(name = "status", nullable = false) // 使用者狀態，1 表示啟用，0 表示停用
    private Byte status;

    // 與 HouseTableBean 的一對多關聯
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // 避免序列化時導致遞迴引用
    private List<HouseTableBean> houses;

    // 與 BookingBean 的一對多關聯
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookingBean> bookings;

    // 與 HouseImageTableBean 的一對多關聯
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HouseImageTableBean> images;

    // 與 AdBean 的一對多關聯
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AdBean> ads;

    // 與 OrderBean 的一對多關聯
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderBean> orders;
}