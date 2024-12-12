package Bean;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class formComplationBean {

	@Id @Column(name="complaints_id")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int complaints_id ;
	
	@Column(name="user_id")
	private int user_id;
	
	@Column(name="username")
	private String username ;
	
	@Column(name="category")
	private String category ;
	
	@Column(name="subject")
	private String subject ;
	
	@Column(name="content")
	private String content ;
	
	@Column(name="status")
	private String status ;

	@Column(name="note")
	private String note ;
	
	@Column(name="submission_date", insertable = false)
	private Timestamp  submission_date;
	


	public Timestamp getSubmission_date() {
		return submission_date;
	}

	public void setSubmission_date(Timestamp submission_date) {
		this.submission_date = submission_date;
	}




//	public LocalDateTime getSubmission_date() {
//		return submission_date;
//	}
//
//	public void setSubmission_date(LocalDateTime submission_date) {
//		this.submission_date = submission_date;
//	}



}
