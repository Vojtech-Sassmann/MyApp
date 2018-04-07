package cz.vectoun.myapp.api.dto;

/**
 * DTO class for user authentication
 *
 * @author Balcirak Peter <peter.balcirak@gmail.com>
 */
public class UserAuthenticateDTO {

	private Long userId;
	private String password;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserAuthenticateDTO{" +
				"userId=" + userId +
				", password='" + password + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserAuthenticateDTO that = (UserAuthenticateDTO) o;

		if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) return false;
		return getPassword() != null ? getPassword().equals(that.getPassword()) : that.getPassword() == null;
	}

	@Override
	public int hashCode() {
		int result = getUserId() != null ? getUserId().hashCode() : 0;
		result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
		return result;
	}
}
