package com.employee.Service;

import java.time.LocalDateTime;

import com.employee.dao.UserDAO;
import com.employee.file.LogUtil;
import com.employee.model.User;
import com.employee.util.Constants;
import com.employee.util.PasswordUtil;

public class LoginService {

	private final UserDAO userDAO = new UserDAO();
	private int failedAttempts = 0;

	public User login(String username, String password) {
		if (isLockedOut()) {
			LogUtil.warn("Login blocked for: " + username);
			return null;
		}
		User user = userDAO.findUsername(username);
		if (user != null && PasswordUtil.matches(password, user.getPasswordHash())) {
			failedAttempts = 0;
			userDAO.updateLastLogin(user.getUserId(), LocalDateTime.now());
			LogUtil.info("Successful login: " + username);
			return user;
		}
		failedAttempts++;
		LogUtil.warn("Failed login #" + failedAttempts + " for: " + username);
		return null;
	}

	public boolean changePassword(User user, String currentPassword, String newPassword) {
		if (!PasswordUtil.matches(currentPassword, user.getPasswordHash())) {
			LogUtil.warn("Wrong current password for: " + user.getUsername());
			return false;
		}
		String newHash = PasswordUtil.hash(newPassword);
		boolean updated = userDAO.changePassword(user.getUserId(), newHash);
		if (updated) {
			user.setPasswordHash(newHash);
			LogUtil.info("Password changed for: " + user.getUsername());
		}
		return updated;
	}

	public boolean isLockedOut() {
		return failedAttempts >= Constants.MAX_LOGIN_ATTEMPTS;
	}

	public int getRemainingAttempts() {
		return Math.max(0, Constants.MAX_LOGIN_ATTEMPTS - failedAttempts);
	}

	public void resetAttempts() {
		failedAttempts = 0;
	}

	public void logout(User user) {
		LogUtil.info("Logged out: " + (user != null ? user.getUsername() : "unknown"));
	}

}
