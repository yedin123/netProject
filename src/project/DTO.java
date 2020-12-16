package project;

public class DTO {

	   private String id;
	   private String pw;
	   private String nName;
	   private String name;
	   private String email;
	   private String birth;
	   private String online;//intÀÏ¼öµµ??
	   private String time;
	   private String msg;
	   private String ip;
	   private String port;

	   public DTO() {
			this("" , "", "", "", "", "","" , "", "", "", "");
		}
	   public DTO(String id, String pw, String nName, String name, String email, String birth, 
			   String online, String time, String msg, String ip, String port) {
		   this.id = id;
		   this.pw = pw;
		   this.nName = nName;
		   this.name = name;
		   this.email = email;
		   this.birth = birth;
		   this.online = online;
		   this.time = time;
		   this.msg = msg;
		   this.ip = ip;
		   this.port = port;
		
	   }
	   public String getId() {
		   return id;
	   }
	   
	   
	   public void setId(String id) {
		   this.id = id;
	   }


	   public String getPw() {
			return pw;
		}
	
	
		public void setPw(String pw) {
			this.pw = pw;
		}
	
	
		public String getnName() {
			return nName;
		}
	
	
		public void setnName(String nName) {
			this.nName = nName;
		}
	
	
		public String getName() {
			return name;
		}
	
	
		public void setName(String name) {
			this.name = name;
		}
	
	
		public String getEmail() {
			return email;
		}
	
	
		public void setEmail(String email) {
			this.email = email;
		}
	
		public String getBirth() {
			return birth;
		}
	
	
		public void setBirth(String birth) {
			this.birth = birth;
		}
	
	
		public String getOnline() {
			return online;
		}
	
	
		public void setOnline(String online) {
			this.online = online;
		}
	
	
		public String getTime() {
			return time;
		}
	
	
		public void setTime(String time) {
			this.time = time;
		}
	
	
		public String getMsg() {
			return msg;
		}
	
	
		public void setMsg(String msg) {
			this.msg = msg;
		}
	
	
		public String getIp() {
			return ip;
		}
	
	
		public void setIp(String ip) {
			this.ip = ip;
		}
	
	
		public String getPort() {
			return port;
		}
	
	
		public void setPort(String port) {
			this.port = port;
		}
	
	
		@Override
		   public String toString() {
		      return "DTO [id=" + id + ", pw=" + pw + ", nName=" + nName + ", name=" + name + ", email=" + email + ", birth="
		            + birth + "]";
		   }
	}
