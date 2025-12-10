package com.trip.member.service;

import com.trip.member.model.UserDAO;
import com.trip.member.model.UserDTO;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스.
 * Controller(서블릿 등)와 DAO 사이의 중개자 역할을 합니다.
 */
public class UserService {

	private UserDAO userDAO;

    /**
     * UserService 생성자입니다.
     * 내부적으로 UserDAO 객체를 생성하여 초기화합니다.
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * 로그인 비즈니스 로직을 처리합니다.
     * * @param user 로그인을 시도하는 사용자의 정보 (username, password)
     * @return 로그인 성공 시 사용자 정보가 담긴 UserDTO, 실패 시 null
     * @see com.trip.member.model.UserDAO#login(UserDTO)
     */
    public UserDTO login(UserDTO user) {
        // 향후 서비스 로직(ex: 로그인 시도 횟수 확인)이 추가될 수 있습니다.
        return userDAO.login(user);
    }

    /**
     * 사용자의 현재 상태(활성, 정지, 탈퇴 등)를 확인하는 로직을 처리합니다.
     * * @param user 확인할 사용자의 user_id가 담긴 UserDTO
     * @return 사용자의 상태 ID (user_status_id)
     * @see com.trip.member.model.UserDAO#loginCheck(UserDTO)
     */
    public int loginCheck(UserDTO user) {
        return userDAO.loginCheck(user);
    }

    /**
     * 사용자 ID를 기준으로 사용자 정보를 조회하는 로직을 처리합니다.
     * * @param userId 조회할 사용자의 고유 번호 (user_id)
     * @return 사용자의 상세 정보가 담긴 UserDTO
     * @see com.trip.member.model.UserDAO#userInfo(String)
     */
    public UserDTO getUserById(String userId) {
        return userDAO.userInfo(userId);
    }

}
