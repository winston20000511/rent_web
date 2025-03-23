package DTO;

import lombok.Data;

@Data
public class HouseSimpleInfoDTO {
	private Long houseId;
	private String userEmail;
	private Long userId;
	private String userName;
	private String title;
	private Integer price;
	private String address;
	private Byte status;


	
	public HouseSimpleInfoDTO() {
		// TODO Auto-generated constructor stub
	}



	public HouseSimpleInfoDTO(Long houseId, String userEmail, Long userId, String userName, String title, Integer price,
			String address, Byte status) {
		super();
		this.houseId = houseId;
		this.userEmail = userEmail;
		this.userId = userId;
		this.userName = userName;
		this.title = title;
		this.price = price;
		this.address = address;
		this.status = status;
	}

}
