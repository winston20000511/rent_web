package IMPL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.HouseDetailsDTO;
import DTO.HouseSimpleInfoDTO;
import Dao.houseDAO;
import util.ConnectionUtil;

public class houseIMPL implements houseDAO {

    @Override
    public boolean updateHouseDetailsWithoutImageAndUser(Long houseId, HouseDetailsDTO houseDetails) {
        String query = "UPDATE house_table SET title = ?, price = ?, description = ?, size = ?, address = ?, status = ?, " +
                       "room = ?, bathroom = ?, livingroom = ?, kitchen = ?, floor = ?, house_type = ?, atticAddition = ?, " +
                       "pet = ?, parkingSpace = ?, elevator = ?, balcony = ?, shortTerm = ?, cooking = ?, waterDispenser = ?, " +
                       "fee = ?, genderRestrictions = ?, washingMachine = ?, airConditioner = ?, network = ?, bedstead = ?, " +
                       "mattress = ?, refrigerator = ?, ewaterHeater = ?, gwaterHeater = ?, television = ?, channel4 = ?, sofa = ?, tables = ? " +
                       "WHERE house_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, houseDetails.getTitle());
            stmt.setInt(2, houseDetails.getPrice());
            stmt.setString(3, houseDetails.getDescription());
            stmt.setInt(4, houseDetails.getSize());
            stmt.setString(5, houseDetails.getAddress());
            stmt.setByte(6, houseDetails.getStatus());
            stmt.setByte(7, houseDetails.getRoom());
            stmt.setByte(8, houseDetails.getBathroom());
            stmt.setByte(9, houseDetails.getLivingroom());
            stmt.setByte(10, houseDetails.getKitchen());
            stmt.setByte(11, houseDetails.getFloor());
            stmt.setString(12, houseDetails.getHouseType());
            stmt.setBoolean(13, houseDetails.getAtticAddition());
            stmt.setBoolean(14, houseDetails.getPet());
            stmt.setBoolean(15, houseDetails.getParkingSpace());
            stmt.setBoolean(16, houseDetails.getElevator());
            stmt.setBoolean(17, houseDetails.getBalcony());
            stmt.setBoolean(18, houseDetails.getShortTerm());
            stmt.setBoolean(19, houseDetails.getCooking());
            stmt.setBoolean(20, houseDetails.getWaterDispenser());
            stmt.setBoolean(21, houseDetails.getManagementFee());
            stmt.setByte(22, houseDetails.getGenderRestrictions());
            stmt.setBoolean(23, houseDetails.getWashingMachine());
            stmt.setBoolean(24, houseDetails.getAirConditioner());
            stmt.setBoolean(25, houseDetails.getNetwork());
            stmt.setBoolean(26, houseDetails.getBedstead());
            stmt.setBoolean(27, houseDetails.getMattress());
            stmt.setBoolean(28, houseDetails.getRefrigerator());
            stmt.setBoolean(29, houseDetails.getEwaterHeater());
            stmt.setBoolean(30, houseDetails.getGwaterHeater());
            stmt.setBoolean(31, houseDetails.getTelevision());
            stmt.setBoolean(32, houseDetails.getChannel4());
            stmt.setBoolean(33, houseDetails.getSofa());
            stmt.setBoolean(34, houseDetails.getTables());
            stmt.setLong(35, houseId);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<HouseSimpleInfoDTO> getPaginatedHouseList(int page, int pageSize, String keyword) {
        List<HouseSimpleInfoDTO> houses = new ArrayList<>();
        String query = "SELECT h.house_id, h.title, h.price, h.address, h.status, " +
                       "u.user_id, u.name AS user_name, u.email AS user_email " +
                       "FROM house_table h JOIN user_table u ON h.user_id = u.user_id " +
                       "WHERE u.name LIKE ? OR h.title LIKE ? " +
                       "ORDER BY h.house_id " +
                       "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement stmt = ConnectionUtil.getConnection().prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setInt(3, (page - 1) * pageSize); // OFFSET
            stmt.setInt(4, pageSize);              // FETCH NEXT

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                HouseSimpleInfoDTO house = new HouseSimpleInfoDTO();
                house.setHouseId(rs.getLong("house_id"));
                house.setTitle(rs.getString("title"));
                house.setPrice(rs.getInt("price"));
                house.setAddress(rs.getString("address"));
                house.setStatus(rs.getByte("status"));
                house.setUserId(rs.getLong("user_id"));
                house.setUserName(rs.getString("user_name"));
                house.setUserEmail(rs.getString("user_email"));
                houses.add(house);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database query error");
        }

        return houses;
    }
    
    @Override
    public HouseDetailsDTO getHouseDetailsById(Long houseId) {
        HouseDetailsDTO houseDetails = null;
        String query = "SELECT h.*, c.pet, c.parkingSpace, c.elevator, c.balcony, c.shortTerm, c.cooking, " +
                       "c.waterDispenser, c.fee AS managementFee, c.genderRestrictions, f.washingMachine, f.airConditioner, " +
                       "f.network, f.bedstead, f.mattress, f.refrigerator, f.ewaterHeater, f.gwaterHeater, f.television, " +
                       "f.channel4, f.sofa, f.tables " +
                       "FROM house_table h " +
                       "LEFT JOIN condition_table c ON h.house_id = c.house_id " +
                       "LEFT JOIN furniture_table f ON h.house_id = f.house_id " +
                       "WHERE h.house_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, houseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                houseDetails = new HouseDetailsDTO();
                houseDetails.setHouseId(rs.getLong("house_id"));
                houseDetails.setTitle(rs.getString("title"));
                houseDetails.setPrice(rs.getInt("price"));
                houseDetails.setDescription(rs.getString("description"));
                houseDetails.setSize(rs.getInt("size"));
                houseDetails.setAddress(rs.getString("address"));
                houseDetails.setStatus(rs.getByte("status"));
                houseDetails.setRoom(rs.getByte("room"));
                houseDetails.setBathroom(rs.getByte("bathroom"));
                houseDetails.setLivingroom(rs.getByte("livingroom"));
                houseDetails.setKitchen(rs.getByte("kitchen"));
                houseDetails.setFloor(rs.getByte("floor"));
                houseDetails.setHouseType(rs.getString("house_type"));
                houseDetails.setAtticAddition(rs.getBoolean("atticAddition"));
                houseDetails.setPet(rs.getBoolean("pet"));
                houseDetails.setParkingSpace(rs.getBoolean("parkingSpace"));
                houseDetails.setElevator(rs.getBoolean("elevator"));
                houseDetails.setBalcony(rs.getBoolean("balcony"));
                houseDetails.setShortTerm(rs.getBoolean("shortTerm"));
                houseDetails.setCooking(rs.getBoolean("cooking"));
                houseDetails.setWaterDispenser(rs.getBoolean("waterDispenser"));
                houseDetails.setManagementFee(rs.getBoolean("managementFee"));
                houseDetails.setGenderRestrictions(rs.getByte("genderRestrictions"));
                houseDetails.setWashingMachine(rs.getBoolean("washingMachine"));
                houseDetails.setAirConditioner(rs.getBoolean("airConditioner"));
                houseDetails.setNetwork(rs.getBoolean("network"));
                houseDetails.setBedstead(rs.getBoolean("bedstead"));
                houseDetails.setMattress(rs.getBoolean("mattress"));
                houseDetails.setRefrigerator(rs.getBoolean("refrigerator"));
                houseDetails.setEwaterHeater(rs.getBoolean("ewaterHeater"));
                houseDetails.setGwaterHeater(rs.getBoolean("gwaterHeater"));
                houseDetails.setTelevision(rs.getBoolean("television"));
                houseDetails.setChannel4(rs.getBoolean("channel4"));
                houseDetails.setSofa(rs.getBoolean("sofa"));
                houseDetails.setTables(rs.getBoolean("tables"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return houseDetails;
    }

    @Override
    public byte[] getSmallestImageByHouseId(Long houseId) {
        byte[] image = null;
        String query = "SELECT image_url FROM image_table WHERE house_id = ? ORDER BY image_id ASC LIMIT 1";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, houseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                image = rs.getBytes("image_url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }
}
