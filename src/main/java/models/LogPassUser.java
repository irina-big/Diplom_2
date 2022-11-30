package models;

public class LogPassUser {

        private String login;
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public LogPassUser(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public static LogPassUser fromUser(User user) {
            return new LogPassUser(user.getEmail(), user.getPassword());
        }

        public static LogPassUser fromLogStrPassStr(String login, String password) {
            return new LogPassUser(login, password);
        }
    }


